package vn.banking.academy.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpStatusCodeException;

public class SpringException extends HttpStatusCodeException {
    public SpringException(HttpStatusCode statusCode, String statusText) {
        super(statusCode, statusText);
    }

    public SpringException(SpringCode code) {
        super(HttpStatusCode.valueOf(code.getCode()), code.getMessage());
    }

    public SpringException(Integer statusCode, String statusText) {
        super(HttpStatusCode.valueOf(statusCode), statusText);
    }

    public SpringException(SpringCode code, String statusText) {
        super(HttpStatusCode.valueOf(code.getCode()), statusText);
    }
}
