import java.util
import org.apache.kafka.clients.consumer.KafkaConsumer
import spray.json.DefaultJsonProtocol.{JsValueFormat, StringJsonFormat}

import java.util.logging.Level
import scala.collection.JavaConverters._
import spray.json._
import DefaultJsonProtocol._




object consummer extends App {

  import java.util.Properties

  val TOPIC="test_drone"

  val  props = new Properties()
  props.put("bootstrap.servers", "127.0.0.1:9092")
  props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  //props.put("value.deserializer", "org.apache.kafka.common.serialization.JsonDeserializer")
  props.put("group.id", "something")

  val consumer = new KafkaConsumer[String, String](props)

  consumer.subscribe(util.Collections.singletonList(TOPIC))



