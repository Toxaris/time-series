# time-series

## Application

Object `timeseries.TimeSeries` contains a command-line
application to analyze a time series.

Computes number of data points, sum, minimum and maximum for the
data points in a rolling window.

The time complexity is independent of size of the sliding window
because the implementation is based on the following data
structure.

## Data structure

Object `timeseries.MinMax` contains a queue data structure with
with O(1) enqueue, O(1) access to minimum and maximum
values and amortized O(1) dequeue.

There are some basic unit tests for the data structure.
