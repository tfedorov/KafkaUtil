package com.tfedorov

import java.util.Properties

import com.tfedorov.consumer.ConsumerWrapper
import com.tfedorov.props.PropertiesUtils.defaultProps

import scala.util.Properties.envOrElse

object ConsumerApp extends App with Logging {

  info(s"*** STARTED ${this.getClass.getName}****")

  private val props: Properties = defaultProps()
  private val topic = envOrElse("KAFKA_TOPIC", "my-topic")

  info(s"***bootstrap = ${props.getProperty("bootstrap.servers")}, topic = '$topic'****")
  private val producer = ConsumerWrapper.create(topic, props)

  info("*** infinite loop ****")
  //while (true)
  //  producer.asyncPoll { (key, value) => println(s"key=$key, value=$value") }
  while (true)
    producer.asyncPoll(producer.simplePrintF)
}
