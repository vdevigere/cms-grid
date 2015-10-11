package com.github.sengi.servlets

import java.util
import javax.servlet.{DispatcherType, ServletContextEvent, ServletContextListener}

import com.github.sengi.api.SengiConstants._
import com.github.sengi.data.DataNode

/**
 * Created by Viddu on 10/11/2015.
 */
class SengiServletListener extends ServletContextListener {

  override def contextDestroyed(sce: ServletContextEvent): Unit = {

  }

  override def contextInitialized(sce: ServletContextEvent): Unit = {
    val cache = DataNode.start("CAMPAIGN_CACHE")
    val context = sce.getServletContext
    context.setAttribute(CAMPAIGN_CACHE_ATTR, cache)
    context.addServlet("echoSeed", classOf[EchoSeedServlet]).addMapping("/*")
    // Assigns a UUID seed if none present
    context.addFilter("seedFilter", classOf[SeedFilter]).addMappingForUrlPatterns(util.EnumSet.of(DispatcherType.REQUEST), false, "/*")
    // Builds an index out of the request
    context.addFilter("indexFilter", classOf[RequestIndexingFilter]).addMappingForUrlPatterns(util.EnumSet.of(DispatcherType.REQUEST), false, "/*")
    // Selects targets that satisfy the index built in previous step.
    context.addFilter("targetSelectionFilter", classOf[TargetSelectionFilter]).addMappingForUrlPatterns(util.EnumSet.of(DispatcherType.REQUEST), false, "/*")
    // Selects campaigns that satisfy the target
    context.addFilter("campaignSelector", classOf[CampaignSelectionFilter]).addMappingForUrlPatterns(util.EnumSet.of(DispatcherType.REQUEST), false, "/*")
  }
}
