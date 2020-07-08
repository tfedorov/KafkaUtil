package com.tfedorov.producer


import com.tfedorov.props.PropertiesCreator
import org.apache.kafka.clients.producer.{Callback, KafkaProducer, ProducerRecord, RecordMetadata}

import scala.concurrent.{ExecutionContextExecutor, Future, Promise}


class ProducerWrapper[K, V](producer: KafkaProducer[K, V], topic: String)(implicit ec: ExecutionContextExecutor) {

  def sendFireAndForget(key: K, value: V): Unit = {
    val record = new ProducerRecord[K, V](topic, key, value)
    producer.send(record)
  }

  def sendSync(key: K, value: V): RecordMetadata = {
    val record = new ProducerRecord[K, V](topic, key, value)
    producer.send(record).get()
  }


  def sendAsync(key: K, value: V): Future[RecordMetadata] = {
    val promise: Promise[RecordMetadata] = Promise[RecordMetadata]()

    val record = new ProducerRecord[K, V](topic, key, value)
    producer.send(record, promisedCallBack(promise))
    promise.future
  }

  private def promisedCallBack(promise: Promise[RecordMetadata]): Callback = {
    (metadata: RecordMetadata, exception: Exception) => {
      if (exception == null)
        promise.success(metadata)
      else
        promise.failure(exception)
    }

  }

  def close(): Unit = producer.close()
}

object ProducerWrapper {

  def default(topic: String)(implicit ec: ExecutionContextExecutor): ProducerWrapper[String, String] = {
    val producer = new KafkaProducer[String, String](PropertiesCreator.create())
    new ProducerWrapper(producer, topic)
  }
}