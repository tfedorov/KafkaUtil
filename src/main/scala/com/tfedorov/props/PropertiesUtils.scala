package com.tfedorov.props

import java.util.Properties

object PropertiesUtils {

  def defaultProps(): Properties = {
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9094")
    //props.put("bootstrap.servers", "localhost:29092")
    props.put("group.id", "Idea-Macc")
    props.put("producer.auto.create.topics", "auto")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props
  }

  case class PropertiesWrapper(properties: Properties) {
    implicit def +(key: String, value: String): Properties = {
      val newProps = new Properties()
      newProps.putAll(properties)
      newProps.put(key, value)
      newProps
    }
  }

  implicit def pro2Wrapper(properties: Properties): PropertiesWrapper = {
    PropertiesWrapper(properties)
  }

}
