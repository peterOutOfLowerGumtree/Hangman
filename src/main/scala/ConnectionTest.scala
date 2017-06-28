import java.sql.{Connection, DriverManager}

object ConnectionTest extends App {
  // connect to the database named "words" on port 8889 of localhost
  val url = "jdbc:mysql://localhost:3306/words"
  val driver = "com.mysql.jdbc.Driver"
  val username = "root"
  val password = "password"
  var connection:Connection = _

  try {
    Class.forName(driver)
    connection = DriverManager.getConnection(url, username, password)
    val statement = connection.createStatement
    val rs = statement.executeQuery("SELECT * FROM words")
    while (rs.next) {
      println(rs.getString("word"))
    }
  } catch {
    case e: Exception => e.printStackTrace
  }
  connection.close
}