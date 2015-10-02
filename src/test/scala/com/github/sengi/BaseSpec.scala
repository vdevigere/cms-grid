package com.github.sengi

import com.typesafe.scalalogging.LazyLogging
import org.scalatest._

/**
 * Created by Viddu on 10/1/2015.
 */
trait BaseSpec extends FlatSpec with Matchers with OptionValues with Inside with Inspectors with LazyLogging
