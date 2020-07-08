package com.tfedorov.util

import java.text.SimpleDateFormat
import java.util.Date


object FormatDate {

  private val format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S")

  def nowFormatted(): String = timeFormatted(new Date().getTime)

  def timeFormatted(timeStamp: Long): String = format.format(timeStamp)
}
