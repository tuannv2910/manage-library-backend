package vn.banking.academy.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.banking.academy.dto.request.BookingRoomRequest;
import vn.banking.academy.service.RoomService;
import vn.banking.academy.utils.DateUtils;

import java.sql.Date;
import java.time.LocalDate;

@RequestMapping("/api/booking/room")
@RestController
@AllArgsConstructor
public class BookingRoomController {
    private final RoomService roomService;

    @GetMapping("/getRoom")
    public ResponseEntity<Object> getStatusRoomInDate(
            @RequestParam(name = "date") Date date
    ) {
        if (null == date)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        LocalDate currentDate = DateUtils.localDate();
        LocalDate convertCurrentDate = date.toLocalDate();
        // So sánh ngày hiện tại với thời gian được truyền vào
        if (convertCurrentDate.isBefore(currentDate)) {
            return new ResponseEntity<>("Ngày chọn phải sau hoặc bằng ngày hiện tại", HttpStatus.OK);
        }
        return new ResponseEntity<>(roomService.getRoomByDate(date), HttpStatus.OK);
    }

    @PostMapping("/checkout")
    public ResponseEntity<Object> checkOutBooking(@RequestBody BookingRoomRequest request) {
        return new ResponseEntity<>(roomService.checkOut(request), HttpStatus.OK);
    }

    @GetMapping("/detail")
    public ResponseEntity<Object> getBookingRoomDetails(
            @RequestParam(name = "booking_id") Integer bookingId
    ) {
        return new ResponseEntity<>(roomService.getDetail(bookingId), HttpStatus.OK);
    }

    @GetMapping("/cancel")
    public ResponseEntity<Object> acceptBookingRoom(
            @RequestParam(name = "booking_id") Integer bookingId
    ) {
        return new ResponseEntity<>(roomService.rejectBooking(bookingId), HttpStatus.OK);
    }

    @GetMapping("list-booked")
    public ResponseEntity<Object> getBookedRooms(
            @RequestParam(name = "user_id") String userId) {
        return new ResponseEntity<>(roomService.getRoomBookedByUserId(userId), HttpStatus.OK);
    }
}
