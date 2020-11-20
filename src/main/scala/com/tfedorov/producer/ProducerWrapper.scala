package com.tfedorov.producer


import java.util.Properties

import com.tfedorov.producer.ProducerWrapper.PromisedCallback

import org.apache.kafka.clients.producer.{Callback, KafkaProducer, ProducerRecord, RecordMetadata}

import scala.concurrent.{ExecutionContextExecutor, Future, Promise}

class ProducerWrapper[K, V](producer: KafkaProducer[K, V], topic: String)(implicit ec: ExecutionContextExecutor) {

  def sendFireAndForget(key: K, value: V): Unit = {
    val record: ProducerRecord[K, V] = new ProducerRecord[K, V](topic, key, value)
    producer.send(record)
  }

  def sendSync(key: K, value: V): RecordMetadata = {
    val record: ProducerRecord[K, V] = new ProducerRecord[K, V](topic, key, value)
    producer.send(record).get()
  }

  def sendAsync(key: K, value: V): Future[RecordMetadata] = {
    val promise: Promise[RecordMetadata] = Promise[RecordMetadata]()
    val record: ProducerRecord[K, V] = new ProducerRecord[K, V](topic, key, value)
    producer.send(record, new PromisedCallback(promise))
    promise.future
  }

  def close(): Unit = producer.close()
}

object ProducerWrapper {

  def create(topic: String, properties: Properties)(implicit ec: ExecutionContextExecutor): ProducerWrapper[String, String] = {
    val producer = new KafkaProducer[String, String](properties)
    new ProducerWrapper(producer, topic)
  }


  private class PromisedCallback(promise: Promise[RecordMetadata]) extends Callback {
    override def onCompletion(metadata: RecordMetadata, exception: Exception): Unit = {
      if (exception == null)
        promise.success(metadata)
      else
        promise.failure(exception)
    }
  }

}