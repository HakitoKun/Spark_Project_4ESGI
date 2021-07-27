import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.functions.{concat_ws, lit}
import org.apache.spark.sql.{Column, SparkSession, functions}
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010._
import java.util.logging.{Level, Logger}



object Analysis {


    def main(args: Array[String]): Unit = {
   

    }

}
