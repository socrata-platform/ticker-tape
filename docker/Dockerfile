FROM socrata/java

### Environment Variables
ENV TICKER_TAPE_ROOT /srv/ticker-tape
ENV TICKER_TAPE_ARTIFACT ticker-tape-assembly.jar

RUN mkdir -p $TICKER_TAPE_ROOT

WORKDIR $TICKER_TAPE_ROOT

ADD ship.d /etc/ship.d

COPY $TICKER_TAPE_ARTIFACT $TICKER_TAPE_ROOT/
