package com.github.sengi.undertow

import javax.servlet.{DispatcherType, Filter}

import com.github.sengi.api.Campaign
import com.github.sengi.servlets._
import com.typesafe.scalalogging.LazyLogging
import io.undertow.server.handlers.PathHandler
import io.undertow.servlet.Servlets
import io.undertow.servlet.api.{DeploymentInfo, DeploymentManager, InstanceFactory, InstanceHandle}
import io.undertow.{Handlers, Undertow}
import org.apache.lucene.search.Query
import org.infinispan.Cache
import org.infinispan.manager.DefaultCacheManager

/**
 * Created by viddu on 9/26/15.
 */
class UndertowContainer(val cache: Cache[Query, Campaign] = new DefaultCacheManager().getCache[Query, Campaign](),
                        val port: Integer = 8080,
                        val host: String = "localhost",
                        val contextRoot: String = "/sengi") extends LazyLogging with AutoCloseable {


  val servletBuilder: DeploymentInfo = Servlets.deployment()
    .setClassLoader(this.getClass.getClassLoader())
    .setContextPath("/sengi")
    .setDeploymentName("sengi")
    .addServlet(Servlets.servlet("echoSeed", classOf[EchoSeedServlet]).addMapping("/*"))
    // Assigns a UUID seed if none present
    .addFilter(Servlets.filter("seedFilter", classOf[SeedFilter])).addFilterUrlMapping("seedFilter", "/*", DispatcherType.REQUEST)
    // Builds an index out of the request
    .addFilter(Servlets.filter("indexFilter", classOf[RequestIndexingFilter])).addFilterUrlMapping("indexFilter", "/*", DispatcherType.REQUEST)
    // Selects queries that satisfy the index built in previous step.
    .addFilter(Servlets.filter("targetSelecte", classOf[TargetSelectionFilter], new TargetSelectionFilter(cache)))
    .addFilter(Servlets.filter("campaignSelector", classOf[CampaignSelectionFilter], new CampaignSelectionFilter())).addFilterUrlMapping("campaignSelector", "/*", DispatcherType.REQUEST)

  val manager: DeploymentManager = Servlets.defaultContainer().addDeployment(servletBuilder)
  manager.deploy()

  val path: PathHandler = Handlers.path().addPrefixPath(contextRoot, manager.start())

  val server = Undertow.builder()
    .addHttpListener(port, host)
    .setHandler(path)
    .build()

  def start(): Unit = {
    logger.debug("Starting undertow container..")
    server.start
  }

  override def close(): Unit = {
    logger.debug("Stopping undertow..")
    if (server != null) server.stop()
  }

  implicit def func2InstanceFactory[T <: Filter](f: => T): InstanceFactory[T] = {
    new InstanceFactory[T] {
      override def createInstance(): InstanceHandle[T] = {
        new InstanceHandle[T] {
          override def getInstance(): T = {
            f
          }

          override def release(): Unit = {}
        }
      }
    }
  }
}
