package com.tfedorov

import java.util.Properties

import com.tfedorov.consumer.ConsumerWrapper
import com.tfedorov.props.PropertiesUtils.{defaultProps, _}

object ConsumerApp extends App with Logging {

  info(s"*** STARTED ${this.getClass.getName}****")

  private val bootstrap = "127.0.0.1:9094"
  private val props: Properties = defaultProps() + ("bootstrap.servers", bootstrap)
  private val topic = "my-topic"

  info(s"***bootstrap = $bootstrap, topic = '$topic'****")
  private val producer = ConsumerWrapper.create(topic, props)

  info("*** infinite loop ****")
  //while (true)
  //  producer.asyncPoll { (key, value) => println(s"key=$key, value=$value") }
  while (true)
    producer.asyncPoll(producer.createPrintF)
}
