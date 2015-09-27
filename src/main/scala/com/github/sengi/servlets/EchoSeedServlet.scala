package com.github.sengi.servlets

import javax.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}

/**
 * Created by viddu on 9/26/15.
 */
class EchoSeedServlet extends HttpServlet {
  override def doGet(req: HttpServletRequest, resp: HttpServletResponse): Unit = {
    val writer = resp.getWriter
    val seed = req.getAttribute("SEED")
    writer.write(s"SEED is $seed")
    writer.flush()
  }
}
