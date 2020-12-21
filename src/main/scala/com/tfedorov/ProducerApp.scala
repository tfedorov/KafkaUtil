package com.tfedorov

import com.tfedorov.message.{MessageGenerator, Payment}
import com.tfedorov.producer.{ProducerWrapper, RecordMetadataPrinter}
import com.tfedorov.props.PropertiesUtils.dockerContainerDefProps
import io.confluent.kafka.serializers.KafkaAvroSerializer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer

import java.util.Properties
import scala.util.Properties.envOrElse

object ProducerApp extends App with Logging {

  info(s"*** STARTED ${this.getClass.getName}.****")

  private val props: Properties = dockerContainerDefProps()
  private val topic = envOrElse("KAFKA_TOPIC", "myTopicSchema")
  private val server = props.getProperty("bootstrap.servers")
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer])
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer])


  info(s"***bootstrap = $server, topic = '$topic'****")
  private val producer: ProducerWrapper[String, Payment] = ProducerWrapper.createTyped(topic, props)
  info(s"*** start sending messages ****")
  (1 to 100).map(_ => simpleMessage())

  private def simpleMessage(): Unit = {
    val message = MessageGenerator.generatePayment()
    val response = producer.sendSync(message.key, message.value)
    RecordMetadataPrinter.printConsole(response)
  }

  /*  while (true) {
      Thread.sleep(5000)
      simpleMessage()
    }*/

  // val futureRes: immutable.Seq[Future[RecordMetadata]] = (1 to 100).map(_ => shotMessage(prod))
  //futureRes.foreach { fr: Future[RecordMetadata] => fr.foreach(printRecordMetadata) }
  //futureRes.filter(!_.isCompleted).foreach(Printer.printRecordMetadata)


  producer.close()
  info("*** ENDED ****")
}
