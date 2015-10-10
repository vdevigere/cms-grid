package com.github.sengi

import com.github.sengi.undertow.UndertowContainer
import com.typesafe.scalalogging.LazyLogging

/**
 * Created by viddu on 9/25/15.
 */
object StartServer extends App with LazyLogging {
  logger.debug("Starting server..")
  val container = new UndertowContainer()
  container.start()

  Runtime.getRuntime.addShutdownHook(new Thread() {
    override def run(): Unit = {
      container.close()
    }
  })
}
