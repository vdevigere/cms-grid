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
    if (request.getCookies != null) request.getCookies.find(_.getName.equalsIgnoreCase(name)) else Option.empty
  }

  def findValueInCookie: handlerFunc = (req, name) => {
    logger.debug("finding in cookies..")
    req.getCookie(name) match {
      case opt: Some[Cookie] => Option(opt.get.getValue)
      case None => None
    }
  }

  def findValueInHeader: handlerFunc = (req, name) => {
    logger.debug("finding in headers..")
    val headerValue = req.getHeader(name)
    if (headerValue != null) Option(headerValue) else None
  }

  def findValueInAttributes: handlerFunc = (req, name) => {
    logger.debug("finding in attributes..")
    val attrVal = req.getAttribute(name)
    if (attrVal != null) Option(attrVal.asInstanceOf[String]) else None
  }

  def findValue(name: String): Option[String] = {
    findValueInCookie :: findValueInHeader :: Nil handle(request, name)
  }

}

object RequestWrapper {

  type handlerFunc = (HttpServletRequest, String) => Option[String]

  trait Handler {
    def handle(req: HttpServletRequest, name: String): Option[String]
  }


  implicit def listOfHandlerFuncs(handlers: List[handlerFunc]): Handler = new Handler {
    override def handle(request: HttpServletRequest, name: String): Option[String] = foo(request, name, handlers)

    private def foo(req: HttpServletRequest, name: String, handlers: List[handlerFunc]): Option[String] = {
      if (handlers.isEmpty) Option.empty[String]
      else handlers.head(req, name) match {
        case value: Some[String] => value
        case None => foo(req, name, handlers.tail)
      }
    }
  }

  implicit def wrap(request: HttpServletRequest): RequestWrapper = new RequestWrapper(request)

}
