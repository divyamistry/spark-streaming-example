package com.slacky

import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.receiver.Receiver
import org.jfarcand.wcs.{TextListener, WebSocket}

import scala.util.parsing.json.JSON
import scalaj.http.Http

/**
  * Get updates from slack for slack org for given token
  */
class SlackReceiver(token: String) extends Receiver[String](StorageLevel.MEMORY_ONLY)
                                      with Runnable {
    private val slackUrl = "https://slack.com/api/rtm.start"

    private def webSocketUrl(): String = {
        val response = Http(slackUrl).param("token", token).asString.body
        JSON.parseFull(response).get.asInstanceOf[Map[String, Any]].get("url").get.toString
    }

    private def receive(): Unit = {
        val webSocket = WebSocket().open(webSocketUrl())
        webSocket.listener(new TextListener {
            override def onMessage(message: String) {
                store(message) // store the data into Spark's memory
            }
        })
    }

    @transient
    private var thread: Thread = _

    override def onStart(): Unit = {
        thread = new Thread(this)
        thread.start()
    }

    override def onStop(): Unit = {
        thread.interrupt()
    }

    override def run(): Unit = {
        receive()
    }
}
