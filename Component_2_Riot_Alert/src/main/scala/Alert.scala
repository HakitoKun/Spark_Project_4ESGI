import org.apache.spark.sql.SparkSession

object Alert {
    val spark: SparkSession = SparkSession.builder
    .master("local[2]")
    .appName("peacekeeper")
    .getOrCreate();
}