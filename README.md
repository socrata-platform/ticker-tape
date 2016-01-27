## Ticker Tape

Ticker Tape is a continuous Scala Application that emits metrics at a controlled batch size and sleep interval.

## Motivation

In order to test controlled metrics traffic.

## Build

```
# Build a docker image
sbt docker:publishLocal 
# To see your latest docker image
# docker images -f "label=com.socrata.ticker-tape"
 
# Stage a docker image
sbt docker:stage
# To see your latest staged files, look under the following path
# /target/docker/stage
```

## Tests

Currently relying on sbt test.

```
sbt test
```

## Contributors

[Socrata Inc](www.socrata.com)

## License

A short snippet describing the license (MIT, Apache, etc.)