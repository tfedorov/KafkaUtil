package com.tfedorov

import com.tfedorov.consumer.{ConsoleRecordProcessor, ConsumerWrapper}

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}

object ConsumerApp extends App {

  implicit val ec: ExecutionContextExecutor = ExecutionContext.global

  private val consumerWrapper = ConsumerWrapper.default("topic4test")

  consumerWrapper.infiniteListen(new ConsoleRecordProcessor())
  while (true)
    Thread.sleep(5000)
}