package com.tfedorov.message

import com.tfedorov.util.FormatDate.nowFormatted

import scala.util.Random

case class Message[K, V](key: K, value: V)

object Message {
  def empty: Message[String, String] = Message("", "")

  def simplePrintF[V]: (String, V) => Unit = (key: String, value: V) => println(s"key=$key, value=$value")

  implicit object _implicit {
    private def randomChar() = Random.nextPrintableChar

    private def random3Char(): String = randomChar().toString + randomChar() + randomChar()

    implicit val str2Payment: String => Payment = (target: String) => new Payment(target, Random.nextDouble())
    implicit val str2String: String => String = (target: String) => s"${random3Char()}-value-${nowFormatted()}"
  }

}
