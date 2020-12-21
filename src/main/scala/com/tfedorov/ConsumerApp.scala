package com.tfedorov

import com.tfedorov.consumer.ConsumerWrapper
import com.tfedorov.message.{Message, Payment}
import com.tfedorov.props.PropertiesUtils.{defaultProps, dockerContainerDefProps}
import io.confluent.kafka.serializers.{KafkaAvroDeserializer, KafkaAvroDeserializerConfig}
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer

import java.util.Properties
import scala.util.Properties.envOrElse

object ConsumerApp extends App with Logging {

  info(s"*** STARTED ${this.getClass.getName}****")

  private val props: Properties = dockerContainerDefProps()
  props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer])
  props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[KafkaAvroDeserializer])
  props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, "true")
  private val topic = envOrElse("KAFKA_TOPIC", "myTopicSchema")

  info(s"***bootstrap = ${props.getProperty("bootstrap.servers")}, topic = '$topic'****")
  private val consumer = ConsumerWrapper.createFromMessage(topic, props, Payment.empty)

  consumer.readFromBeginning(_.simplePrintF)

  info("*** infinite loop ****")
  // consumer.autoCommitPoll(_.simplePrintF)
  //while (true)
  //  consume.asyncPoll { (key, value) => println(s"key=$key, value=$value") }
  while (true)
    consumer.asyncPoll(_.simplePrintF)
}
