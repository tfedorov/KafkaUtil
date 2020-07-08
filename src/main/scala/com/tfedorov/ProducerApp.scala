package com.tfedorov

import com.tfedorov.message.MessageGenerator
import com.tfedorov.producer.{RecordMetadataPrinter, ProducerWrapper}
import org.apache.kafka.clients.producer.RecordMetadata

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}

object ProducerApp extends App {
  implicit val ec: ExecutionContextExecutor = ExecutionContext.global

  private def shotMessage(producerWrapper: ProducerWrapper[String, String]): Future[RecordMetadata] = {
    val rand = MessageGenerator.generateRandom()
    producerWrapper.sendAsync(rand.key, rand.value)
  }

  private def shotMessageSync(producerWrapper: ProducerWrapper[String, String]): RecordMetadata = {
    val message = MessageGenerator.generateConsistent()
    producerWrapper.sendSync(message.key, message.value)
  }


  private val producerWrapper: ProducerWrapper[String, String] = ProducerWrapper.default("topic4test")

  val resultsSync: Seq[RecordMetadata] = (1 to 100).map(_ => shotMessageSync(producerWrapper))

  Thread.sleep(5000)

  resultsSync.foreach(RecordMetadataPrinter.printConsole)

  // val futureRes: immutable.Seq[Future[RecordMetadata]] = (1 to 100).map(_ => shotMessage(producerWrapper))
  //futureRes.foreach { fr: Future[RecordMetadata] => fr.foreach(printRecordMetadata) }
  //futureRes.filter(!_.isCompleted).foreach(Printer.printRecordMetadata)

  producerWrapper.close()
}
