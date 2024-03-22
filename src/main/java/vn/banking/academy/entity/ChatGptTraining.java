package vn.banking.academy.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "chat_gpt_training")
public class ChatGptTraining {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "book_name")
    private String bookName;

    @Column(name = "chat_gpt_email")
    private String chatGptEmail;

    @Column(name = "status_train")
    private String statusTrain;

    @Column(name = "session_chat")
    private Integer sessionChat;
}
