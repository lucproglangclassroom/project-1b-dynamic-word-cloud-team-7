package hellotest
import java.util.Scanner
import mainargs._
import org.apache.commons.collections4.queue.CircularFifoQueue




object Main {
  def main(args: Array[String]): Unit = {
    ParserForMethods(this).runOrExit(args.toIndexedSeq)
    ()
  }
  @main def run(
           @arg(short = 'c', name = "cloudSize") cloudSize: Int = 10,
           @arg(short = 'l', name = "minLength") minLength: Int = 6,
           @arg(short = 'w', name = "windowSize") windowSize: Int = 1000
         ): Unit = {
    println(s"cloudSize: $cloudSize, minLength: $minLength, windowSize: $windowSize")
    queue(cloudSize, minLength, windowSize)
  }
}
  def queue(cloudSize: Int, minLength: Int, windowSize: Int): Unit = {
    // TODO consider using a command-line option library

    try {
        if (windowSize < 1) {
          throw new NumberFormatException()
        }
      } catch {
      case _: NumberFormatException =>
        Console.err.println("argument should be a natural number")
        System.exit(4)
    }

      val input = new Scanner(System.in).useDelimiter("(?U)[^\\p{Alpha}0-9']+")
      val queue = new CircularFifoQueue(windowSize)

      try {
        while (input.nn.hasNext) {
          val word = input.nn.next()
          queue.add(word) // the oldest item automatically gets evicted
          Console.println(queue)
        }
      } catch {
        case e: Exception =>
          Console.err.println(s"An error occurred: ${e.getMessage}")
          System.exit(1)
      }
  }

