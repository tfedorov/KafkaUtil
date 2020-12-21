package com.tfedorov.message

case class Message[K, V](key: K, value: V) {

  def simplePrintF: (K, V) => Unit = (key: K, value: V) => println(s"key=$key, value=$value")

}

object Message {

  def empty: Message[String, String] = Message("", "")
}