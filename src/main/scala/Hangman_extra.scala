import java.sql.{Connection, DriverManager}

object Hangman_extra extends App {
  val url = "jdbc:mysql://localhost:3306/words"
  val driver = "com.mysql.jdbc.Driver"
  val username = "root"
  val password = "password"
  var connection: Connection = _
  var selectedWord: String = ""
  var randWordId: Int = 0
  var count: Int = 0
  var wrongLetters: String = ""
  var computerWord: String = ""
  var hiddenWord: String = ""
  var input: String = ""


  println("Welcome to Hangman!\n*******************\n")
  println("Choose a difficulty:\n0: Easy\n1: Intermediate\n2: Hard")
  input = scala.io.StdIn.readLine
  startGame(input)





  def startGame(in:String): Unit = {
    if(in=="0") computerWord = getRandWord(0)
    else if(in=="1") computerWord = getRandWord(1)
    else if(in=="2") computerWord = getRandWord(2)
    else {
      println("Try again. Choose a difficulty:\n0: Easy\n1: Intermediate\n2: Hard")
      input = scala.io.StdIn.readLine.toLowerCase
      startGame(input)
    }
    val mappedWord: Seq[(Char, Char)] = computerWord.map(_ -> '_')
    for ((k, v) <- mappedWord) hiddenWord += v
    hiddenWord = hiddenWord.substring(0,computerWord.length-1)
    printHangedMan()

    hiddenWord.foreach(c => print(c + " "))
    println("\nEnter a letter:")
    input = scala.io.StdIn.readLine.toLowerCase
    checkLength(input)
    guess(input(0))
  }

  def checkLength(str: String): Any = {
    if (input.length > 1) {
      println("\nEnter a letter:")
      input = scala.io.StdIn.readLine.toLowerCase
    }
    else input.toCharArray
  }


  def guess(inputChar: Char): Unit = {
    inputChar.toLower
    checkIfCorrectGuess(inputChar)
    printHangedMan()
    hiddenWord.foreach(c => print(c + " "))

    if (!hiddenWord.contains('_')) println(s"\n\nYou win!")
    else if(count == 9) println(s"\n\nYou failed! Correct word is $computerWord")
    else {
      println()
      println("\nEnter a letter:")
      input = scala.io.StdIn.readLine.toLowerCase
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
      wrongLetters += s"$inputChar "
      println("WRONG!")
    }
  }


  def getRandWord(in: Int): String = {
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

      if(in==2 && selectedWord.length > 5) getRandWord(in)
      if(in==1 && (selectedWord.length<5 || selectedWord.length > 7)) getRandWord(1)
      if(in==0 && selectedWord.length<7) getRandWord(2)

    } catch {
      case e: Exception => e.printStackTrace()
    }
    connection.close()
    selectedWord
  }


  def printHangedMan(): Unit = {
    count match {
      case 0 => println(s"     \n    |      \n    |      \n    |      \n    |       \n    |      \n    |\n____|________\nGuesses left:${9-count}")
      case 1 => println(s"     _______\n    |      \n    |      \n    |      \n    |       \n    |      \n    |\n____|________\nGuesses left: ${9-count}\nWrong guesses: $wrongLetters")
      case 2 => println(s"     _______\n    |/      \n    |      \n    |      \n    |       \n    |      \n    |\n____|________\nGuesses left: ${9-count}\nWrong guesses: $wrongLetters")
      case 3 => println(s"     _______\n    |/      |\n    |      \n    |      \n    |       \n    |      \n    |\n____|________\nGuesses left: ${9-count}\nWrong guesses: $wrongLetters")
      case 4 => println(s"     _______\n    |/      |\n    |      (_)\n    |      \n    |       \n    |      \n    |\n____|________\nGuesses left: ${9-count}\nWrong guesses: $wrongLetters")
      case 5 => println(s"     _______\n    |/      |\n    |      (_)\n    |       |\n    |       |\n    |      \n    |\n____|________\nGuesses left: ${9-count}\nWrong guesses: $wrongLetters")
      case 6 => println(s"     _______\n    |/      |\n    |      (_)\n    |      \\|\n    |       |\n    |      \n    |\n____|________\nGuesses left: ${9-count}\nWrong guesses: $wrongLetters")
      case 7 => println(s"     _______\n    |/      |\n    |      (_)\n    |      \\|/\n    |       |\n    |      \n    |\n____|________\nGuesses left: ${9-count}\nWrong guesses: $wrongLetters")
      case 8 => println(s"     _______\n    |/      |\n    |      (_)\n    |      \\|/\n    |       |\n    |      / \n    |\n____|________\nGuesses left: ${9-count}\nWrong guesses: $wrongLetters")
      case _ => println(s"     _______\n    |/      |\n    |      (_)\n    |      \\|/\n    |       |\n    |      / \\\n    |\n____|________\nGuesses left: ${9-count}\nWrong guesses: $wrongLetters")
    }
  }


}