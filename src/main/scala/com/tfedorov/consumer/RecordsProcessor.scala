package com.tfedorov.consumer

import org.apache.kafka.clients.consumer.ConsumerRecords

trait RecordsProcessor[K, V] {

  def process(records: ConsumerRecords[K, V]): Unit
}

class ConsoleRecordsProcessor extends RecordsProcessor[String, String] {

  override def process(records: ConsumerRecords[String, String]): Unit = records.forEach(println)

}
