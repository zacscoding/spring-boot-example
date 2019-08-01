#!/usr/bin/env bash

SCRIPT_NAME="prototype.sh"
URL="http://localhost:8080"
COMMAND=
ID=
PORT=8080

function printHelp() {
  echo "Test for prototype."
  echo ""
  echo "commands :"
  echo "  get       getting bean all or one"
  echo "    e.g : ${SCRIPT_NAME} get 1 or ${SCRIPT_NAME} get"
  echo "  getctx    getting bean all from context"
  echo "    e.g : ${SCRIPT_NAME} getctx"
  echo "  create    create prototype bean"
  echo "    e.g : ${SCRIPT_NAME} create "
  echo "  delete    destroy prototype bean"
  echo "    e.g : ${SCRIPT_NAME} delete 1 "
  echo ""
}

function get() {
  if [[ -z ${ID} ]]; then
    curl -X GET ${URL}/prototypes
  else
    curl -X GET ${URL}/prototype/${ID}
  fi
  echo ""
}

function getctx() {
  curl -X GET -H "Content-Type: application/json; charset=utf-8" ${URL}/prototypes/ctx
  echo ""
}

function create() {
  curl -X POST -H "Content-Type: application/json; charset=utf-8" ${URL}/prototype
  echo ""
}

function delete() {
  curl -X DELETE ${URL}/prototype/${ID}
  echo ""
}

# command
case "${1}" in
  get | getctx | create | delete )
    COMMAND=${1}
    ;;
  * )
    printHelp
    exit 1
    ;;
esac

case "${COMMAND}" in
  get )
    ID=${2}
    get
  ;;
  getctx )
    getctx
  ;;
  create )
    create
  ;;
  delete )
    ID=${2}
    delete
  ;;
esac
