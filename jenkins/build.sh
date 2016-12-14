#!/bin/bash

# Ticker Tape build file.
# TODO Technically technical debt because it builds docker images with a different
# set of commands.  But seemed appropriate to remove Mission Control on our
# own build pipeline.

set -e

# Log the author name and email of the last commit
echo "Change committed by: $(git log HEAD -1 --pretty=format:"%an (%ae)")"

echo "1. Set up the environment"

DOCKER_REGISTRY_SYNC=/usr/local/bin/docker-registry-sync

SHA=`git rev-parse HEAD | cut -c1-8`

REGISTRY=registry.docker.aws-us-west-2-infrastructure.socrata.net:5000

if [ "${WORKSPACE}" != "" ]; then
  WORK_DIR=${WORKSPACE}
else
  WORK_DIR="."
fi
PROPERTIES_FILES_ROOT_PATH=${WORK_DIR}

if [ -z "${SERVICE_BUILD_NUMBER}" ]; then
  export SERVICE_BUILD_NUMBER="${BUILD_NUMBER}" # we prefer to use the build number from the service build,
fi                        # but if not, we will use the dockerize build number

if [ -z "${SERVICE_NAME_PATTERN}" ]; then
    export SERVICE_NAME_PATTERN="${SERVICE}"
fi

if [ "${MARATHON_DEPLOY_ENVIRONMENT}" != "" ]; then
  DEPLOY_ENVIRONMENT=${MARATHON_DEPLOY_ENVIRONMENT}
else
  DEPLOY_ENVIRONMENT=staging
fi

SERVICE=ticker-tape
SERVICE_VERSION=`cat ${WORK_DIR}/version.sbt | grep -o '\".*\"' | sed 's/\"//g'`

if [ "${OVERRIDE_TAG}" ]; then
    TAG="${OVERRIDE_TAG}"
else
    TAG=${SERVICE_VERSION}_${SERVICE_BUILD_NUMBER}_${SHA}
fi

echo "2. build the image and publish to the local docker daemon"

existing_images=$(docker images -f "label=com.socrata.ticker-tape" -q | sort | uniq)
if [ "${existing_images}" != "" ]; then
  echo "2a. Remove all existing images"
  docker rmi -f $existing_images
fi

echo "2b. Publish to the local docker daemon."
sbt docker:publishLocal

new_image=$(docker images -f "label=com.socrata.ticker-tape" -q | head -n1)
echo "New Image: $new_image Registry: ${REGISTRY} Service: ${SERVICE} Tag: ${TAG}"

set -x

docker tag "${new_image}" "${REGISTRY}/internal/${SERVICE}:${TAG}"
docker push ${REGISTRY}/internal/${SERVICE}:${TAG}
docker rmi -f ${REGISTRY}/internal/${SERVICE}:${TAG}
if [ -x ${DOCKER_REGISTRY_SYNC} ]; then
    ${DOCKER_REGISTRY_SYNC} -s "$DOCKER_REGISTRY_SOURCE" -t "$DOCKER_REGISTRY_TARGETS" -q "$DOCKER_REGISTRY_QUEUE" queue-sync internal/${SERVICE}:${TAG}
fi

# Push a latest image for development
docker tag ${new_image} ${REGISTRY}/internal/${SERVICE}:latest
docker push ${REGISTRY}/internal/${SERVICE}:latest
docker rmi ${REGISTRY}/internal/${SERVICE}:latest

echo "3. Set up marathon deploy job"

echo "Properties files will be saved in ${PROPERTIES_FILES_ROOT_PATH}..."

MARATHON_PROPERTIES_FILE=${PROPERTIES_FILES_ROOT_PATH}/marathon.properties

rm -f ${MARATHON_PROPERTIES_FILE}
if [ "${DEPLOY_ENVIRONMENT}" != "" ]; then
  echo "Writing ${MARATHON_PROPERTIES_FILE}..."
  touch ${MARATHON_PROPERTIES_FILE}
  echo "DOCKER_IMAGE=${REGISTRY}/internal/${SERVICE}:${TAG}" >> ${MARATHON_PROPERTIES_FILE}
  echo "SERVICE=${SERVICE}" >> ${MARATHON_PROPERTIES_FILE}
  echo "DOCKER_JOB=${SERVICE}" >> ${MARATHON_PROPERTIES_FILE}
  echo "ENVIRONMENT=${DEPLOY_ENVIRONMENT}" >> ${MARATHON_PROPERTIES_FILE}
fi
