#!/usr/bin/env bash

# This script is intended to be a small wrapper around docker compose
# It does the following:
#   1. Verifies that docker-compose is an available application
#   2. Composes the database migrations into target/it/docker/migrations
#   3. Stages the Dockerfile and resources
#   4. Run the docker-compose [CMD]
#

command -v docker-compose >/dev/null 2>&1 || { echo >&2 "I require docker-compose but it's not installed.  Aborting."; exit 1; }

# A reference to the directory of this script.
BASE_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

function down() {
    echo
    # Cleanup by stopping all containers and removing all docker images
    echo "Shutting down Ticker Tape Stack. ($( docker-compose -f ${BASE_DIR}/../docker-compose.yml down --rmi all))"
    exit
}

function usage() {
    echo
    echo "Usage: ticker-tape-stack.sh [-h|up|down]"
    echo "  -h      : Print usage"
    echo "  up      : Stand up Ticker Tape environment.  Synonymous for \"docker-compose up -d\""
    echo "  down    : Stand up Ticker Tape environment.  Synonymous for \"docker-compose stop && docker-compose rm -f\""
    echo
}

function up() {
    echo
    echo "Staging Docker related files"
    if ! sbt docker:stage ; then # 3.
        echo "Error staging the docker image for the service."
        exit 1
    fi
    echo "Running docker-compose up"
    # Always recreate the docker images and stand up all containers in the background.
    docker-compose -f ${BASE_DIR}/../docker-compose.yml up -d --force-recreate ${UP_SUB_COMMAND}
    echo
}

# Set up to handle multiple command line arguments
while [[ $# > 0 ]]
do
key="$1"

case $key in
    -h|--help)
    usage
    exit
    ;;
    up)
    UP_SUB_COMMAND="$2"
    shift
    up
    ;;
    down)
    down
    ;;
    *)
        # unknown option
        usage && exit
    ;;
esac
shift # past argument or value
done
