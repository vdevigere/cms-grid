package com.github.sengi.servlets

import javax.servlet._
import javax.servlet.http.{Cookie, HttpServletRequest, HttpServletResponse}

import com.github.sengi.api.SengiConstants._
import com.github.sengi.util.RequestWrapper._
import com.github.sengi.util.UUIDGenarator
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
    val seedCookieValue = httpRequest.findValue(SEED_COOKIE_NAME).getOrElse(UUIDGenarator.generate.toString)
    httpRequest.setAttribute(SEED_COOKIE_NAME, seedCookieValue)
    val seedCookie: Cookie = getSeedCookie(seedCookieValue)
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

  def getSeedCookie(seedCookieValue: String): Cookie = {
    val seedCookie = new Cookie(SEED_COOKIE_NAME, seedCookieValue)
    seedCookie.setMaxAge(THIRTY_DAYS_IN_SECONDS)
    seedCookie.setPath("/")
    seedCookie.setSecure(false)
    seedCookie.setComment("Sengi Session ID")
    seedCookie
  }
}
