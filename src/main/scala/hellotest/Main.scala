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

class CircularQueue(val capacity: Int, val queue: List[String] = List(), val wordCount: Map[String, Int] = Map().withDefaultValue(0)) {
  def add(elements: List[String]): CircularQueue = {
    val finalState = elements.iterator.scanLeft((queue, wordCount)) {
      case ((currentQueue, currentWordCount), element) =>
        val (newQueue, newWordCount) = if (currentQueue.size >= capacity) {
          val removed = currentQueue.head
          val updatedQueue = currentQueue.tail
          val updatedWordCount = currentWordCount.updated(removed, currentWordCount(removed) - 1)
          val finalWordCount = if (updatedWordCount(removed) == 0) updatedWordCount - removed else updatedWordCount
          (updatedQueue, finalWordCount)
        } else {
          (currentQueue, currentWordCount)
        }

        val updatedQueue = newQueue :+ element
        val updatedWordCount = newWordCount.updated(element, newWordCount(element) + 1)

        (updatedQueue, updatedWordCount)
    }.toList.last

    new CircularQueue(capacity, finalState._1, finalState._2)
  }

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

    val lines = Source.stdin.getLines
    val words = lines.flatMap(line => line.split("(?U)[^\\p{Alpha}0-9']+"))

    
    def processWords(queue: CircularQueue, wordStream: Iterator[String]): Unit = {
      if (wordStream.hasNext) {
        val word = wordStream.next()
        if (word.length >= minLength) {
          val lowercasedWord = word.toLowerCase

          // Get the new queue after adding the word
          val updatedQueue = queue.add(List(lowercasedWord))

          if (updatedQueue.size >= windowSize) {
            PRINT_COUNTER += 1
            if (PRINT_COUNTER % kSteps == 0) {
              val topWords = updatedQueue.topFrequentWords(cloudSize, minFrequency)
              println(s"Top words: $topWords")
            }
          }

          
          processWords(updatedQueue, wordStream)
        } else {
          
          processWords(queue, wordStream)
        }
      }
    }

    // Start the recursive word processing with an empty CircularQueue
    processWords(new CircularQueue(windowSize), words)
  }
}



