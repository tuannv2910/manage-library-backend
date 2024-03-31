package vn.banking.academy.exception;

public enum SpringCode {
    DATE_FORMAT_INVALID(400, "Định dạng ngày tháng không hợp lê"),
    BAD_REQUEST(400, "Dữ liệu không hợp lệ!");

    SpringCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private Integer code;
    private String message;

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
