package vn.banking.academy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.banking.academy.entity.ChatGptTraining;

@Repository
public interface ChatGptTrainingRepository extends JpaRepository<ChatGptTraining, Long> {
}
