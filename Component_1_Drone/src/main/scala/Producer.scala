import java.util.Properties
import java.util
import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.ListTopicsResult
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.ByteArraySerializer


//import com.sun.media.jfxmedia.logging.Logger;


object KafkaClient {
  private val logger = org.apache.log4j.Logger.getLogger(classOf[KafkaClient])

  def createProducer: Nothing = {
    val props = new Properties
    props.put("bootstrap.servers", "172.0.0.1:9092");
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[Nothing].getName)
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[Nothing].getName)

    props.put(ProducerConfig.RETRIES_CONFIG, "2")
    logger.debug("createProducer -> props : " + props.toString)
    try new Nothing(props)
    catch {
      case e: Exception =>
        logger.error(e.getMessage)
        e.printStackTrace()
        null
    }
  }

  def sendMessage(pProducer: Any, topicName: String, key: Array[Byte], value: Array[Byte]): Unit = { //logger.debug("sendMessage -> " + topicName + " - " + key + " - " + value);
    val producer = pProducer.asInstanceOf[Nothing]
    //Transformation consécutive avant de Foreach
    producer.send(new Nothing(topicName, key, value))
    producer.flush
  }

  /*def isTopicExist(topicName: String): Int = {
    /**
     * System.getenv().forEach((k, v) -> {
     * System.out.println(k + ":" + v);
     * });
     * */
    var returnValue = 0
    try {
      val config = new Properties
      config.put("bootstrap.servers", "172.0.0.1:9092");
      config.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, 10000)
      val admin = AdminClient.create(config)
      val listTopics = admin.listTopics
      admin.close
      var names = null
      names = listTopics.names.get
      val contains = names.contains(topicName)
      if (contains) {
        returnValue = 1
        logger.info("topicExist: " + topicName)
      }
    } catch {
      case e: Exception =>
        returnValue = -1
        logger.error(e.getMessage)
        e.printStackTrace()
    }
    returnValue
  }
  */

  def closeProducer(pProducer: Any): Unit = {
    val producer = pProducer.asInstanceOf[Nothing]
    if (producer != null) producer.close
  }
}     
