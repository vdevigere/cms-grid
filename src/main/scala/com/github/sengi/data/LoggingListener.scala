package com.github.sengi.data

import com.typesafe.scalalogging.LazyLogging
import org.infinispan.notifications.Listener
import org.infinispan.notifications.cachelistener.annotation.{CacheEntryCreated, CacheEntryModified, CacheEntryRemoved, TopologyChanged}
import org.infinispan.notifications.cachelistener.event.{CacheEntryCreatedEvent, CacheEntryModifiedEvent, CacheEntryRemovedEvent, TopologyChangedEvent}

/**
 * Created by viddu on 9/26/15.
 */
@Listener
object LoggingListener extends LazyLogging {


  @CacheEntryCreated
  def observeAdd(event: CacheEntryCreatedEvent[String, String]): Unit = {
    if (event.isPre()) return

    logger.debug("Cache entry [{},{}] added in cache {}", event.getKey(), event.getValue, event.getCache())
  }

  @CacheEntryModified
  def observeUpdate(event: CacheEntryModifiedEvent[String, String]) {
    if (event.isPre())
      return

    logger.debug("Cache entry {} = {} modified in cache {}", event.getKey(), event.getValue(), event.getCache())
  }

  @CacheEntryRemoved
  def observeRemove(event: CacheEntryRemovedEvent[String, String]) {
    if (event.isPre())
      return

    logger.debug("Cache entry {} removed in cache {}", event.getKey(), event.getCache())
  }

  @TopologyChanged
  def observeTopologyChange(event: TopologyChangedEvent[String, String]) {
    if (event.isPre())
      return

    logger.debug("Cache {} topology changed, new membership is {}", event.getCache().getName(), event.getConsistentHashAtEnd().getMembers())
  }
}