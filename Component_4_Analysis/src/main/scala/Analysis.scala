import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._



object Analysis {


    def main(args: Array[String]): Unit = {
        val spark = SparkSession.builder
          .master("local[2]")
          .appName("Analysis")
          .getOrCreate();
        import spark.implicits._
        val df_json = spark.read.option("header", "true").json("../Saved_Data/Data_Reports.json/*.json")


        val df1 = df_json.withColumn("elem", explode(col("citizenInVicinity")))

        val df2 = df1.withColumn("name", $"elem._1")
          .withColumn("score", $"elem._2")
          .withColumn("longitude", col("position._1$mcD$sp"))
          .withColumn("latitude", col("position._2$mcD$sp"))
          .withColumn("test", from_unixtime(unix_timestamp($"date")))
          .withColumn("Date", date_format(col("test"), "yyyy-MM-dd"))
          .withColumn("Time", date_format(col("test"), "HH:mm:ss"))
          .select($"drone_id", $"Date", $"Time", $"longitude", $"latitude", $"name", $"score", $"words")
        df2.show()
        df2.printSchema()

        println("\n\n\n\n")

    }

}
