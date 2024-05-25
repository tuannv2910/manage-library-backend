package vn.banking.academy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.banking.academy.entity.RoomBookingDetail;

import javax.swing.text.html.Option;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomBookingDetailRepository extends JpaRepository<RoomBookingDetail, Integer> {
    @Query("select rbd from RoomBooking rb join RoomBookingDetail rbd on rb.id = rbd.roomBookingId" +
            " where rbd.dateBook =:date  and rbd.roomCode =: roomCode and rbd.roomCode =:roomCode")
    List<RoomBookingDetail> findAllByDateBookAndRoomCode(Date date, String roomCode);

    List<RoomBookingDetail> findAllByRoomBookingId(Integer bookingId);

    RoomBookingDetail getRoomBookingDetailByTimeFrameAndDateBook(String timeFrame, Date dateBook);
}
