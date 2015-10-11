package com.github.sengi.data

import com.typesafe.scalalogging.LazyLogging
import org.infinispan.Cache
import org.infinispan.manager.DefaultCacheManager

/**
 * Created by viddu on 9/25/15.
 */
object DataNode extends AutoCloseable with LazyLogging {
  logger.debug("Starting data node..")
  val cacheManager = new DefaultCacheManager()

  def start(cacheName: String = null): Cache[String, Any] = {
    val cache: Cache[String, Any] = if (cacheName == null) cacheManager.getCache[String, Any] else cacheManager.getCache(cacheName)
    cache.addListener(LoggingListener)
    cache
  }

  override def close(): Unit = {
    logger.debug("Stopping data node..")
    if (cacheManager != null) cacheManager.stop()
  }
}
