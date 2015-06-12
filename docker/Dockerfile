FROM socrata/java

### Map Docker environment variables to Application configuration.
ENV TICKER_TAPE_DATA_DIRECTORY $METRICS_DIR

### Environment Variables
ENV TICKER_TAPE_ROOT /srv/ticker-tape
ENV TICKER_TAPE_ARTIFACT ticker-tape-assembly.jar
ENV TICKER_TAPE_CONFIG application.conf
ENV TICKER_TAPE_LOG4J log4j.properties

RUN mkdir -p $TICKER_TAPE_ROOT

WORKDIR $TICKER_TAPE_ROOT

ADD ship.d /etc/ship.d

COPY $TICKER_TAPE_CONFIG $TICKER_TAPE_ROOT/
COPY $TICKER_TAPE_LOG4J $TICKER_TAPE_ROOT/
COPY $TICKER_TAPE_ARTIFACT $TICKER_TAPE_ROOT/
