package com.tfedorov.message

import org.apache.avro.specific.SpecificRecord
import org.apache.avro.util.Utf8
import org.apache.avro.{AvroRuntimeException, Schema}

class Payment(var id: String, var amount: Double) extends SpecificRecord {
  def this() = this("", 0)

  override def put(i: Int, v: Any): Unit = i match {
    case 0 =>
      id = v match {
        case utf8: Utf8 => utf8.toString
        case _ => v.asInstanceOf[String]
      }
    case 1 =>
      amount = v match {
        case _ => v.asInstanceOf[Double]
      }
    case _ => throw new AvroRuntimeException("Bad index")
  }

  override def get(i: Int): AnyRef = i match {
    case 0 => id.asInstanceOf[AnyRef]
    case 1 => amount.asInstanceOf[AnyRef]
    case _ => throw new AvroRuntimeException("Bad index")
  }

  override def getSchema: Schema = Payment.SCHEMA$

  override def toString: String = s"Payment($id,$amount)" + super.toString
}

object Payment {
  private val SCHEMA$ =
    (new Schema.Parser).parse(
      """{
        |  "fields": [
        |    {
        |      "name": "id",
        |      "type": "string"
        |    },
        |    {
        |      "name": "amount",
        |      "type": "double"
        |    }
        |  ],
        |  "name": "Payment",
        |  "namespace": "com.tfedorov.message",
        |  "type": "record"
        |}""".stripMargin)

  def empty: Message[String, Payment] = Message("", new Payment("", 0f))
}