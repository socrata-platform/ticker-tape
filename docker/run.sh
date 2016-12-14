#!/bin/sh

set -ev

# We currently depend on an environment variable set in the Socrata base image that identifies
# which directory to write metrics to.
# Reference: https://github.com/socrata/shipyard/blob/master/base/set_metrics_dir
[ -n "${METRICS_DIR}" ] && export BALBOA_FILE_DIR=$METRICS_DIR

# Assemble Java system properties
[ -n "${TICKER_TAPE_LOG4J_LEVEL}" ] && JAVA_OPTS="${JAVA_OPTS} -Dlog4j.logLevel=${TICKER_TAPE_LOG4J_LEVEL}"

# Run It!
exec sudo -Eu socrata /opt/docker/bin/ticker-tape
