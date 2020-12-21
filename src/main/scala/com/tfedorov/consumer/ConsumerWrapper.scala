package com.tfedorov.consumer

import com.tfedorov.Logging
import com.tfedorov.message.Message
import org.apache.kafka.clients.consumer._
import org.apache.kafka.common.TopicPartition

import java.time.Duration
import java.util
import java.util.Properties
import scala.collection.JavaConverters._

case class ConsumerWrapper[K, V](consumer: KafkaConsumer[K, V], topic: String) extends Logging {

  private final val DEF_DURATION: Duration = Duration.ofMillis(1000)

  def readFromBeginning(recordF: (K, V) => Unit): Unit = {
    consumer.seekToBeginning(consumer.assignment())
    messageIterator().foreach(c => recordF(c.key(), c.value()))

    consumer.seekToBeginning(consumer.assignment())
    messageIterator().foreach(c => recordF(c.key(), c.value()))
  }

  def autoCommitPoll(recordF: (K, V) => Unit): Unit = {
    messageIterator().foreach(c => recordF(c.key(), c.value()))
  }

  def syncPoll(recordF: (K, V) => Unit): Unit = {
    try {
      messageIterator().foreach(c => recordF(c.key(), c.value()))
      consumer.commitSync()
    }
    catch {
      case e: Throwable => error(e)
    }
  }

  def asyncPoll(recordF: (K, V) => Unit): Unit = {
    try {
      messageIterator().foreach(c => recordF(c.key(), c.value()))
      consumer.commitAsync(new OffsetCommitCallback() {
        override def onComplete(offsets: util.Map[TopicPartition, OffsetAndMetadata], exception: Exception): Unit =
          if (exception != null)
            error(exception)
      })
    }
    catch {
      case e: Throwable => error(e)
    }
  }


  private def messageIterator(): Iterator[ConsumerRecord[K, V]] = {
    val polledRecords: ConsumerRecords[K, V] = consumer.poll(DEF_DURATION)
    polledRecords.iterator().asScala
  }
}

object ConsumerWrapper extends Logging {

  def create[K, V, M <: Message[K, V]](topic: String, properties: Properties): ConsumerWrapper[K, V] = {
    val consumer = new KafkaConsumer[K, V](properties)
    consumer.subscribe((topic :: Nil).asJava)
    new ConsumerWrapper[K, V](consumer, topic)
  }

}
