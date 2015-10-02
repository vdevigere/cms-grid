package com.github.sengi.util

import javax.servlet.http.{Cookie, HttpServletRequest}
import RequestWrapper._
import com.github.sengi.BaseSpec
import org.mockito.Mockito._

import org.scalatest.{Matchers, FlatSpec}

/**
 * Created by Viddu on 10/1/2015.
 */
class RequestWrapperTest extends BaseSpec{


  "looking for key[cookie_param]" should "find the value [cookie_value] in cookie" in {
    val mockRequest: HttpServletRequest = mock(classOf[HttpServletRequest])
    when(mockRequest.getCookies).thenReturn(Array(new Cookie("cookie_param", "cookie_value")))
    val result = mockRequest findValue "cookie_param"
    result.get should be("cookie_value")
    verify(mockRequest).getCookies()
  }

  "looking for key[header_param]" should "not find it in cookies and continue on to find value[header_val] in headers" in {
    val mockRequest: HttpServletRequest = mock(classOf[HttpServletRequest])
    when(mockRequest.getHeader("header_param")).thenReturn("header_val")
    val result = mockRequest findValue "header_param"
    result.get should be("header_val")
    verify(mockRequest).getCookies()
    verify(mockRequest).getHeader("header_param")
  }

  "looking for key[attr_param]" should "not find it in cookies and header but continue on to find value[attr_val] in attributes" in {
    val mockRequest: HttpServletRequest = mock(classOf[HttpServletRequest])
    val result = mockRequest findValue "attr_param"
    result.isEmpty should be(true)
    verify(mockRequest).getCookies()
    verify(mockRequest).getHeader("attr_param")
    verify(mockRequest).getAttribute("attr_param")
  }
}
