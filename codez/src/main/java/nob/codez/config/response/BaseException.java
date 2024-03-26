package nob.codez.config.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nob.codez.config.response.BaseResponseStatus;

@Getter
@AllArgsConstructor
public class BaseException extends Exception {
    private BaseResponseStatus status;
}