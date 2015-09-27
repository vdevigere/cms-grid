package com.github.sengi.servlets

import javax.servlet._
import javax.servlet.http.{Cookie, HttpServletRequest, HttpServletResponse}

import com.fasterxml.uuid.Generators
import com.github.sengi.util.RequestWrapper._
import com.typesafe.scalalogging.LazyLogging

/**
 * Created by viddu on 9/26/15.
 */
class SeedFilter extends Filter with LazyLogging {

  import SeedFilter._

  override def init(filterConfig: FilterConfig): Unit = {
    logger.debug("Initializing SEED filter")

  }

  override def doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain): Unit = {
    logger.debug("Setting seed >>")
    val httpRequest = request.asInstanceOf[HttpServletRequest]
    val httpResponse = response.asInstanceOf[HttpServletResponse]
    val seedCookie = httpRequest.getCookie(SEED_COOKIE_NAME).getOrElse(new Cookie(SEED_COOKIE_NAME, generator.generate().toString))
    httpRequest.setAttribute(SEED_COOKIE_NAME, seedCookie.getValue)
    seedCookie.setMaxAge(THIRTY_DAYS_IN_SECONDS)
    seedCookie.setPath("/")
    seedCookie.setSecure(false)
    seedCookie.setComment("Sengi Session ID")
    httpResponse.addCookie(seedCookie)
    logger.debug("<< Sending seed:{}", seedCookie.getValue)
    chain.doFilter(request, response)
  }

  override def destroy(): Unit = {
    logger.debug("Destroying seed filter")
  }
}

object SeedFilter {
  val THIRTY_DAYS_IN_SECONDS: Int = 30 * 24 * 3600
  val SEED_COOKIE_NAME = "SEED"
  val generator = Generators.randomBasedGenerator()
}
