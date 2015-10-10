package com.github.sengi.servlets

import javax.servlet._
import javax.servlet.http.HttpServletRequest

import com.github.sengi.api.{SengiConstants, Campaign}
import org.apache.lucene.index.memory.MemoryIndex
import org.apache.lucene.search.Query
import org.infinispan.Cache

import scala.collection.JavaConversions._

/**
 * Created by Viddu on 10/10/2015.
 */
class TargetSelectionFilter(cache: Cache[Query, Campaign]) extends Filter {

  override def init(filterConfig: FilterConfig): Unit = {}

  override def doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain): Unit = {
    val req = request.asInstanceOf[HttpServletRequest]
    val index = req.getAttribute(SengiConstants.INDEX_ATTR).asInstanceOf[MemoryIndex]

    val matchedTargets = cache.keySet().filter(query => index.search(query) > 0)


  }

  override def destroy(): Unit = {}
}