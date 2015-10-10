package com.github.sengi.tokenizer

import java.util.Date
import javax.servlet.http.HttpServletRequest

import com.github.sengi.api.RequestTokenizer
import org.apache.lucene.document.DateTools

/**
 * Created by Viddu on 10/5/2015.
 */
class TimeStampTokenizer extends RequestTokenizer {
  override val field: String = "timeStamp"

  override def generateTokens(req: HttpServletRequest): Seq[String] = Seq(DateTools.dateToString(new Date, DateTools.Resolution.DAY))

}
