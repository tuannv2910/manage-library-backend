package vn.banking.academy.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.banking.academy.dto.request.BookingRoomRequest;
import vn.banking.academy.service.RoomService;

import java.sql.Date;

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

    @GetMapping("/accept")
    public ResponseEntity<Object> acceptBookingRoom(
            @RequestParam(name = "booking_id") Integer bookingId
    ) {
        return new ResponseEntity<>(roomService.acceptBooking(bookingId), HttpStatus.OK);
    }
}
