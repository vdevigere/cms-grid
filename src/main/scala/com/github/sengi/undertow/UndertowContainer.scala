package com.github.sengi.undertow

import javax.servlet.DispatcherType

import com.github.sengi.servlets.{EchoSeedServlet, SeedFilter}
import com.typesafe.scalalogging.LazyLogging
import io.undertow.server.handlers.PathHandler
import io.undertow.servlet.Servlets
import io.undertow.servlet.api.{DeploymentInfo, DeploymentManager}
import io.undertow.{Handlers, Undertow}

/**
 * Created by viddu on 9/26/15.
 */
object UndertowContainer extends LazyLogging with AutoCloseable {

  val servletBuilder: DeploymentInfo = Servlets.deployment()
    .setClassLoader(UndertowContainer.getClass.getClassLoader())
    .setContextPath("/sengi")
    .setDeploymentName("sengi")
    .addServlet(Servlets.servlet("echoSeed", classOf[EchoSeedServlet]).addMapping("/*"))
    .addFilter(Servlets.filter("seedFilter", classOf[SeedFilter])).addFilterUrlMapping("seedFilter", "/*", DispatcherType.REQUEST)

  val manager: DeploymentManager = Servlets.defaultContainer().addDeployment(servletBuilder)
  manager.deploy()

  val path: PathHandler = Handlers.path().addPrefixPath("/sengi", manager.start())

  val server = Undertow.builder()
    .addHttpListener(8080, "localhost")
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
}
