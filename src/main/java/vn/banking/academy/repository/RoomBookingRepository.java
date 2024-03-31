package vn.banking.academy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.banking.academy.entity.RoomBooking;

@Repository
public interface RoomBookingRepository extends JpaRepository<RoomBooking, Integer> {
}
