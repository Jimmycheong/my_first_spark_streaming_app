package jimmy.learning

import jimmy.learning.StreamingApp.getClass

object Main extends App {

  val logbackPath = getClass.getResource("/logback.xml").getPath
  println("PATH !!!!: " + logbackPath)

}
