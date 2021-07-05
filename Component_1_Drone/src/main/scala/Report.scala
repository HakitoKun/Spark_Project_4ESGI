import org.joda.time.DateTime

class Report (val date : DateTime,
              val id : Int,
              val position: (Double, Double),
              val citizenInVicinity : List[(String, Int)],
              val words : List[String]) {

  /**
   * Overrides the function toString in order to print the report
   * in a csv format (might later be a json format)
   * @return
   */
  override def toString: String = {
    id + ";" +
      date + ";" +
      position._1 + "," + position._2 + ";" +
      citizenInVicinity.mkString(",") + ";" +
      words.mkString(",")
  }

}
