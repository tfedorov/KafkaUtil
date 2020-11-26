package com.tfedorov.producer

import java.util.Properties

import com.tfedorov.props.PropertiesUtils
import org.junit.jupiter.api.Assertions._
import org.junit.jupiter.api.Test

class ProducerWrapperTest {

  @Test
  def createMinimal(): Unit = {
    val props = new Properties()
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("bootstrap.servers", "127.0.0.1:9097")

    val actual: ProducerWrapper[String, String] = ProducerWrapper.create("new topic", props)

    assertNotNull(actual)
  }

  @Test
  def createDefault(): Unit = {
    val properties: Properties = PropertiesUtils.defaultProps

    val actual: ProducerWrapper[String, String] = ProducerWrapper.create("new topic", properties)

    assertNotNull(actual)
  }
}
