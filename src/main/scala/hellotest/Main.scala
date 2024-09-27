package hellotest

import org.log4s._
import java.util.Scanner
import mainargs._
import org.apache.commons.collections4.queue.CircularFifoQueue


import java.util.NoSuchElementException
import scala.io.Source
import scala.language.unsafeNulls

import scala.collection.mutable.Queue

import scala.collection.mutable

class CircularQueue(capacity: Int) {
  private val queue = mutable.Queue.empty[String]
  private val wordCount = mutable.Map[String, Int]().withDefaultValue(0)

  def add(element: String): Unit = {
    // If queue is full, remove the oldest element and update the word count
    if (queue.size >= capacity) {
      val removed = queue.dequeue()
      wordCount(removed) -= 1
      if (wordCount(removed) == 0) {
        wordCount.remove(removed)
      }
    }

    // Add the new element and update the word count
    queue.enqueue(element)
    wordCount(element) += 1
  }

  def getElements: Seq[String] = queue.toSeq

  def size: Int = queue.size


  def topFrequentWords(topN: Int, minFrequency: Int): Seq[(String, Int)] = {
    wordCount
      .filter(_._2 > minFrequency)
      .toSeq
      .sortBy(-_._2)
      .take(topN)
  }

  override def toString: String = queue.mkString("[", ", ", "]")
}


object Main {

  private[this] val logger = org.log4s.getLogger

  var PRINT_COUNTER: Int = 0

  def main(args: Array[String]): Unit = {
    ParserForMethods(this).runOrExit(args.toIndexedSeq)
    ()
  }
  @main def run(
                 @arg(short = 'c', name = "cloudSize") cloudSize: Int = 10,
                 @arg(short = 's', name = "kSteps") kSteps: Int = 6,
                 @arg(short = 'f', name = "minFrequency") minFrequency: Int = 6,
                 @arg(short = 'l', name = "minLength") minLength: Int = 6,
                 @arg(short = 'w', name = "windowSize") windowSize: Int = 1000
               ): Unit = {

    logger.debug(f"cloudSize : $cloudSize, kSteps : $kSteps, minFrequency : $minFrequency, minLength : $minLength, windowSize: $windowSize ")
    // Check for the correct number of arguments

    // Initialize the circular queue
    val queue = new CircularQueue(windowSize)

    // Read input from standard input and split it into words
    val lines = Source.stdin.getLines
    val words = lines.flatMap(line => line.split("(?U)[^\\p{Alpha}0-9']+"))

    // Process each word and add to the queue
    try {
      words.foreach { word =>
        if (word.length >= minLength) {
          //case insensitive now
          val lowercasedWord = word.toLowerCase
          queue.add(lowercasedWord)

          if (queue.size >= windowSize) {
            // kWords = 1 by default
            //print counter = 0 by default
            PRINT_COUNTER += 1
            if (PRINT_COUNTER % kSteps == 0){
              val topWords = queue.topFrequentWords(cloudSize, minFrequency)
              println(s"Top words: $topWords") // Print the top frequent words
              if scala.sys.process.stdout.checkError() then sys.exit(1) //I think this is how we handle SIGPIPE?
            }
          }
        }
      }
    } catch {
      case _: NoSuchElementException =>
        println("End of input (Ctrl-D) detected")
    }
  }
}

