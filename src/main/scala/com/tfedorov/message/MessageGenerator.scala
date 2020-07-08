package com.tfedorov.message

import java.util.concurrent.atomic.AtomicInteger

import com.tfedorov.util.FormatDate._

import scala.util.Random

object MessageGenerator {


  private def randomChar() = Random.nextPrintableChar

  private def random3Char(): String = randomChar().toString + randomChar() + randomChar()

  private val _counted = new AtomicInteger(0)

  private def count: Int = _counted.addAndGet(1)

  def generateConsistent(): Message[String, String] = {
    val mConsist = count
    Message(mConsist.toString, mConsist + s"$mConsist - ${random3Char()}-value-${nowFormatted()}")
  }

  def generateRandom(): Message[String, String] = {
    Message(random3Char(), s"${random3Char()}-value-${nowFormatted()}")
  }
}
