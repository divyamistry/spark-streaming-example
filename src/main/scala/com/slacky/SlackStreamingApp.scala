package com.slacky

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * App to get updates from slack and stream them to print to console
 */
object SlackStreamingApp {

    def main(args: Array[String]) {
        val conf = new SparkConf().setMaster(args(0)).setAppName("SlackStreaming")
        val ssc = new StreamingContext(conf, Seconds(5))
        val stream = ssc.receiverStream(new SlackReceiver(args(1)))

        // websocket should return a json. we print that here.
        stream.print()

        // save the output to a folder with .part and _SUCCESS files
        if (args.length > 2) {
            stream.saveAsTextFiles(args(2))
        }

        // let the party begin
        ssc.start()
        ssc.awaitTermination()
    }
}
