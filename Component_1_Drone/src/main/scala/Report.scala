import net.liftweb.json._
import net.liftweb.json.Serialization.write


class Report (val date : String,
              val drone_id : Int,
              val position: (Double, Double),
              val citizenInVicinity : List[(String, Int)],
              val words : List[String]) {

  /**
   * Overrides the function toString in order to print the report
   * in a csv format (might later be a json format)
   * @return
   */
  override def toString: String = {
    drone_id + ";" +
      date + ";" +
      position._1 + "," + position._2 + ";" +
      citizenInVicinity.mkString(",") + ";" +
      words.mkString(",")
  }

  /**
   * Convert the class Report into a JSON formatted String
   * @return JSon String
   */
  def toJson: String = {
    implicit val formats = DefaultFormats
    write(this)
  }

}