package com.tfedorov

import java.util.Date

object ProducerApp extends App {

  def shotMessage(producerWrapper: ProducerWrapper[String, String]) = {
    def time: Long = new Date().getTime

    producerWrapper.writeSync("topic4test", s"key$time", s"value$time")
  }

  private val producerWrapper: ProducerWrapper[String, String] = ProducerInit.createPWrapper()

  (1 to 10).foreach(_ => shotMessage(producerWrapper))

  producerWrapper.close()
}
