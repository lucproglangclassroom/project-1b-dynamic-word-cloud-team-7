package hellotest
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

  var CLOUD_SIZE: Int = 10
  var WORD_LENGTH: Int = 6
  var WINDOW_SIZE: Int = 1000
  var KWORDS: Int = 10
  var PRINT_COUNTER: Int = 0
  var MIN_FREQUENCY: Int = 3


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
   
      CLOUD_SIZE = cloudSize
      WORD_LENGTH = minLength
      WINDOW_SIZE = windowSize
      KWORDS = kSteps
      MIN_FREQUENCY = minFrequency
      println(s"cloudSize: $CLOUD_SIZE, minLength: $WORD_LENGTH, windowSize: $WINDOW_SIZE")
      // Check for the correct number of arguments
      

      // Initialize the circular queue
      val queue = new CircularQueue(WINDOW_SIZE)

      // Read input from standard input and split it into words
      val lines = Source.stdin.getLines
      val words = lines.flatMap(line => line.split("(?U)[^\\p{Alpha}0-9']+"))

      // Process each word and add to the queue
      try {
        words.foreach { word =>
          if (word.length >= WORD_LENGTH) {
            //case insensitive now
            val lowercasedWord = word.toLowerCase
            queue.add(lowercasedWord)

            if (queue.size >= WINDOW_SIZE) {
              // kWords = 1 by default
              //print counter = 0 by default
              PRINT_COUNTER += 1
              if (PRINT_COUNTER % KWORDS == 0){
                val topWords = queue.topFrequentWords(CLOUD_SIZE, MIN_FREQUENCY)
                println(s"Top words: $topWords") // Print the top frequent words
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




  