## Spring validation check

#### Validation Error Message

> Phone validation error

```
{
  "timestamp": "2018-08-19T12:01:39.974+0000",
  "status": 400,
  "error": "Bad Request",
  "errors": [
    {
      "codes": [
        "Pattern.memberRequest.phoneNumber",
        "Pattern.phoneNumber",
        "Pattern.java.lang.String",
        "Pattern"
      ],
      "arguments": [
        {
          "codes": [
            "memberRequest.phoneNumber",
            "phoneNumber"
          ],
          "arguments": null,
          "defaultMessage": "phoneNumber",
          "code": "phoneNumber"
        },
        [],
        {
          "defaultMessage": "[0-9]{10,11}",
          "arguments": null,
          "codes": [
            "[0-9]{10,11}"
          ]
        }
      ],
      "defaultMessage": "Invalid phone format",
      "objectName": "memberRequest",
      "field": "phoneNumber",
      "rejectedValue": "010",
      "bindingFailure": false,
      "code": "Pattern"
    }
  ],
  "message": "Validation failed for object='memberRequest'. Error count: 1",
  "path": "/member"
}
```  

> Phone + Email(Blank)  

```
{
  "timestamp": "2018-08-19T12:02:32.164+0000",
  "status": 400,
  "error": "Bad Request",
  "errors": [
    {
      "codes": [
        "NotBlank.memberRequest.email",
        "NotBlank.email",
        "NotBlank.java.lang.String",
        "NotBlank"
      ],
      "arguments": [
        {
          "codes": [
            "memberRequest.email",
            "email"
          ],
          "arguments": null,
          "defaultMessage": "email",
          "code": "email"
        }
      ],
      "defaultMessage": "Must be not null email",
      "objectName": "memberRequest",
      "field": "email",
      "rejectedValue": "",
      "bindingFailure": false,
      "code": "NotBlank"
    },
    {
      "codes": [
        "Pattern.memberRequest.phoneNumber",
        "Pattern.phoneNumber",
        "Pattern.java.lang.String",
        "Pattern"
      ],
      "arguments": [
        {
          "codes": [
            "memberRequest.phoneNumber",
            "phoneNumber"
          ],
          "arguments": null,
          "defaultMessage": "phoneNumber",
          "code": "phoneNumber"
        },
        [],
        {
          "defaultMessage": "[0-9]{10,11}",
          "arguments": null,
          "codes": [
            "[0-9]{10,11}"
          ]
        }
      ],
      "defaultMessage": "Invalid phone format",
      "objectName": "memberRequest",
      "field": "phoneNumber",
      "rejectedValue": "010",
      "bindingFailure": false,
      "code": "Pattern"
    }
  ],
  "message": "Validation failed for object='memberRequest'. Error count: 2",
  "path": "/member"
}
```

> Custom Validation Error Message  

```
{
  "timestamp": "2018-08-19T11:59:40.315+0000",
  "status": 400,
  "error": "Bad Request",
  "message": "No message available",
  "path": "/member",
  "errors": [
    {
      "defaultMessage": "Already exist email.",
      "field": "email"
    }
  ]
}
```  


---  

## Ref  

- http://jojoldu.tistory.com/129
