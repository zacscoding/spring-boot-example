# Command line with picocli  

There are two command `MemberCommand` and `BookCommand`.
And each Command has only one sub command AddCommand.  


> Getting started  

```curl
curl --location --request POST 'http://localhost:8080/command' \
--header 'Content-Type: application/json' \
--data-raw '{
	"commandLine" : "member add -n memberA --age 15 -hb coding -hb movie"
}'
```