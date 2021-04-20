import org.joda.time.DateTime

class Report (val date : DateTime,
              val id : Int,
              val position: (Double, Double),
              val citizenInVicinity : String,
              val words : Stream[List[String]]) {

  /**
   * Overrides the function toString in order to print the report
   * in a csv format (might later be a json format)
   * @return
   */
  override def toString: String = {
    id + ";" +
      date + ";" +
      position + ";" +
      citizenInVicinity + ";" +
      words
  }

}
