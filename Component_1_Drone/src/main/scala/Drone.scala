import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer

import java.time.format.DateTimeFormatter
import java.util.Properties
import scala.annotation.tailrec
import scala.collection.immutable.LazyList
import scala.language.postfixOps
import scala.util.Random
import scala.util.Random.between


object Drone {
  def main(args: Array[String]): Unit = {


    val alertProperty: Properties = new Properties()

    alertProperty.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    alertProperty.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer])
    alertProperty.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer])

    val producer: KafkaProducer[String, String] = new KafkaProducer[String, String](alertProperty)

    run(1, 5, producer)
    producer.close()

//    val report: Report = generateReport(1)
//    println(report.toString)
//    processReport(alertProducer, report, "Alert")
//    processReport(alertProducer, report, "Reports")
//    alertProducer.close()
//     generateWord take between(2, 25)
  }

  /**
   * Fake loop to run a simulated drone
   *
   * Run the drone with a wait time of 0.5 seconds each no action iteration of i. The simulation ends when i reaches
   * 10000
   * This function is tail recursive
   * @param i Starting iterator (1)
   * @param id ID of the drone
   * @param producer A already active producer
   */
  @tailrec
  def run(i: Int, id :Int, producer: KafkaProducer[String, String]): Unit = {
    i match {
      case 1 =>
        println("Starting up the Drone POC simulation")
        generateReport(id)
        run(i + 1, id, producer)
      case i if i % 90 == 0 =>
        println("The drone is generating an alert, sending is imminent")
        val report: Report = generateReport(id)
        processReport(producer, report, "Alert")
        run(i + 1, id, producer)
      case i if i % 20 == 0 =>
        println("The drone is generating an report, sending is imminent")
        val report_1: Report = generateReport(id)
        processReport(producer, report_1, "Reports")
        run(i + 1, id, producer)
      case 10000 =>
        println("end")
      case _ =>
        Thread.sleep(500)
        run(i + 1, id, producer)
    }

  def processReport(producer: KafkaProducer[String, String], r: Report, topic: String): Unit = {
    // Checks for negatives scores and prepare the Alert
    topic match {
      case "Alert" => {
        val processedAlert: List[(String, Int)] = r.citizenInVicinity.collect {
          case x if x._2 < 0 => x
        }
        processedAlert.foreach(x => sendAlert(producer, "Alert", x._1, x._2, r.position.toString()))
      }
      case "Reports" =>
        sendReport(producer, "Reports", r)
    }
  }

  /**
   * Function that generate a report
   * @param id default parameter : 1
   */
  def generateReport(id : Int): Report = {
    val date: String = generateTimestamp().toString
    val position: (Double, Double) = generateCurrentLocation()
    val citizenInVicinity: List[String] = (generateNameCitizen take between(2, 25)) toList
    val citizenWithScore: List[(String, Int)] = assignPeaceScore(citizenInVicinity)
    val words: List[String] = (generateWord take between(2, 25) toList)
    new Report(date, 1, position, citizenWithScore, words)
  }

  /**
   * Generate one word between 2 and 25 char
   * @return
   */
  def generateWord: LazyList[String] = {
    def word: String = {
      (Random.alphanumeric take between(2, 10)).mkString
    }
    LazyList continually word
  }

  /**
   * Generate one sentences between 2 and 25 words
   * @return
   */
  /*def generateSentence : LazyList[List[String]] = {
    def sentences : List[String] = {
      (generateWord take between(2, 10)) toList
    }
    LazyList continually sentences
  }*/

  /**
   * Generate a GPS coordinate tuple (Latitude, Longitude)
   * @return
   */
  def generateCurrentLocation(): (Double, Double) = {
    (between(-90.0, 90.0), between(-180.0, 180.0))
  }

  /**
   * Generate a single word defining name from a LazyList of alphanumeric
   * @return
   */
  def generateNameCitizen(): LazyList[String] = {
    def word: String = {
      (Random.alphanumeric take between(2, 10)).mkString
    }
    LazyList continually word
  }

  /**
   * Generate a peace score between -42 and 42.
   * Positive integer is for good behaviour
   * Negative integer is for bad / dangerous behaviour
   * Neutral 0 integer is for neutral behaviour
   * @return Int
   */
  def generatePeaceScore(): Int = {
    between(-42, 42)
  }

  def assignPeaceScore(l : List[String]): List[(String, Int)] = {
    l.map(name => (name, generatePeaceScore()))
  }

  /**
   * Generate the current local TimeStamp using DateTime
   * @return
   */
  def generateTimestamp(): String = {
    java.time.LocalDateTime.now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
  }

  /* Alert Support Producer */

  /**
   * A producer that sends an Alert to the Alert Consumer
   * @param alertProducer
   * @param topic
   * @param name
   * @param score
   * @param location
   */
  def sendAlert(alertProducer: KafkaProducer[String, String], topic: String, name: String, score: Int, location: String): Unit = {
    val stringConcat = location.concat(",").concat(score.toString)
    val recordAlert = new ProducerRecord[String, String](topic, name, stringConcat)
    alertProducer.send(recordAlert)
    println(s"[$topic] The drone has sent alert for $name located at $location with a score of $score")
  }

  /**
   * A producer that sends a report to the report consumer
   * @param alertProducer
   * @param topic
   * @param report
   */
  def sendReport(alertProducer: KafkaProducer[String, String], topic: String, report: Report): Unit = {
    val stringConcat = report.toJson
    val recordAlert = new ProducerRecord[String, String](topic, report.drone_id.toString, stringConcat)
    println(stringConcat)
    alertProducer.send(recordAlert)
    println(s"[$topic] The drone has sent a Report")
  }

}
