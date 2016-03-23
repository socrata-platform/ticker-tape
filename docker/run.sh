#!/bin/sh

set -ev

# TODO Ugly dependency for the Metrics directory that may or may not be in the base image
# Why is it ugly?
#   Coupled with underlying knowledge of a base image that may or may not change
#   Setting a Metrics directory argument in two places.
# [[ -x /bin/set_metrics_dir ]] && /bin/set_metrics_dir
[ -n "${METRICS_DIR}" ] && export BALBOA_FILE_DIR=$METRICS_DIR

# Assemble Java system properties
[ -n "${TICKER_TAPE_LOG4J_LEVEL}" ] && JAVA_OPTS="${JAVA_OPTS} -Dlog4j.logLevel=${TICKER_TAPE_LOG4J_LEVEL}"

# Run It!
exec sudo -Eu socrata /opt/docker/bin/tickertape
