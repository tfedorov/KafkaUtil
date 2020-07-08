package com.tfedorov.producer

import com.tfedorov.util.FormatDate.timeFormatted
import org.apache.kafka.clients.producer.RecordMetadata

object Printer {
  def printRecordMetadata(r: RecordMetadata): Unit = {
    println(
      "topic=" + r.topic() +
        " ; partition=" + r.partition() +
        " ; offset=" + r.offset() +
        " ; timestamp=" + timeFormatted(r.timestamp())
      //" ; serializedKeySize=" + r.serializedKeySize() +
      //" ; serializedValueSize=" + r.serializedValueSize() +
      //" ; timestamp=" + r.timestamp() +
    )
  }
}
