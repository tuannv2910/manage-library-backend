package vn.banking.academy.validator;

import vn.banking.academy.dto.request.BookingRoomRequest;

public class BookingRequestValidator {

    public static void validate(BookingRoomRequest bookingRoomRequest) {
        ValidatorUtils.validDateFormat(bookingRoomRequest.getDateBook().toString());


    }
}
