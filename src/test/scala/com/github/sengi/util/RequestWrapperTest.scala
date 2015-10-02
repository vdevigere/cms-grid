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

  val mockRequest: HttpServletRequest = mock(classOf[HttpServletRequest])

  "looking for key[cookie_param]" should "find the value [cookie_value] in cookie" in {
    when(mockRequest.getCookies).thenReturn(Array(new Cookie("cookie_param", "cookie_value")))
    val result = mockRequest findValue "cookie_param"
    result.get should be("cookie_value")
  }

  "looking for key[header_param]" should "not find it in cookies and continue on to find value[header_val] in headers" in {
    when(mockRequest.getHeader("header_param")).thenReturn("header_val")
    val result = mockRequest findValue "header_param"
    result.get should be("header_val")
  }
}
