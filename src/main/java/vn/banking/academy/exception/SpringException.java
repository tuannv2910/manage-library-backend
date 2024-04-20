package vn.banking.academy.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class SpringException extends HttpStatusCodeException {
    public SpringException(HttpStatus statusCode, String statusText) {
        super(statusCode, statusText);
    }

    public SpringException(SpringCode code) {
        super(HttpStatus.valueOf(code.getCode()), code.getMessage());
    }

    public SpringException(Integer statusCode, String statusText) {
        super(HttpStatus.valueOf(statusCode), statusText);
    }

    public SpringException(SpringCode code, String statusText) {
        super(HttpStatus.valueOf(code.getCode()), statusText);
    }
}
