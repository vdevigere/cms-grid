package com.github.sengi.servlets

import javax.servlet._
import javax.servlet.http.HttpServletRequest

import com.github.sengi.api.{Campaign, SengiConstants}
import org.apache.lucene.index.memory.MemoryIndex
import org.apache.lucene.search.Query
import org.infinispan.Cache

import scala.collection.JavaConversions._

/**
 * Created by Viddu on 10/10/2015.
 */
class TargetSelectionFilter extends Filter {
  var cache: Cache[Query, Campaign] = null

  override def init(filterConfig: FilterConfig): Unit = {
    cache = filterConfig.getServletContext.getAttribute(SengiConstants.CAMPAIGN_CACHE_ATTR).asInstanceOf[Cache[Query, Campaign]]
  }

  override def doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain): Unit = {
    val req = request.asInstanceOf[HttpServletRequest]
    val index = req.getAttribute(SengiConstants.INDEX_ATTR).asInstanceOf[MemoryIndex]
    val matchedTargets = if (!cache.isEmpty) cache.keySet().filter(query => index.search(query) > 0)
    chain.doFilter(request, response)
  }

  override def destroy(): Unit = {}
}