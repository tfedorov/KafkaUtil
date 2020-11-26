package com.tfedorov.props

import java.util.Properties

import org.junit.jupiter.api.Assertions._
import org.junit.jupiter.api.Test

class PropertiesUtilsTest {

  @Test
  def pro2WrapperPlus(): Unit = {
    val wrapper = PropertiesUtils.pro2Wrapper(new Properties())

    val actualResult = wrapper + ("newKey", "new Value")

    val expected = new Properties()
    expected.put("newKey", "new Value")
    assertEquals(expected, actualResult)
  }

  @Test
  def defaultPropsSerializer(): Unit = {

    val actualResult: Properties = PropertiesUtils.defaultProps

    val expected = "org.apache.kafka.common.serialization.StringSerializer"
    assertEquals(expected, actualResult.getProperty("key.serializer"))
  }
}
