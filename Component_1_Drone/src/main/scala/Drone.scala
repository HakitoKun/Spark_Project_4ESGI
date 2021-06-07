import org.joda.time.DateTime

import scala.annotation.tailrec
import scala.collection.immutable.LazyList
import scala.language.postfixOps
import scala.util.Random
import scala.util.Random.{between, nextDouble}

object Drone {
  def main(args: Array[String]): Unit = {
    print(generateReport(5).toString)
    // (generateSentence take between(2, 100)).foreach(x => println(x.mkString trim))

   // generateWord take between(2, 25)
  }

  /**
   * Function that generate a report
   * @param id
   */
  def generateReport(id : Int): Report = {
    val date = generateTimestamp()
    val position = generateCurrentLocation()
    val citizenInVicinity = ((generateNameCitizen take between(2, 25)) toList)
    val words = (generateWord take between(2, 25) toList)
    new Report(date, 1, position, citizenInVicinity, words)
  }

  /**
   * Generate one word between 2 and 25 char
   * @return
   */
  def generateWord: LazyList[String] = {
    def word : String = {
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
  def generateCurrentLocation() : (Double, Double) = {
    (between(-90.0, 90.0), between(-180.0, 180.0))
  }

  /**
   * Generate a single word defining name from a LazyList of alphanumeric
   * @return
   */
  def generateNameCitizen() : LazyList[String] = {
    def word : String = {
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
  def generatePeaceScore() : Int = {
    between(-42, 42)
  }

  /**
   * Generate the current local TimeStamp using DateTime
   * @return
   */
  def generateTimestamp() : DateTime = {
    DateTime.now()
  }
}
