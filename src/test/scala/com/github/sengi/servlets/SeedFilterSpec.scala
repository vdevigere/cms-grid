package com.github.sengi.servlets

import javax.servlet.FilterChain
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import com.github.sengi.BaseSpec
import com.github.sengi.api.SengiConstants
import com.github.sengi.util.RequestWrapper._
import com.typesafe.scalalogging.LazyLogging
import org.mockito.Mockito._


/**
 * Created by Viddu on 10/11/2015.
 */
class SeedFilterSpec extends BaseSpec with LazyLogging {

  "getSeedCookie" should "construct a 'SEED' cookie valid for 30 days on the root path" in {
    val seedCookie = SeedFilter.getSeedCookie("blah")
    seedCookie.getPath should equal("/")
    seedCookie.getName should equal(SengiConstants.SEED_COOKIE_NAME)
    seedCookie.getValue should equal("blah")
    seedCookie.getMaxAge should equal(30 * 24 * 3600)
  }

  "doFilter" should "assign a new randomly generated seed if none is present in [Cookie, Attribute, Header]" in {
    val mockRequest = mock(classOf[HttpServletRequest])
    val mockResponse = mock(classOf[HttpServletResponse])
    val mockFilterChain = mock(classOf[FilterChain])
    val seedFilter = new SeedFilter
    seedFilter.init(null)
    seedFilter.doFilter(mockRequest, mockResponse, mockFilterChain)
    verify(mockRequest).findValue(SengiConstants.SEED_COOKIE_NAME)
    verify(mockFilterChain).doFilter(mockRequest, mockResponse)
  }

  "doFilter" should "assign the current value if seed already exists" in {
    val mockRequest = mock(classOf[HttpServletRequest])
    val mockResponse = mock(classOf[HttpServletResponse])
    val mockFilterChain = mock(classOf[FilterChain])
    when(mockRequest.getHeader(SengiConstants.SEED_COOKIE_NAME)).thenReturn("blah")
    val seedFilter = new SeedFilter
    seedFilter.init(null)
    seedFilter.doFilter(mockRequest, mockResponse, mockFilterChain)
    verify(mockRequest).findValue(SengiConstants.SEED_COOKIE_NAME)
    verify(mockRequest).setAttribute(SengiConstants.SEED_COOKIE_NAME, "blah")
    verify(mockFilterChain).doFilter(mockRequest, mockResponse)
  }
}
