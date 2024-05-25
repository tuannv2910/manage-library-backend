package vn.banking.academy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.banking.academy.entity.RoomBooking;

import java.util.Date;
import java.util.List;

@Repository
public interface RoomBookingRepository extends JpaRepository<RoomBooking, Integer> {
    @Query("SELECT a from RoomBooking a where a.dateBook = :date and a.status in :status")
    List<RoomBooking> getAllRoomBookingByStatusAndDateBook(List<String> status, Date date);

    @Query("select r from RoomBooking r where r.status = 'PENDING'")
    List<RoomBooking> getAllRoomBookingByStatus();
}
