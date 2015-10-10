package com.github.sengi.servlets

import java.util.ServiceLoader
import javax.servlet._
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import com.github.sengi.api.{RequestTokenizer, SengiConstants}
import com.typesafe.scalalogging.LazyLogging
import org.apache.lucene.index.memory.MemoryIndex

import scala.collection.JavaConversions._

/**
 * Created by Viddu on 10/5/2015.
 */
class RequestIndexingFilter extends Filter with LazyLogging {
  var serviceLoader: List[RequestTokenizer] = Nil

  override def init(filterConfig: FilterConfig): Unit = {
    logger.debug("Initializing IndexingFilter")
    val srvLoader: ServiceLoader[RequestTokenizer] = ServiceLoader.load(classOf[RequestTokenizer])
    serviceLoader = srvLoader.iterator().toList
    logger.debug(s"Found $serviceLoader ServiceLoaders")
  }

  override def doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain): Unit = {
    val memoryIndex: MemoryIndex = new MemoryIndex()
    val req = request.asInstanceOf[HttpServletRequest]
    val res = response.asInstanceOf[HttpServletResponse]
    serviceLoader.par.foreach(tokenizer => {
      val fieldPair = tokenizer.tokenize(req)
      logger.debug(s"Adding Field: $fieldPair")
      memoryIndex.addField(fieldPair._1, memoryIndex.keywordTokenStream(fieldPair._2))
    })
    req.setAttribute(SengiConstants.INDEX_ATTR, memoryIndex)
    chain.doFilter(req, res)
  }

  override def destroy(): Unit = {}
}
