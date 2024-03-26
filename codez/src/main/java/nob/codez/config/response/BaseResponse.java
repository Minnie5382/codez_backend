package nob.codez.config.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static nob.codez.config.response.BaseResponseStatus.SUCCESS;


@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "message", "result"})
public class BaseResponse<T> {
    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    @JsonIgnore
    @Builder.Default
    private HttpStatusCode statusCode = HttpStatus.OK;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    // 요청에 성공한 경우
    public BaseResponse(T result) {
        this.isSuccess = SUCCESS.isSuccess();
        this.message = SUCCESS.getMessage();
        this.result = result;
        this.statusCode = HttpStatus.OK;
    }

    // 요청에 실패한 경우
    public BaseResponse(BaseResponseStatus status) {
        this.isSuccess = status.isSuccess();
        this.statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        this.message = status.getMessage();
    }

    public BaseResponse(BaseResponseStatus status, HttpStatusCode statusCode) {
        this.isSuccess = status.isSuccess();
        this.statusCode = statusCode;
        this.message = status.getMessage();
    }

    // HTTP 응답으로 변환
    public ResponseEntity<BaseResponse<?>> toResponseEntity() {
        return new ResponseEntity<>(this, statusCode);
    }

}