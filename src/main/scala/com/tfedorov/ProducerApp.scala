package com.tfedorov

import java.util.Properties

import com.tfedorov.message.MessageGenerator
import com.tfedorov.producer.{ProducerWrapper, RecordMetadataPrinter}
import com.tfedorov.props.PropertiesUtils.defaultProps

import scala.util.Properties.envOrElse

object ProducerApp extends App with Logging {

  info(s"*** STARTED ${this.getClass.getName}.****")

  private val props: Properties = defaultProps()
  private val topic = envOrElse("KAFKA_TOPIC", "my-topic")
  private val server = props.getProperty("bootstrap.servers")

  info(s"***bootstrap = $server, topic = '$topic'****")
  private val producer = ProducerWrapper.create(topic, props)
  info(s"*** start sending messages ****")
  (1 to 100).map(_ => simpleMessage())

  private def simpleMessage(): Unit = {
    val message = MessageGenerator.generateConsistent()
    val response = producer.sendSync(message.key, message.value)
    RecordMetadataPrinter.printConsole(response)
  }

  while (true) {
    Thread.sleep(5000)
    simpleMessage()
  }

  // val futureRes: immutable.Seq[Future[RecordMetadata]] = (1 to 100).map(_ => shotMessage(prod))
  //futureRes.foreach { fr: Future[RecordMetadata] => fr.foreach(printRecordMetadata) }
  //futureRes.filter(!_.isCompleted).foreach(Printer.printRecordMetadata)


  producer.close()
  info("*** ENDED ****")
}
