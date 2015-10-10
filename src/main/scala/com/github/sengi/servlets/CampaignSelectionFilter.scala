package com.github.sengi.servlets

import javax.servlet._

import com.typesafe.scalalogging.LazyLogging

/**
 * Created by Viddu on 10/2/2015.
 */
class CampaignSelectionFilter extends Filter with LazyLogging {
  override def init(filterConfig: FilterConfig): Unit = {
    logger.debug("Initializing CampaignSelectionFilter")
  }

  override def destroy(): Unit = {

  }

  override def doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain): Unit = {
    chain.doFilter(request, response)
  }
}
