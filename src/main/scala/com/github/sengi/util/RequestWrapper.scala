package com.github.sengi.util

import javax.servlet.http.{Cookie, HttpServletRequest}

import com.typesafe.scalalogging.LazyLogging

import scala.Option
import scala.annotation.tailrec

/**
 * Created by viddu on 9/26/15.
 */

class RequestWrapper(request: HttpServletRequest) extends LazyLogging {

  import RequestWrapper._

  def getCookie(name: String): Option[Cookie] = {
    val allCookies = request.getCookies
    if (allCookies != null) allCookies.find(_.getName.equalsIgnoreCase(name)) else None
  }

  def findValueInCookie: HandlerFunc = (req, name) => {
    logger.debug("finding in cookies..")
    req.getCookie(name) match {
      case opt: Some[Cookie] => Option(opt.get.getValue)
      case None => None
    }
  }

  def findValueInHeader: HandlerFunc = (req, name) => {
    logger.debug("finding in headers..")
    val headerValue = req.getHeader(name)
    if (headerValue != null) Option(headerValue) else None
  }

  def findValueInAttributes: HandlerFunc = (req, name) => {
    logger.debug("finding in attributes..")
    val attrVal = req.getAttribute(name)
    if (attrVal != null) Option(attrVal.asInstanceOf[String]) else None
  }

  def findValue(name: String): Option[String] = {
    findValueInCookie :: findValueInHeader :: findValueInAttributes :: Nil apply(request, name)
  }

}

object RequestWrapper {

  type HandlerFunc = (HttpServletRequest, String) => Option[String]

  @tailrec
  def handleList(request: HttpServletRequest, name: String, handlers: List[HandlerFunc]): Option[String] = {
    if (handlers.isEmpty) None
    else handlers.head(request, name) match {
      case value: Some[String] => value
      case None => handleList(request, name, handlers.tail)
    }
  }

  implicit def listToHandlerFunction(handlers: List[HandlerFunc]): HandlerFunc = (request, name) => handleList(request, name, handlers)


  implicit def wrap(request: HttpServletRequest): RequestWrapper = new RequestWrapper(request)

}
