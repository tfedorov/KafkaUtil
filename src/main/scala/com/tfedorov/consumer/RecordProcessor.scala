package com.tfedorov.consumer

import org.apache.kafka.clients.consumer.ConsumerRecords

trait RecordProcessor[K, V] {

  def process(records: ConsumerRecords[K, V]): Unit
}

class ConsoleRecordProcessor extends RecordProcessor[String, String] {

  override def process(records: ConsumerRecords[String, String]): Unit = records.forEach(println)

}
