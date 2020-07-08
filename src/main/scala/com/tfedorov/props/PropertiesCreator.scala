package com.tfedorov.props

import java.util.Properties

object PropertiesCreator {

  def create(): Properties = {
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")
    //props.put("bootstrap.servers", "localhost:29092")
    props.put("group.id", "Idea")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props
  }
}
