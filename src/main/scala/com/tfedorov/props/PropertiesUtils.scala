package com.tfedorov.props

import java.util.Properties

import scala.collection.JavaConverters._
import scala.util.Properties.envOrElse

object PropertiesUtils {

  def defaultProps(): Properties = {
    val props = new Properties()
    val bootstrapUrl = envOrElse("BOOTSTRAP_SERVERS", "127.0.0.1:9097")
    props.put("bootstrap.servers", bootstrapUrl)
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
