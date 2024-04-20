package vn.banking.academy.service;

import vn.banking.academy.dto.request.BookingRoomRequest;
import vn.banking.academy.dto.response.RoomResponse;

import java.util.Date;
import java.util.List;

public interface RoomService {
    List<RoomResponse> getRoomByDate(Date date);

    Object checkOut(BookingRoomRequest request);

    Object getDetail(Integer id);

    Object acceptBooking(Integer id);
}
