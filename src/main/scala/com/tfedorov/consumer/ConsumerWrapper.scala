package com.tfedorov.consumer

import java.time.Duration
import java.util
import java.util.Properties
import com.tfedorov.Logging
import com.tfedorov.consumer.ConsumerWrapper.PrintErrorCallBack
import com.tfedorov.message.Message
import org.apache.kafka.clients.consumer._
import org.apache.kafka.common.TopicPartition

import scala.collection.JavaConverters._

case class ConsumerWrapper[K, V](consumer: KafkaConsumer[K, V], topic: String, emptyMessage: Message[K, V]) extends Logging {

  private final val DEF_DURATION: Duration = Duration.ofMillis(1000)

  def readFromBeginning(recordF: (K, V) => Unit): Unit = {
    consumer.seekToBeginning(consumer.assignment())
    val polledRecords1: ConsumerRecords[K, V] = consumer.poll(Duration.ofMillis(10000))
    polledRecords1.iterator().asScala.foreach(r => recordF(r.key(), r.value()))

    consumer.seekToBeginning(consumer.assignment())
    val polledRecords2: ConsumerRecords[K, V] = consumer.poll(Duration.ofMillis(10000))
    polledRecords2.iterator().asScala.foreach(r => recordF(r.key(), r.value()))
  }

  def readFromBeginning(f: Message[K, V] => (K, V) => Unit): Unit = {
    readFromBeginning(f(emptyMessage))
  }

  def autoCommitPoll(recordF: (K, V) => Unit): Unit = {
    val polledRecords: ConsumerRecords[K, V] = consumer.poll(DEF_DURATION)
    polledRecords.iterator().asScala.foreach(r => recordF(r.key(), r.value()))
  }

  def autoCommitPoll(f: Message[K, V] => (K, V) => Unit): Unit = {
    autoCommitPoll(f(emptyMessage))
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

  def asyncPoll(f: Message[K, V] => (K, V) => Unit): Unit = {
    asyncPoll(f(emptyMessage))
  }
}

object ConsumerWrapper extends Logging {

  def createFromMessage[K, V, M <: Message[K, V]](topic: String, properties: Properties, emptyMessage: Message[K, V]): ConsumerWrapper[K, V] = {
    val consumer = new KafkaConsumer[K, V](properties)
    consumer.subscribe((topic :: Nil).asJava)
    new ConsumerWrapper[K, V](consumer, topic, emptyMessage)
  }

  private class PrintErrorCallBack extends OffsetCommitCallback {
    override def onComplete(offsets: util.Map[TopicPartition, OffsetAndMetadata], exception: Exception): Unit =
      if (exception != null)
        error(exception)
  }

}
