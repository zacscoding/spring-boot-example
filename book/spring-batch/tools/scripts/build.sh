#!/usr/bin/env bash

SCRIPT_PATH=$( cd "$(dirname "$0")" ; pwd -P )
MODULE_NAME=

for opt in "$@"
do
    case "$opt" in
        ch2)
            MODULE_NAME=Chapter02
            ;;
        ch3)
            MODULE_NAME=Chapter03
            ;;
        *)
            echo $"Usage: $0 {ch2|ch3}"
            exit 1
esac
done

cd ${SCRIPT_PATH}/../../ && ./mvnw clean install -pl ${MODULE_NAME}