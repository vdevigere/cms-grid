package com.github.sengi.api

import javax.servlet.http.HttpServletRequest

/**
 * Created by Viddu on 10/5/2015.
 */
trait RequestTokenizer {
  val field: String

  def tokenize(req: HttpServletRequest): Tuple2[String, Seq[String]] = {
    new Tuple2(field, generateTokens(req))
  }

  def generateTokens(req: HttpServletRequest): Seq[String]
}
