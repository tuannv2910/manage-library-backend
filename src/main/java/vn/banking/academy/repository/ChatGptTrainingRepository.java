package vn.banking.academy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.banking.academy.dto.response.BookTrainingResponse;
import vn.banking.academy.entity.AITraining;

import java.util.List;

@Repository
public interface ChatGptTrainingRepository extends JpaRepository<AITraining, Long> {
    @Query("select new vn.banking.academy.dto.response.BookTrainingResponse(" +
            "c.id, c.bookName,c.sessionChat) from AITraining c ")
    List<BookTrainingResponse> findAllBookTraining();

    AITraining findBySessionChat(String sessionChat);

    @Query("update AITraining c set c.accessToken =: accessToken")
    void updateAccessToken(@Param("accessToken") String accessToken) ;

}
