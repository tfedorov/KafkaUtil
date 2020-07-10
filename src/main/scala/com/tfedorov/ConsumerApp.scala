package com.tfedorov

import com.tfedorov.consumer.{ConsoleRecordsProcessor, ConsumerWrapper}

object ConsumerApp extends App with Logging {

  info("*** STARTED ****")

  private val topic4testProducer = ConsumerWrapper.default("topic4test")
  private val recordsProcessor = new ConsoleRecordsProcessor()

  info("*** infinite loop ****")
  while (true)
    topic4testProducer.asyncPoll(recordsProcessor)
}
