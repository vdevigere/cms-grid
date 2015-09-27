package com.github.sengi.data

import com.typesafe.scalalogging.LazyLogging
import org.infinispan.Cache
import org.infinispan.configuration.cache.{CacheMode, ConfigurationBuilder}
import org.infinispan.configuration.global.GlobalConfigurationBuilder
import org.infinispan.manager.DefaultCacheManager

/**
 * Created by viddu on 9/25/15.
 */
object DataNode extends AutoCloseable with LazyLogging {
  logger.debug("Starting data node..")
  val cacheManager = new DefaultCacheManager(GlobalConfigurationBuilder.defaultClusteredBuilder()
    .transport()
    .nodeName("sengi")
    //.addProperty("configurationFile", "jgroups.xml")
    .build(),
    new ConfigurationBuilder()
      .clustering()
      .cacheMode(CacheMode.REPL_SYNC)
      .build())

  cacheManager.defineConfiguration("dist", new ConfigurationBuilder()
    .clustering()
    .cacheMode(CacheMode.DIST_SYNC)
    .hash().numOwners(2)
    .build())

  def start(cacheName: String): Cache[String, String] = {
    val cache: Cache[String, String] = cacheManager.getCache(cacheName)
    logger.debug("Cache {} started on {}, cache members are now {}", cacheName, cacheManager.getAddress(),
      cache.getAdvancedCache().getRpcManager().getMembers());
    cache.addListener(LoggingListener)
    cache
  }

  override def close(): Unit = {
    logger.debug("Stopping data node..")
    if (cacheManager != null) cacheManager.stop()
  }
}
