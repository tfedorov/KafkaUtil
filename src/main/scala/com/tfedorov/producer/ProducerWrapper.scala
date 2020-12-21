package com.tfedorov.producer


import com.tfedorov.message.{Message, Payment}

import java.util.Properties
import org.apache.kafka.clients.producer.{Callback, KafkaProducer, ProducerRecord, RecordMetadata}

import scala.concurrent.{Future, Promise}

class ProducerWrapper[K, V](producer: KafkaProducer[K, V], topic: String) {

  def sendFireAndForget(key: K, value: V): Unit = {
    val record: ProducerRecord[K, V] = new ProducerRecord[K, V](topic, key, value)
    producer.send(record)
  }

  def sendSync(message: Message[K, V]): RecordMetadata = {
    val record: ProducerRecord[K, V] = new ProducerRecord[K, V](topic, message.key, message.value)
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

  def create[V](topic: String, properties: Properties): ProducerWrapper[String, V] = {
    val producer: KafkaProducer[String, V] = new KafkaProducer[String, V](properties)
    new ProducerWrapper(producer, topic)
  }
}

private class PromisedCallback(promise: Promise[RecordMetadata]) extends Callback {
  override def onCompletion(metadata: RecordMetadata, exception: Exception): Unit = {
    if (exception == null)
      promise.success(metadata)
    else
      promise.failure(exception)
  }


}