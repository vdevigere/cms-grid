package com.github.sengi.util

import javax.servlet.http.{Cookie, HttpServletRequest}

/**
 * Created by viddu on 9/26/15.
 */

class RequestWrapper(request: HttpServletRequest) {
  def getCookie(name: String): Option[Cookie] = {
    if (request.getCookies != null) request.getCookies.find(_.getName.equalsIgnoreCase(name)) else Option.empty
  }
}

object RequestWrapper {

  implicit def wrap(request: HttpServletRequest): RequestWrapper = new RequestWrapper(request)

}
