import org.joda.time.DateTime
import scala.annotation.tailrec
import scala.collection.immutable.Stream
import scala.language.postfixOps
import scala.util.Random
import scala.util.Random.nextDouble

object Drone {
  def main(args: Array[String]): Unit = {
    print(generateReport(5).toString)
  }

  /**
   * Function that generate a report
   * @param id
   */
  def generateReport(id : Int): Report = {
    val date = generateTimestamp()
    val position = generateCurrentLocation()
    val citizenInVincinity = (generateNameCitizen take between(2, 25))
    val words = (generateSentence take 6)
    new Report(date, 1, position, citizenInVincinity, words)
  }

  /**
   * Generate one word between 2 and 25 char
   * @return
   */
  def generateWord: Stream[String] = {
    def word : String = {
      (Random.alphanumeric take between(2, 25)).mkString
    }
    Stream continually word
  }

  /**
   * Generate one sentences between 2 and 25 words
   * @return
   */
  def generateSentence : Stream[List[String]] = {
    def sentences : List[String] = {
      generateWord take between(2, 25) toList
    }
    Stream continually sentences
  }

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
  def generateNameCitizen() : String = {
    (Random.alphanumeric take between(2, 25)).mkString
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


  /** Returns the next pseudorandom, uniformly distributed double value
   *  between min (inclusive) and max (exclusive) from this random number generator's sequence.
   *  Pas disponible 2.12 (Disponible 2.13)
   *  https://github.com/scala/scala/blob/8a2cf63ee5bad8c8c054f76464de0e10226516a0/src/library/scala/util/Random.scala#L57
   */
  def between(minInclusive: Double, maxExclusive: Double): Double = {
    require(minInclusive < maxExclusive, "Invalid bounds")

    val next = nextDouble() * (maxExclusive - minInclusive) + minInclusive
    if (next < maxExclusive) next
    else Math.nextAfter(maxExclusive, Double.NegativeInfinity)
  }


  /** Returns a pseudorandom, uniformly distributed int value between min
   *  (inclusive) and the specified value max (exclusive), drawn from this
   *  random number generator's sequence.
   *  Pas disponible 2.12 (Disponible 2.13)
   *  https://github.com/scala/scala/blob/8a2cf63ee5bad8c8c054f76464de0e10226516a0/src/library/scala/util/Random.scala#L98
   */
  def between(minInclusive: Int, maxExclusive: Int): Int = {
    require(minInclusive < maxExclusive, "Invalid bounds")

    val difference = maxExclusive - minInclusive
    if (difference >= 0) {
      Random.nextInt(difference) + minInclusive
    } else {
      /* The interval size here is greater than Int.MaxValue,
       * so the loop will exit with a probability of at least 1/2.
       */
      @tailrec
      def loop(): Int = {
        val n = Random.nextInt()
        if (n >= minInclusive && n < maxExclusive) n
        else loop()
      }
      loop()
    }
  }
}
