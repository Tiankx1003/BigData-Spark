package com.tian.day01.transform

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * 无状态转换
 *
 * @author tian
 * @date 2019/9/23 20:12
 * @version 1.0.0
 */
object NonStateTransform {
    def main(args: Array[String]): Unit = {
        val conf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("NonStateTransform")
        val ssc: StreamingContext = new StreamingContext(conf, Seconds(3))
        val socketSteam: ReceiverInputDStream[String] = ssc.socketTextStream("hadoop02", 9999)
        val resultDStream: DStream[(String, Int)] =
            socketSteam.transform(_.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _))
        resultDStream.print
        ssc.start()
        ssc.awaitTermination()
    }
}
