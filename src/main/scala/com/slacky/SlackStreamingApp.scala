package com.slacky

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/* App to get updates from slack and stream them to print to console */
object SlackStreamingApp {

    def main(args: Array[String]) {
        val conf = new SparkConf().setMaster(args(0)).setAppName("SlackStreaming")
        val ssc = new StreamingContext(conf, Seconds(5))
        val stream = ssc.receiverStream(new SlackReceiver(args(1)))

        stream.print() // websocket should return a json. we print that here.

        if (args.length > 2) { // output .part and _SUCCESS files to a folder
            stream.saveAsTextFiles(args(2))
        }

        // let the party begin
        ssc.start()
        ssc.awaitTermination()
    }
}
