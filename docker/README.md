Ticker Tape Docker
==================

Docker container and build for Ticker Tape.  Builds dockerized service for emitting metrics to a specific directory.

To build the image run:

  `docker build -rm -t ticker-tape <path/to/ticker-tape/root>/docker/`

## Required:

* TICKER_TAPE_DATA_DIRECTORY: The data directory to write metrics to.
IE: "/data/metrics" or "/tmp/metrics"

* TICKER_TAPE_SLEEP_TIME: Amount of time to sleep in milliseconds.  Can also be considered the period interval.
IE: 1000

* TICKER_TAPE_BATCH_SIZE: Number of metrics to emit every period.

## Building your container

  `docker build -rm -t ticker-tape <path/to/ticker-tape/root>/docker/`

## Running your container

Locally
`docker run --env-file=<full path to your envfile> -t ticker-tape `
