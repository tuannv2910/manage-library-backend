package vn.banking.academy.validator;

import vn.banking.academy.exception.SpringCode;
import vn.banking.academy.exception.SpringException;
import vn.banking.academy.utils.DateUtils;
import vn.banking.academy.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * @author myname
 */
public class ValidatorUtils {
    private ValidatorUtils() {
        super();
    }

    //
    private static final String NOT_BLANK = "Không được để trống ";
    private static final String INVALID = " không hợp lệ";
    private static final String MUST_LENGTH_LESS = " phải ít hơn ";
    private static final String MUST_LENGTH_MORE = " phải nhiều hơn ";
    private static final String CHARACTER = " ký tự";
    private static final String MUST_MORE = " phải lớn hơn ";
    private static final String MUST_LESS = " phải nhỏ hơn ";

    public static void validNullOrEmpty(String fieldName, String value) {
        if (StringUtils.isNullOrEmpty(value)) {
            throw new SpringException(SpringCode.BAD_REQUEST, NOT_BLANK + fieldName);
        }
    }

    public static void validNullOrEmpty(String fieldName, List<String> values) {
        if (values == null || values.isEmpty()) {
            throw new SpringException
                    (
                            SpringCode.BAD_REQUEST, NOT_BLANK + fieldName);
        }
        for (String value : values) {
            validNullOrEmpty(fieldName, value);
        }
    }

    public static void validNullOrEmptyList(String fieldName, List<Object> values) {
        if (values == null || values.isEmpty()) {
            throw new SpringException
                    (
                            SpringCode.BAD_REQUEST, NOT_BLANK + fieldName);
        }
    }

    public static void validOnlyCharacterAndNumber(String fieldName, String value) {
        if (!StringUtils.isOnlyCharacterAndNumber(value)) {
            throw new SpringException(SpringCode.BAD_REQUEST,fieldName + INVALID);
        }
    }

    public static void validOnlyCharacterAndNumber(String fieldName, List<String> values) {
        if (values.isEmpty()) {
            return;
        }
        for (String value : values) {
            if (!StringUtils.isOnlyCharacterAndNumber(value)) {
                fieldName = fieldName.charAt(0) + fieldName.substring(1);
                throw new SpringException(SpringCode.BAD_REQUEST,fieldName + INVALID);
            }
        }
    }
//

    /**
     * valid do dai
     * type = true => valid max length
     */
    public static void validLength(String fieldName, String value, int length, boolean type) {
        if (type && value.length() > length) {
            throw new SpringException
                    (
                            SpringCode.BAD_REQUEST, fieldName + MUST_LENGTH_LESS + length + CHARACTER);
        }

        if (!type && value.length() < length) {
            throw new SpringException
                    (
                            SpringCode.BAD_REQUEST, fieldName + MUST_LENGTH_MORE + length + CHARACTER);
        }
    }

    public static void validLength(String fieldName, String value, int minLength, int maxLength) {
        if (value.length() < minLength) {
            throw new SpringException
                    (
                            SpringCode.BAD_REQUEST, fieldName + MUST_LENGTH_MORE + minLength + CHARACTER);
        }
        if (value.length() > maxLength) {
            throw new SpringException
                    (
                            SpringCode.BAD_REQUEST, fieldName + MUST_LENGTH_LESS + maxLength + CHARACTER);
        }
    }

    public static void validLength(String fieldName, List<String> values, int minLength, int maxLength) {
        if (values == null || values.isEmpty()) {
            return;
        }
        for (String value : values) {
            if (value.length() < minLength) {
                throw new SpringException
                        (
                                SpringCode.BAD_REQUEST, fieldName + MUST_LENGTH_MORE + minLength + CHARACTER);
            }
            if (value.length() > maxLength) {
                throw new SpringException
                        (
                                SpringCode.BAD_REQUEST, fieldName + MUST_LENGTH_LESS + maxLength + CHARACTER);
            }
        }
    }

    public static void validOnlyNumber(String fieldName, String value) {
        if (!StringUtils.isOnlyNumber(value)) {
            throw new SpringException
                    (
                            SpringCode.BAD_REQUEST, fieldName + INVALID);
        }
    }

    public static void validPrice(String fieldName, String value) {
        try {
            if (value.startsWith("0") || value.contains(" ")) {
                throw new SpringException
                        (
                                SpringCode.BAD_REQUEST, fieldName + INVALID);
            }
            Long price = Long.valueOf(value);
            if (price < 0) {
                throw new SpringException
                        (
                                SpringCode.BAD_REQUEST, fieldName + INVALID);
            }
        } catch (NumberFormatException e) {
            throw new SpringException
                    (
                            SpringCode.BAD_REQUEST, fieldName + INVALID);
        }
    }

    public static void validOnlyCharacter(String fieldName, String value) {
        if (!StringUtils.isOnlyCharacter(value)) {
            throw new SpringException
                    (
                            SpringCode.BAD_REQUEST, fieldName + INVALID);
        }
    }

    public static void validEmail(String fieldName, String value) {
        if (!StringUtils.isValidEmail(value)) {
            throw new SpringException
                    (
                            SpringCode.BAD_REQUEST, fieldName + INVALID);
        }
    }

    public static void validPhone(String fieldName, String value) {
        if (!StringUtils.isCheck(value, StringUtils.PHONE_NUMBER_REGEX)) {
            throw new SpringException
                    (
                            SpringCode.BAD_REQUEST, fieldName + INVALID);
        }
    }


    public static void validBooleanType(String fieldName, Boolean value) {
        if (value == null) {
            throw new SpringException
                    (
                            SpringCode.BAD_REQUEST, fieldName + INVALID);
        }
    }

    public static void validLongValueMustBeMore(String fieldName, String value, Long minValue) {
        Long percent = Long.valueOf(value);
        if (percent < minValue) {
            throw new SpringException
                    (
                            SpringCode.BAD_REQUEST, fieldName + MUST_MORE + minValue);
        }
    }

    public static void validDateFormat(String value) {
        try {
            DateTimeFormatter sdf = DateTimeFormatter.ofPattern(DateUtils.F_DDMMYYYY);
            LocalDate localDate = LocalDate.parse(value, sdf);
        } catch (Exception e) {
            throw new SpringException(SpringCode.DATE_FORMAT_INVALID);
        }
    }
}
