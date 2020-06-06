#!/usr/bin/env bash

# This cli is copied from https://woowabros.github.io/study/2019/06/05/spring-data-dynamodb-1.html
SCRIPT_PATH=$( cd "$(dirname "$0")" ; pwd -P )

echo "##################################################################################"
echo "Create table / Create item / Get item / Update item / Delete item / Drop table"
echo "##################################################################################"
echo ""

compose_up() {
  echo "Start dynamodb with docker-compose"
  cd ${SCRIPT_PATH}/../docker && docker-compose up --force-recreate -d
}

compose_down() {
  echo "Stop dynamodb"
  cd ${SCRIPT_PATH}/../docker && docker-compose down
}

create_table() {
  echo "#############################################"
  echo "Create table"
  echo "#############################################"
  aws dynamodb create-table \
    --endpoint-url http://localhost:8000 \
    --table-name Comment \
    --attribute-definitions \
    AttributeName=id,AttributeType=S \
    AttributeName=mentionId,AttributeType=N \
    AttributeName=createdAt,AttributeType=S \
    --key-schema AttributeName=id,KeyType=HASH \
    --provisioned-throughput ReadCapacityUnits=1,WriteCapacityUnits=1 \
    --global-secondary-indexes IndexName=byMentionId,KeySchema=["{\
  AttributeName=mentionId,\
  KeyType=HASH\
}","{\
  AttributeName=createdAt,\
  KeyType=RANGE\
}"],Projection="{ProjectionType=ALL}",ProvisionedThroughput="{\
  ReadCapacityUnits=1,\
  WriteCapacityUnits=1\
}"
}

function create_item() {
  echo "#############################################"
  echo "Create item"
  echo "#############################################"
  aws dynamodb put-item \
    --endpoint-url http://localhost:8000 \
    --table-name Comment \
    --item '{
        "id": {"S": "uuid"},
        "name": {"S": "comment name"},
        "mentionId": {"N": "1"},
        "content" : {"S": "comment content"},
        "deleted" : {"BOOL": false},
        "createdAt": {"S": "1836-03-07T02:21:30.536Z"}
    }'
}

function get_item() {
  echo "#############################################"
  echo "Get item"
  echo "#############################################"
  aws dynamodb get-item \
    --endpoint-url http://localhost:8000 \
    --table-name Comment \
    --key '{"id":{"S":"uuid"}}'
}

function update_item() {
  echo "#############################################"
  echo "Update item"
  echo "#############################################"
  aws dynamodb put-item \
    --endpoint-url http://localhost:8000 \
    --table-name Comment \
    --item '{
        "id": {"S": "uuid"},
        "name": {"S": "name"},
        "mentionId": {"N": "1"},
        "content" : {"S": "modified comment content"},
        "deleted" : {"BOOL": false},
        "createdAt": {"S": "1836-03-07T02:21:30.536Z"}
    }'
  get_item
}

function delete_item() {
  echo "#############################################"
  echo "Delete item"
  echo "#############################################"
  aws dynamodb delete-item \
    --endpoint-url http://localhost:8000 \
    --table-name Comment \
    --key '{"id":{"S":"uuid"}}'
}

function drop_table() {
  echo "#############################################"
  echo "Drop table"
  echo "#############################################"
  aws dynamodb delete-table \
    --endpoint-url http://localhost:8000 \
    --table-name Comment
}

compose_up
sleep 1s
create_table
create_item
get_item
update_item
delete_item
drop_table
compose_down