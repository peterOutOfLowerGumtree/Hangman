import java.sql.{Connection, DriverManager}

object Hangman extends App {
  val url = "jdbc:mysql://localhost:3306/words"
  val driver = "com.mysql.jdbc.Driver"
  val username = "root"
  val password = "password"
  var connection: Connection = _
  var selectedWord: String = ""
  var randWordId: Int = 0
  var count = 0

  val computerWord = getRandWord
//  println(computerWord)
  var mappedWord = computerWord.map(_ -> '_')
//  for ((k, v) <- mappedWord) printf("(%s, %s)\n", k, v)
  var hiddenWord: String = ""
  for ((k, v) <- mappedWord) hiddenWord += v
  hiddenWord = hiddenWord.substring(0,computerWord.length-1)
  hiddenWord.foreach(c => print(c + " "))

  println()
  printHangedMan()
  println("Enter a letter:")
  var input = scala.io.StdIn.readLine
  checkLength(input)
  guess(input(0))


  def checkLength(str: String): Any = {
    if (input.length > 1) {
      println("\nEnter a letter:")
      input = scala.io.StdIn.readLine
    }
    else input.toCharArray
  }


  def guess(inputChar: Char): Unit = {
    checkIfCorrectGuess(inputChar)
    printHangedMan()
    hiddenWord.foreach(c => print(c + " "))

    if (!hiddenWord.contains('_')) println("\nYou win!")
    else if(count == 8) println("You failed! Correct word is "+computerWord)
    else {
      println()
      println("\nEnter a letter:")
      input = scala.io.StdIn.readLine
      checkLength(input)
      guess(input(0))
    }
  }


  def checkIfCorrectGuess(inputChar: Char): Unit = {
    for (i <- 0 until computerWord.length) {
      if (computerWord(i) == inputChar) {
        hiddenWord = hiddenWord.substring(0, i) + computerWord.charAt(i) + hiddenWord.substring(i + 1)
      }
    }
    if(!computerWord.contains(inputChar)) {
      count = count + 1
      println("WRONG!")
    }
  }


  def getRandWord: String = {
    try {
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)
      val statement = connection.createStatement
      val tableSize = statement.executeQuery("SELECT id FROM words ORDER BY id DESC LIMIT 1")
      tableSize.next
      randWordId = scala.util.Random.nextInt(tableSize.getInt("id")) + 1
      val output = statement.executeQuery("SELECT * FROM words WHERE id=" + randWordId)
      output.next
      selectedWord = output.getString("word")
    } catch {
      case e: Exception => e.printStackTrace()
    }
    connection.close()
    selectedWord
  }


  def printHangedMan(): Unit = {
    count match {
      case 0 => println("     \n    |      \n    |      \n    |      \n    |       \n    |      \n    |\n____|________")
      case 1 => println("     _______\n    |      \n    |      \n    |      \n    |       \n    |      \n    |\n____|________")
      case 2 => println("     _______\n    |/      |\n    |      \n    |      \n    |       \n    |      \n    |\n____|________")
      case 3 => println("     _______\n    |/      |\n    |      (_)\n    |      \n    |       \n    |      \n    |\n____|________")
      case 4 => println("     _______\n    |/      |\n    |      (_)\n    |       |\n    |       |\n    |      \n    |\n____|________")
      case 5 => println("     _______\n    |/      |\n    |      (_)\n    |      \\|\n    |       |\n    |      \n    |\n____|________")
      case 6 => println("     _______\n    |/      |\n    |      (_)\n    |      \\|/\n    |       |\n    |      \n    |\n____|________")
      case 7 => println("     _______\n    |/      |\n    |      (_)\n    |      \\|/\n    |       |\n    |      / \n    |\n____|________")
      case _ => println("     _______\n    |/      |\n    |      (_)\n    |      \\|/\n    |       |\n    |      / \\\n    |\n____|________")
    }
  }


}