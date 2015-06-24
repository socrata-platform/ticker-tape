## Ticker Tape

Ticker Tape is a continuous Scala Application that emits metrics at a controlled batch size and sleep interval.

## Motivation

In order to test controlled metrics traffic.

## Installation

N/A

## Build

Provide an example of how to build this project.

```
sbt assembly
cp `ls -1t target/scala-2.10/*.jar | sed -n 1p` docker/ticker-tape-assembly.jar
docker build --rm -t ticker-tape docker/
# or
make docker
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