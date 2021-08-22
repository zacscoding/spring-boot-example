#!/usr/bin/env bash

SCRIPT_PATH=$( cd "$(dirname "$0")" ; pwd -P )

if [[ ! -e "${SCRIPT_PATH}/../compose/docker-compose.yaml" ]];then
  echo "docker-compose.yaml not found."
  exit 1
fi

function clean(){
  cd "${SCRIPT_PATH}"/../compose && docker-compose  -f docker-compose.yaml down -v
}

function build(){
  cd "${SCRIPT_PATH}"/../compose && docker-compose build
}

function up(){
  cd "${SCRIPT_PATH}"/../compose && docker-compose up --force-recreate
}

function down(){
  cd "${SCRIPT_PATH}"/../compose && docker-compose down -v
}

for opt in "$@"
do
    case "$opt" in
        up)
            up
            ;;
        build)
            build
            ;;
        down)
            down
            ;;
        stop)
            down
            ;;
        start)
            up
            ;;
        clean)
            clean
            ;;
        restart)
            down
            clean
            up
            ;;
        *)
            echo $"Usage: $0 {up|down|build|start|stop|clean|restart}"
            exit 1

esac
done