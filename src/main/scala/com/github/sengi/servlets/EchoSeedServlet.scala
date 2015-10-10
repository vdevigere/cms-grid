package com.github.sengi.servlets

import javax.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}

import com.typesafe.scalalogging.LazyLogging

/**
 * Created by viddu on 9/26/15.
 */
class EchoSeedServlet extends HttpServlet with LazyLogging {
  override def doGet(req: HttpServletRequest, resp: HttpServletResponse): Unit = {
    val writer = resp.getWriter
    val seed = req.getAttribute("SEED")
    val message = s"SEED is $seed"
    logger.debug(message)
    writer.write(message)
    writer.flush()
  }
}
