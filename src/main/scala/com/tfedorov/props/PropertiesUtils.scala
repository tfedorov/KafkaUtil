package com.tfedorov.props

import com.tfedorov.ProducerApp.props
import io.confluent.kafka.serializers.{KafkaAvroDeserializerConfig, KafkaAvroSerializer}
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer

import java.util.Properties
import scala.collection.JavaConverters._
import scala.util.Properties.envOrElse

object PropertiesUtils {

  def defaultProps(): Properties = {
    val props = new Properties()
    props.put("group.id", "Idea-Macc")
    props.put("producer.auto.create.topics", "auto")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props
  }

  def k8sDefaultProps(): Properties = {
    val props = defaultProps()
    val bootstrapUrl = envOrElse("BOOTSTRAP_SERVERS", "127.0.0.1:9094")
    props.put("bootstrap.servers", bootstrapUrl)
    props
  }

  def dockerContainerDefProps(): Properties = {
    val props = defaultProps()
    val bootstrapUrl = envOrElse("BOOTSTRAP_SERVERS", "127.0.0.1:9092")
    props.put("bootstrap.servers", bootstrapUrl)
    props.put("schema.registry.url", "http://localhost:8081")
    props
  }

  case class PropertiesWrapper(properties: Properties) {
    implicit def +(key: String, value: String): Properties = {
      val newProps: Properties = new Properties()
      properties.asScala.foreach { case (key: String, value: String) => newProps.put(key, value) }
      newProps.put(key, value)
      newProps
    }
  }

  implicit def pro2Wrapper(properties: Properties): PropertiesWrapper = {
    PropertiesWrapper(properties)
  }

}
