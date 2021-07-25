import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}
import org.apache.kafka.common.serialization.StringDeserializer
import java.time.Duration
import java.util.{Collections, Properties}
import scala.annotation.tailrec
import scala.collection.convert.ImplicitConversions.`iterable AsScalaIterable`



object Storage {
    def main(args: Array[String]): Unit = {
        val topic = "Reports"


        val props = new Properties()
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer])
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer])
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer-group-data-storage")

        val consumer = new KafkaConsumer[String, String](props)
        consumer.subscribe(Collections.singletonList(topic))

        manageConsumer(consumer, 1000,1)
        consumer.close()
    }


    /**
     * Retrieves and print any received poll from the subscribed consumer (topic alert)
     * @param consumer A KafkaConsumer[String, String]
     * @param consumerLifetime An Integer
     * @param pollDuration An Integer
     */
    @tailrec
    def manageConsumer(consumer: KafkaConsumer[String, String], consumerLifetime: Int, pollDuration: Int): Unit = {
        if (consumerLifetime != 0) {
            val polledRecords = consumer.poll(Duration.ofSeconds(pollDuration))
            if (!polledRecords.isEmpty) {
                println(s"Polled ${polledRecords.count()} records")
                polledRecords.foreach(record => {
                    println(s"ALERT : A Peacemaker is required at geographical location ${record.value()} for the citizen ${record.key()} !")
                })}
            manageConsumer(consumer, consumerLifetime - pollDuration, pollDuration)
        }
    }

    // Todo Integrate Kafka and Spark
    // Retrieve Data Report
    // Convert it to Spark
    // Store it
    def dataStorage(test: Int):  Unit = {
    }
}
