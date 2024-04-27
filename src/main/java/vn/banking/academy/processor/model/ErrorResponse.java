package vn.banking.academy.processor.model;

public class ErrorResponse {

    // Interface for custom error response
    public interface CustomErrorResponse {
        String getMessage();
        int getCode();
    }

    // Function to create a custom error response
    public static CustomErrorResponse errorResponse(int code, String message) {
        return new CustomErrorResponse() {
            @Override
            public String getMessage() {
                return message;
            }

            @Override
            public int getCode() {
                return code;
            }
        };
    }
}
