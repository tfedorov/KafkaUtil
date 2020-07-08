package com.tfedorov.consumer

import java.time.Duration

import com.tfedorov.props.PropertiesCreator
import org.apache.kafka.clients.consumer.KafkaConsumer

import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContextExecutor

case class ConsumerWrapper[K, V](consumer: KafkaConsumer[K, V], topic: String) {

  def infiniteListen(recordProcessor: RecordProcessor[K, V]): Unit =
    while (true) {
      val polledRecords = consumer.poll(Duration.ofMillis(1000))
      recordProcessor.process(polledRecords)
    }
}


object ConsumerWrapper {

  def default(topic: String)(implicit ec: ExecutionContextExecutor): ConsumerWrapper[String, String] = {
    val consumer = new KafkaConsumer[String, String](PropertiesCreator.create())
    consumer.subscribe((topic :: Nil).asJava)
    new ConsumerWrapper[String, String](consumer, topic)
  }
}
