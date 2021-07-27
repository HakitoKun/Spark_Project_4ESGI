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

                val reorderedColumns : Array[String] = Array("drone_id","date", "position", "citizenInVicinity", "words")

                // Reorder column, apply string cast to complexes columns
                val res = df.select(reorderedColumns.head, reorderedColumns.tail: _*)
              
                res.show()
                res.printSchema()
//                val df1 = res.withColumn("elem", explode(col("citizenInVicinity")))
//                df1.show()
//                df1.printSchema()
//                println("\n\n\n\n")

                // Save to JSON

                res.write.format("json").option("header", "true").mode("append").save("../Saved_Data/Data_Reports.json")
//                val df2 = df1.withColumn("name", $"elem._1")
//                  .withColumn("score", $"elem._2")
//                  .withColumn("longitude", col("position._1$mcD$sp"))
//                  .withColumn("latitude", col("position._2$mcD$sp"))
//                  .select($"drone_id", $"date", $"longitude", $"latitude", $"name", $"score", $"words")
//                df2.show()
//                df2.printSchema()
            }
        })

        streamingContext.start()
        streamingContext.awaitTermination()

    }

}
