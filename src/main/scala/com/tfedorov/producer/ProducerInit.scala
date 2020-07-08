package com.tfedorov.producer

import java.util.Properties

import org.apache.kafka.clients.producer.KafkaProducer

import scala.concurrent.ExecutionContextExecutor

object ProducerInit {
  def createPWrapper()(implicit ec: ExecutionContextExecutor): ProducerWrapper[String, String] = {
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")
    //props.put("bootstrap.servers", "localhost:29092")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    val producer = new KafkaProducer[String, String](props)
    new ProducerWrapper(producer)
  }

}
