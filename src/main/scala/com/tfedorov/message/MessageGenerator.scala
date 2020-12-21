package com.tfedorov.message

import java.util.concurrent.atomic.AtomicInteger

object MessageGenerator {

  private val _counted = new AtomicInteger(100)

  private def count: Int = _counted.addAndGet(1)

  def generateMessage[V](implicit generateF: String => V): Message[String, V] = {
    val id = count.toString
    Message(id, generateF(id))
  }
}
