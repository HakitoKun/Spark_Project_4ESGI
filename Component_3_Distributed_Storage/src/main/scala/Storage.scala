import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010._
//import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming._

import java.time.Duration
import java.util.Properties
import scala.annotation.tailrec
import scala.collection.convert.ImplicitConversions.`iterable AsScalaIterable`


object Storage {


    def main(args: Array[String]): Unit = {
        val topic = Array("Reports")
        Logger.getLogger("org").setLevel(Level.WARNING)
        Logger.getLogger("akka").setLevel(Level.WARNING)
        Logger.getLogger("kafka").setLevel(Level.WARNING)

        val kafkaProb = Map[String, Object](
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> "localhost:9092",
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
            ConsumerConfig.GROUP_ID_CONFIG -> "consumer-group-data-storage",
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG -> "latest",
            ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG -> (false: java.lang.Boolean)
        )



        val spark = SparkSession.builder
          .master("local[2]")
          .appName("Peacekeeper_Storage")
          .getOrCreate();

        val sc = spark.sparkContext

        val streamingContext = new StreamingContext(sc, Seconds(5))

        val stream = KafkaUtils.createDirectStream[String, String](
            streamingContext,
            PreferConsistent,
            Subscribe[String, String](topic, kafkaProb)
        )

        stream.foreachRDD(line => {
            if(!line.isEmpty()) {
                val rdd : RDD[String] = line.map(_.value.toString)
                import spark.implicits._
                val df = spark.read.json(spark.createDataset(rdd))
                df.show()
                df.printSchema()
            }
        })

        streamingContext.start()
        streamingContext.awaitTermination()

    }

}
