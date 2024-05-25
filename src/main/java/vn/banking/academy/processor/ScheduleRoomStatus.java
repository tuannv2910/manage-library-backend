package vn.banking.academy.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import vn.banking.academy.entity.RoomBooking;
import vn.banking.academy.entity.eums.BookingStatus;
import vn.banking.academy.repository.RoomBookingRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Component
public class ScheduleRoomStatus {
    @Autowired
    RoomBookingRepository roomBookingRepository;


    @Scheduled(fixedDelay = 1000 * 60 * 15)
    public void scheduleTaskWithFixedDelay() {
        List<RoomBooking> bookingPending = roomBookingRepository.getAllRoomBookingByStatus();
        for (RoomBooking roomBooking : bookingPending) {
            // Thời điểm hiện tại
            LocalDateTime currentDateTime = LocalDateTime.now();
            // Chuyển đổi java.util.Date sang java.time.LocalDateTime
            LocalDateTime providedDateTime = roomBooking.getCreatedDate().toLocalDateTime();
            if (currentDateTime.isAfter(providedDateTime.plusMinutes(15))) {
                roomBooking.setStatus(BookingStatus.EXPIRED.toString());
                roomBookingRepository.save(roomBooking);
            }
        }
    }

}
