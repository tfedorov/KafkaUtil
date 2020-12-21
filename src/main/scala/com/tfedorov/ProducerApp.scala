package com.tfedorov

import com.tfedorov.message.{Message, MessageGenerator, Payment}
import com.tfedorov.producer.{ProducerWrapper, RecordMetadataPrinter}
import com.tfedorov.props.PropertiesUtils.dockerContainerDefProps
import io.confluent.kafka.serializers.KafkaAvroSerializer
import org.apache.kafka.clients.producer.{ProducerConfig, RecordMetadata}
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
  private val producer: ProducerWrapper[String, Payment] = ProducerWrapper.create(topic, props)
  info(s"*** start sending messages ****")
  (1 to 100).map(_ => simpleMessage())

  private def simpleMessage(): Unit = {
    import Message._implicit.str2Payment
    val message: Message[String, Payment] = MessageGenerator.generateMessage[Payment]
    val response: RecordMetadata = producer.sendSync(message)
    RecordMetadataPrinter.printConsole(response)
  }

  producer.close()
  info("*** ENDED ****")
}
