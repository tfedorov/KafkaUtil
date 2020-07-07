package com.tfedorov

import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.atomic.AtomicInteger

import org.apache.kafka.clients.producer.RecordMetadata

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}
import scala.util.Random


object ProducerApp extends App {
  implicit val ec: ExecutionContextExecutor = ExecutionContext.global

  private val format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S")

  private def randomStr = Random.nextPrintableChar //Random.nextString(Random.nextInt(5))
  private def time: String = format.format(new Date())

  val counted = new AtomicInteger(0)
  private def count: Int = counted.addAndGet(1)

  private def shotMessage(producerWrapper: ProducerWrapper[String, String]): Future[RecordMetadata] = {
    producerWrapper.writeAsync("topic4test", s"key" + randomStr, randomStr + s"value-$time")
  }

  private def shotMessageSync(producerWrapper: ProducerWrapper[String, String]): RecordMetadata = {
    producerWrapper.writeSync("topic4test", s"key" + randomStr, count + s"value-$time").get()
  }

  def printRecordMetadata = {
    r: RecordMetadata =>
      println(
        "topic=" + r.topic() +
          " ; partition=" + r.partition() +
          " ; offset=" + r.offset() +
          " ; timestamp=" + format.format(r.timestamp())
        //" ; serializedKeySize=" + r.serializedKeySize() +
        //" ; serializedValueSize=" + r.serializedValueSize() +
        //" ; timestamp=" + r.timestamp() +
      )
  }

  private val producerWrapper: ProducerWrapper[String, String] = ProducerInit.createPWrapper()

  val results = (1 to 100).map(_ => shotMessageSync(producerWrapper))
  // val futureRes: immutable.Seq[Future[RecordMetadata]] = (1 to 100).map(_ => shotMessage(producerWrapper))

  Thread.sleep(5000)

  results.foreach(printRecordMetadata)

  //futureRes.foreach { fr: Future[RecordMetadata] => fr.foreach(printRecordMetadata) }
  //futureRes.filter(!_.isCompleted).foreach(println)

  producerWrapper.close()
}
