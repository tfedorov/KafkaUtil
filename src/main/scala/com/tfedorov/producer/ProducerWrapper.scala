package com.tfedorov.producer

import java.util.concurrent.Future

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord, RecordMetadata}

import scala.concurrent.ExecutionContextExecutor


class ProducerWrapper[K, V](producer: KafkaProducer[K, V])(implicit ec: ExecutionContextExecutor) {

  def writeSync(topic: String, key: K, value: V): Future[RecordMetadata] = {
    val record = new ProducerRecord[K, V](topic, key, value)
    producer.send(record)
  }


  def writeAsync(topic: String, key: K, value: V): concurrent.Future[RecordMetadata] = {
    scala.concurrent.Future {
      writeSync(topic, key, value).get()
    }
  }

  def close(): Unit = producer.close()
}
