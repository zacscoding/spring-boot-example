#!/usr/bin/env bash

SCRIPT_NAME=curl.sh
COMMAND=
ID=

function printHelp
{
  echo "${SCRIPT_NAME} [command] [args]"
  echo ""
}

function getPerson
{
  if [[ -z ${ID} ]]; then
    echo "invalid id : ${ID}"
    printHelp
    exit 1
  fi

  echo "curl -XGET localhost:8080/person/${ID}"
  curl -XGET localhost:8080/person/${ID}
}

## command check
if [[ -z ${1} ]]; then
  echo "Must have command"
  echo ""
  printHelp
  exit 1
fi


## parse command
case "${1}" in
  person | persons | save )
    COMMAND=${1}
    ;;
  *)
    echo "Invalid command ${1}"
    echo ""
    printHelp
    exit 1
esac
shift

## parse options
while [[ $# != 0 ]]; do
  case "${1}" in
    -h | --help )
      printHelp
      exit 0
      ;;
    -i )
      ID=${2}
      shift
      ;;
  esac
  shift
done


case ${COMMAND} in
  person)
  getPerson
  ;;
  persons)
  echo "request persons"
  ;;
  save)
  echo "request save"
  ;;
esac

#curl -XGET localhost:8080/person/${1}
