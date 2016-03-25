## Ticker Tape

Ticker Tape is a continuous Scala Application that emits metrics at a configurable batch size and sleep interval.

## Motivation

This tool was designed to help understand whether a [Balboa](https://github.com/socrata-platform/balboa) system is 
behaving improperly.  Ticker tape is able to emit metrics at a fixed frequency.  This allows us to monitor whether 
metrics are being processed throughout the entire system.

## Usage

```
# Run docker container that emits to docker volume
$ scripts/ticker-tape-stack.sh up ticker-tape-file
# Run docker container that emits to Activemq
$ scripts/ticker-tape-stack.sh up ticker-tape-jms
```

## Configuration

Configuration is done through environment variables when ticker-tape is run as a docker container.  See 
[docker-compose](docker-compose.yml) Otherwise Ticker-tape uses 
[HOCON](https://github.com/typesafehub/config/blob/master/HOCON.md) configuration provided by 
[Typesafe Config](https://github.com/typesafehub/config).  Please refer to the 
[reference configuration file](src/main/resources/reference.conf) as an example of the format and description of the 
possible keys.  Please refer to [application.conf](src/main/resources/application.conf) as a reference for the appropriate 
environment variables.

## Build

```
# Compile
$ sbt compile
```

Ticker tape supports Debian, Tar GZ, and docker packaging via 
[SBT Native Packager](http://www.scala-sbt.org/sbt-native-packager/).  
```
# Package debian, Linux only
$ sbt debian:packageBin
# Package *.xz
$ sbt universal:packageXzTarball
# Build and push docker to local docker daemon
$ sbt docker:publishLocal
```

## Tests

```
$ sbt test
```

## Contributors

[Socrata Inc](www.socrata.com)

## License
