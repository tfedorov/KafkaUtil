package com.tfedorov

import com.tfedorov.message.MessageGenerator
import com.tfedorov.producer.{Printer, ProducerInit, ProducerWrapper}
import com.tfedorov.util.FormatDate._
import org.apache.kafka.clients.producer.RecordMetadata

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}

object ProducerApp extends App {
  implicit val ec: ExecutionContextExecutor = ExecutionContext.global

  private def shotMessage(producerWrapper: ProducerWrapper[String, String]): Future[RecordMetadata] = {
    val rand = MessageGenerator.generateRandom()
    producerWrapper.writeAsync("topic4test", rand.key, rand.value)
  }

  private def shotMessageSync(producerWrapper: ProducerWrapper[String, String]): RecordMetadata = {
    val message = MessageGenerator.generateConsistent()
    producerWrapper.writeSync("topic4test", message.key, message.value).get()
  }


  private val producerWrapper: ProducerWrapper[String, String] = ProducerInit.createPWrapper()

  val resultsSync: Seq[RecordMetadata] = (1 to 100).map(_ => shotMessageSync(producerWrapper))

  Thread.sleep(5000)

  resultsSync.foreach(Printer.printRecordMetadata)

  // val futureRes: immutable.Seq[Future[RecordMetadata]] = (1 to 100).map(_ => shotMessage(producerWrapper))
  //futureRes.foreach { fr: Future[RecordMetadata] => fr.foreach(printRecordMetadata) }
  //futureRes.filter(!_.isCompleted).foreach(Printer.printRecordMetadata)

  producerWrapper.close()
}
