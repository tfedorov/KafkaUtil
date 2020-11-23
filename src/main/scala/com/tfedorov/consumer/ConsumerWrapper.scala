package com.tfedorov.consumer

import java.time.Duration
import java.util
import java.util.Properties

import com.tfedorov.Logging
import com.tfedorov.consumer.ConsumerWrapper.PrintErrorCallBack
import org.apache.kafka.clients.consumer._
import org.apache.kafka.common.TopicPartition

import scala.collection.JavaConverters._

case class ConsumerWrapper[K, V](consumer: KafkaConsumer[K, V], topic: String) extends  Logging {

  private final val DEF_DURATION: Duration = Duration.ofMillis(1000)

  def autoCommitPoll(recordF: (K, V) => Unit): Unit = {
    val polledRecords: ConsumerRecords[K, V] = consumer.poll(DEF_DURATION)
    polledRecords.iterator().asScala.foreach(_.applyRecordF(recordF))
  }


  def asyncPoll(recordF: (K, V) => Unit): Unit = {
    autoCommitPoll(recordF)
    try {
      consumer.commitAsync(new PrintErrorCallBack())
    }
    catch {
      case e: Throwable => error(e)
    }
  }

  private implicit def consumerRecord2Processor(keyValF: ConsumerRecord[K, V]): ConsumerRecordsProcessor = {
    new ConsumerRecordsProcessor(keyValF)
  }

  private class ConsumerRecordsProcessor(consRecord: ConsumerRecord[K, V]) {
    def applyRecordF(recordF: (K, V) => Unit): Unit = recordF(consRecord.key(), consRecord.value())
  }

  def createPrintF: (K, V) => Unit = (key: K, value: V) => println(s"key=$key, value=$value")
}

object ConsumerWrapper extends Logging {

  def create(topic: String, properties: Properties): ConsumerWrapper[String, String] = {
    val consumer = new KafkaConsumer[String, String](properties)
    consumer.subscribe((topic :: Nil).asJava)
    new ConsumerWrapper[String, String](consumer, topic)
  }

  private class PrintErrorCallBack extends OffsetCommitCallback {
    override def onComplete(offsets: util.Map[TopicPartition, OffsetAndMetadata], exception: Exception): Unit =
      if (exception != null)
        error(exception)
  }

}
