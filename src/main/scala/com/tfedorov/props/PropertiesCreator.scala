package com.tfedorov.props

import java.util.Properties

object PropertiesCreator {

  def create(): Properties = {
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")
    //props.put("bootstrap.servers", "192.168.0.103:9092")
    //props.put("bootstrap.servers", "localhost:29092")
    props.put("group.id", "Idea-Macc")
    props.put("producer.auto.create.topics", "auto")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props
  }
}
