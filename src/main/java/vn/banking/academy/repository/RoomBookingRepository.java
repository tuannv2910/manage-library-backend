package vn.banking.academy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.banking.academy.entity.RoomBooking;

import java.util.Date;
import java.util.List;

@Repository
public interface RoomBookingRepository extends JpaRepository<RoomBooking, Integer> {
    List<RoomBooking> getAllRoomBookingByStatusAndDateBook(String status, Date date);
}
