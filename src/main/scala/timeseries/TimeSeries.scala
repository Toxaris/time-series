// Copyright 2016 Tillmann Rendel.
// All Rights Reserved.

package timeseries

import java.util.Locale

/** Command-line application to analyze a time series.
  *
  * Computes number of data points, sum, minimum and maximum for
  * the data points in a rolling window.
  *
  * Reads input time series from file.
  * Prints output time series to stdout.
  *
  * Expects one command-line argument, the name of the file to
  * read input time series from.
  *
  * Input example:
  *
  * {{{
  * 1355270609 1.80215
  * 1355270621 1.80185
  * 1355270646 1.80195
  * 1355270702 1.80225
  * }}}
  *
  * Output example:
  *
  * {{{
  * T          V       N RS      MinV    MaxV
  * --------------------------------------------
  * 1355270609 1.80215 1 1.80215 1.80215 1.80215
  * 1355270621 1.80185 2 3.60400 1.80185 1.80215
  * 1355270646 1.80195 3 5.40595 1.80185 1.80215
  * 1355270702 1.80225 2 3.60420 1.80195 1.80225
  * }}}
  */
object TimeSeries {
  /** Size of the rolling window (in seconds) */
  val windowsize = 60

  /** Main entry point */
  def main(args: Array[String]) {
    // ensure floating-point values are written with dots
    Locale.setDefault(Locale.US)

    // input
    val source = io.Source.fromFile(args(0))

    // state
    val queue = MinMax.Queue()
    var sum = 0.0
    var size = 0

    // print header
    println("T          V       N RS      MinV    MaxV")
    println("--------------------------------------------")

    // for splitting
    val whitespace = "\\s".r

    // processing
    for (line <- source.getLines) {
      // parse data point
      val parts = whitespace.split(line)
      val timestamp = parts(0).toLong
      val ratio = parts(1).toDouble

      // process data points that fall out of the window
      while (!queue.isEmpty && timestamp - queue.nextKey > windowsize) {
        size -= 1
        sum -= queue.nextValue
        queue.dequeue()
      }

      // process new data point
      queue.enqueue(timestamp, ratio)
      size += 1
      sum += ratio

      // print data point
      val min = queue.min
      val max = queue.max
      println(f"$timestamp%10d $ratio%.5f $size%1d $sum%.5f $min%.5f $max%.5f")
    }
  }
}
