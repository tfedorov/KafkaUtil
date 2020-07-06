package com.tfedorov

import java.util.concurrent.Future

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord, RecordMetadata}

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}


class ProducerWrapper[K, V](producer: KafkaProducer[K, V]) {

  def writeSync(topic: String, key: K, value: V): Future[RecordMetadata] = {
    val record = new ProducerRecord[K, V](topic, key, value)
    producer.send(record)
  }

  implicit val ec: ExecutionContextExecutor = ExecutionContext.global

  def writeAsync(topic: String, key: K, value: V): concurrent.Future[RecordMetadata] = {
    scala.concurrent.Future {
      writeSync(topic, key, value).get()
    }
  }

  def close(): Unit = producer.close()
}
