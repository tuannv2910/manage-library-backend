package vn.banking.academy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.banking.academy.dto.response.BookTrainingResponse;
import vn.banking.academy.entity.ChatGptTraining;

import java.util.List;

@Repository
public interface ChatGptTrainingRepository extends JpaRepository<ChatGptTraining, Long> {
    @Query("select new vn.banking.academy.dto.response.BookTrainingResponse(" +
            "c.id, c.bookName,c.sessionChat) from ChatGptTraining c ")
    List<BookTrainingResponse> findAllBookTraining();
}
