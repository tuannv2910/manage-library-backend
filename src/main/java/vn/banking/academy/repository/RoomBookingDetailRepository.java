package vn.banking.academy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.banking.academy.entity.RoomBookingDetail;

import java.util.Date;
import java.util.List;

@Repository
public interface RoomBookingDetailRepository extends JpaRepository<RoomBookingDetail, Integer> {
        @Query("select rbd from RoomBooking rb join RoomBookingDetail rbd on rb.id = rbd.roomBookingId" +
                " where rbd.dateBook =:date  and rbd.roomCode =: roomCode and rbd.roomCode =:roomCode")
        List<RoomBookingDetail> findAllByDateBookAndRoomCode(Date date,String roomCode);
        List<RoomBookingDetail> findAllByRoomBookingId(Integer bookingId);
        Boolean existsRoomBookingDetailByTimeFrameAndDateBook(String timeFrame, Date dateBook);
}
