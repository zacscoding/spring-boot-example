package server.api.v1;

/**
 * @author zacconding
 * @Date 2018-11-30
 * @GitHub : https://github.com/zacscoding
 */
public class ApiV1Response<T> {

    public static final ApiV1Response<String> DEFAULT_OK = new ApiV1Response<>(ApiV1ResponseCode.OK);

    private ApiV1ResponseCode code;
    private String message;
    private T data;


    private ApiV1Response(ApiV1ResponseCode code) {
        this(code, null);
    }

    private ApiV1Response(ApiV1ResponseCode code, T data) {
        this(code, code.getMessage(), data);
    }

    private ApiV1Response(ApiV1ResponseCode code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiV1Response createOK(T data) {
        return new ApiV1Response(ApiV1ResponseCode.OK, data);
    }


    /*
    public static ResponseDto<String> createException(@Nonnull BnsAdminException e) {
        return new ResponseDto<>(e.getStatus(), e.getMessage(), "");
    }

    public static ResponseDto<String> createException(@Nonnull ResponseCode code) {
        return new ResponseDto<>(code, code.getMessage(), "");
    }

    public static ResponseDto<String> createException(@Nonnull ResponseCode code, @Nonnull String message) {
        return new ResponseDto<>(code, message, "");
    }

    public static <T> ResponseDto<T> createException(@Nonnull ResponseCode code, @Nonnull T data) {
        return new ResponseDto<>(code, data);
    }
    */


}
