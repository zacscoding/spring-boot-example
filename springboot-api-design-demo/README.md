## Api server - client demo for design
; Version is no meanings because classified by design

#### Version1  

> ResponseDTO  

```aidl
package server.api.v1;

import lombok.Getter;

@Getter
public class ResponseDTOV1<T> {

    private ApiStatusV1 status;
    private T data;

    public static <T> ResponseDTOV1<T> createOK(T data) {
        return new ResponseDTOV1<>(new ApiStatusV1(ApiStatusCodeV1.OK.getErrorCode(), ApiStatusCodeV1.OK.getMessage()), data);
    }

    public static <T> ResponseDTOV1<T> createException(ApiStatusCodeV1 status) {
        return createException(status, status.getMessage());
    }

    public static <T> ResponseDTOV1<T> createException(ApiStatusCodeV1 code, String message) {
        return createException(code.getErrorCode(), message);
    }

    public static <T> ResponseDTOV1<T> createException(int errorCode, String message) {
        return new ResponseDTOV1<>(new ApiStatusV1(errorCode, message), null);
    }

    private ResponseDTOV1(ApiStatusV1 status, T data) {
        this.status = status;
        this.data = data;
    }
}
```  

> ApiStatus  

```aidl
package server.api.v1;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author zacconding
 * @Date 2018-12-04
 * @GitHub : https://github.com/zacscoding
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiStatusV1 {

    private int errorCode;
    private String message;
}
```  

> ApiStatusCode  

```aidl
package server.api.v1;

import lombok.Getter;

@Getter
public enum ApiStatusCodeV1 {

    OK(0, "")
    , BAD_REQUEST(400, "Bad Request")
    , UNAUTHORIZED(401, "Unauthorized")
    , FORBIDDEN(403, "Forbidden")
    , INTERNAL_SERVER_ERROR(500, "Internal Server error");

    private int errorCode;
    private String message;

    ApiStatusCodeV1(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
```  

> RestController  

```aidl
@ApiOperation(value = "ID값으로 Person 정보 가져오기")
@GetMapping(value = "{id}")
public ResponseDTOV1<Person> findPersonById(@ApiParam(name = "id", value = "person`s id") @PathVariable("id") String id) {
  log.info("find person by id. {}", id);
  Person person = personService.findOneById(id);

  if (person == null) {
    return ResponseDTOV1.createException(ApiStatusCodeV1.BAD_REQUEST, String.format("Not found person id : %s", id));
  }

  return ResponseDTOV1.createOK(person);
}
```  

#### Reference  

- https://github.com/jojoldu/bns  

---  
