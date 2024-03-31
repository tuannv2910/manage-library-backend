package vn.banking.academy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.banking.academy.entity.RoomBookingDetail;

import java.util.Date;
import java.util.List;

@Repository
public interface RoomBookingDetailRepository extends JpaRepository<RoomBookingDetail, Integer> {
        List<RoomBookingDetail> findAllByDateBookAndRoomCode(Date date,String roomCode);
}
