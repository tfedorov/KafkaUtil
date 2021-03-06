package com.tfedorov

import org.slf4j.LoggerFactory

trait Logging {
  private val logger = LoggerFactory.getLogger(this.getClass)

  protected def info(text: String): Unit = logger.info(text)

  protected def error(e: Throwable): Unit = logger.info(e.getMessage)

}

