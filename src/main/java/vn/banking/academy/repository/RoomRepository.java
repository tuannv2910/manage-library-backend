package vn.banking.academy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.banking.academy.entity.Room;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {
     List<Room> findAllByStatus(Boolean status);
}
