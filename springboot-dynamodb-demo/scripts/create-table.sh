#!/usr/bin/env bash

set -x

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
