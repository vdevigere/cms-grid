package com.github.sengi.tokenizer

import java.util.Date
import javax.servlet.http.HttpServletRequest

import com.github.sengi.BaseSpec
import com.typesafe.scalalogging.LazyLogging
import org.apache.lucene.document.DateTools
import org.mockito.Mockito._

/**
 * Created by Viddu on 10/10/2015.
 */
class TimeStampTokenizerSpec extends BaseSpec with LazyLogging {
  val timeStampTokenizer = new TimeStampTokenizer()
  val mockRequest = mock(classOf[HttpServletRequest])

  "tokenize " should "return field name as 'timeStamp' and current date as YYYMMDD as token" in {
    val resultTuple = timeStampTokenizer.tokenize(mockRequest)
    resultTuple._1 should equal("timeStamp")
    val currentDate = Seq(DateTools.dateToString(new Date, DateTools.Resolution.DAY))
    resultTuple._2 should equal(currentDate)
  }

}
