package com.tfedorov


import java.util.Properties

import com.tfedorov.message.MessageGenerator
import com.tfedorov.producer.{ProducerWrapper, RecordMetadataPrinter}
import com.tfedorov.props.PropertiesUtils.{defaultProps, _}
import org.apache.kafka.clients.producer.RecordMetadata

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}

object ProducerApp extends App with Logging {

  info(s"*** STARTED ${this.getClass.getName}****")
  private val bootstrap = "127.0.0.1:9094"
  implicit val props: Properties = defaultProps() + ("bootstrap.servers", bootstrap)
  implicit val ec: ExecutionContextExecutor = ExecutionContext.global
  private val topic = "my-topic"
  info(s"***bootstrap = $bootstrap, topic = '$topic'****")
  private val prod = ProducerWrapper.create(topic, props)

  val resultsSync: Seq[RecordMetadata] = (1 to 100).map { _ =>
    val message = MessageGenerator.generateConsistent()
    prod.sendSync(message.key, message.value)
  }

  resultsSync.foreach(RecordMetadataPrinter.printConsole)

  // val futureRes: immutable.Seq[Future[RecordMetadata]] = (1 to 100).map(_ => shotMessage(prod))
  //futureRes.foreach { fr: Future[RecordMetadata] => fr.foreach(printRecordMetadata) }
  //futureRes.filter(!_.isCompleted).foreach(Printer.printRecordMetadata)

  Thread.sleep(5000)
  prod.close()
  info("*** ENDED ****")
}
