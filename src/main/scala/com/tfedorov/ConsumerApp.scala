package com.tfedorov

import com.tfedorov.consumer.ConsumerWrapper
import com.tfedorov.message.Message
import com.tfedorov.props.PropertiesUtils.defaultProps

import java.util.Properties
import scala.util.Properties.envOrElse

object ConsumerApp extends App with Logging {

  info(s"*** STARTED ${this.getClass.getName}****")

  private val props: Properties = defaultProps()

  private val topic = envOrElse("KAFKA_TOPIC", "my-topic")

  info(s"***bootstrap = ${props.getProperty("bootstrap.servers")}, topic = '$topic'****")
  private val consumer = ConsumerWrapper.createFromMessage(topic, props, Message.strings)

  consumer.readFromBeginning(_.simplePrintF)

  info("*** infinite loop ****")
  // consumer.autoCommitPoll(_.simplePrintF)
  //while (true)
  //  consume.asyncPoll { (key, value) => println(s"key=$key, value=$value") }
  while (true)
    consumer.asyncPoll(_.simplePrintF)
}
