package com.tfedorov

import java.util.Properties

import org.apache.kafka.clients.producer.KafkaProducer

object ProducerInit {
  def createPWrapper(): ProducerWrapper[String, String] = {
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    val producer = new KafkaProducer[String, String](props)
    new ProducerWrapper(producer)
  }

}
