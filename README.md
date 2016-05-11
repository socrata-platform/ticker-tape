## Ticker Tape

Ticker Tape is a continuous Scala Application that emits metrics at a
configurable batch size and sleep interval.

## Motivation

This tool was designed to help understand whether a
[Balboa](https://github.com/socrata-platform/balboa) system is behaving
improperly.  Ticker tape is able to emit metrics at a fixed frequency.  This
allows us to monitor whether metrics are being processed throughout the entire
system.

## Usage

### As a Command Line Tool

Build a fat jar.

```
sbt assembly
```

ticker-tape basically writes valid metrics files to a directory, expecting
balboa-agent to pick them up. You can use it to write metric files locally for
testing, or on a destination machine. You can write the metrics files locally
and copy them to the destination machine, or copy ticker-tape and run it there.

To run ticker-tape on a remote machine:

```
scp target/scala-2.11/ticker-tape-assembly-*.jar 10.110.43.71:.
ssh 10.110.43.71
sudo BALBOA_FILE_DIR=/data/metrics TICKER_TAPE_METRIC_ENTITY_ID=ticker-tape-metric-2016-05-01 java -jar ticker-tape-assembly-*.jar
```

Note, the /data/metrics directory is protected, so you must sudo to write
metrics files to it.

ticker-tape will write metrics to a file, and when ticker-tape ends (by Ctrl-C
or kill, not including kill -9), it will close the metrics file and rename it
to .completed, indicating to balboa-agent that no more will be added to the
file and balboa-agent can begin consuming the file. If the metrics file does
not get renamed, balboa-agent will wait until another file appears in the same
directory before beginning to consume the metrics file.

If you write a unique name for the TICKER_TAPE_METRIC_ENTITY_ID, you don't have
to worry about confusing the metrics from your current test run with some other
test run.

To verify the metrics written by ticker-tape have arrived in the metrics store,
query the balboa-http API for the entity id you wrote:

```
curl 'http://balboa.app.aws-us-west-2-staging.socrata.net/metrics/ticker-tape-metric-2016-05-01/range?start=2010-01-01+00%3A00%3A00%3A000&end=2017-01-01+00%3A00%3A00%3A000'
```

### As a Docker Image

```
# Run docker container that emits to docker volume
$ scripts/ticker-tape-stack.sh up ticker-tape-file
# Run docker container that emits to Activemq
$ scripts/ticker-tape-stack.sh up ticker-tape-jms
```

## Configuration

Configuration is done through environment variables when ticker-tape is run as
a docker container.  See [docker-compose](docker-compose.yml) Otherwise
Ticker-tape uses
[HOCON](https://github.com/typesafehub/config/blob/master/HOCON.md)
configuration provided by [Typesafe
Config](https://github.com/typesafehub/config).  Please refer to the [reference
configuration file](src/main/resources/reference.conf) as an example of the
format and description of the possible keys.  Please refer to
[application.conf](src/main/resources/application.conf) as a reference for the
appropriate environment variables.

## Build

```
# Compile
$ sbt compile
```

Ticker tape supports Debian, Tar GZ, and docker packaging via [SBT Native
Packager](http://www.scala-sbt.org/sbt-native-packager/).

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
