package com.tfedorov.consumer

import java.time.Duration
import java.util

import com.tfedorov.Logging
import com.tfedorov.consumer.ConsumerWrapper.PrintErrorCallBack
import com.tfedorov.props.PropertiesUtils
import org.apache.kafka.clients.consumer.{ConsumerRecords, KafkaConsumer, OffsetAndMetadata, OffsetCommitCallback}
import org.apache.kafka.common.TopicPartition

import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContextExecutor

case class ConsumerWrapper[K, V](consumer: KafkaConsumer[K, V], topic: String) extends  Logging {

  private final val DEF_DURATION: Duration = Duration.ofMillis(1000)

  def autoCommitPoll(recordProcessor: RecordsProcessor[K, V]): Unit = {
    val polledRecords: ConsumerRecords[K, V] = consumer.poll(DEF_DURATION)
    recordProcessor.process(polledRecords)
  }

  def syncPoll(recordProcessor: RecordsProcessor[K, V]): Unit = {
    val polledRecords: ConsumerRecords[K, V] = consumer.poll(DEF_DURATION)
    recordProcessor.process(polledRecords)
    try {
      consumer.commitSync()
    } catch {
      case e: Throwable => error(e)
    }
  }

  def asyncPoll(recordProcessor: RecordsProcessor[K, V]): Unit = {
    val polledRecords: ConsumerRecords[K, V] = consumer.poll(DEF_DURATION)
    recordProcessor.process(polledRecords)
    try {
      consumer.commitAsync(new PrintErrorCallBack())
    } catch {
      case e: Throwable => error(e)
    }
  }

}


object ConsumerWrapper extends Logging {

  def default(topic: String): ConsumerWrapper[String, String] = {
    val consumer = new KafkaConsumer[String, String](PropertiesUtils.defaultProps())
    consumer.subscribe((topic :: Nil).asJava)
    new ConsumerWrapper[String, String](consumer, topic)
  }

  private class PrintErrorCallBack extends OffsetCommitCallback {
    override def onComplete(offsets: util.Map[TopicPartition, OffsetAndMetadata], exception: Exception): Unit =
      if (exception != null)
        error(exception)

  }

}
