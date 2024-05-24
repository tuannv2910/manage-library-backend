package vn.banking.academy.entity;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "ai_training")
public class AITraining {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "book_name")
    private String bookName;

    @Column(name = "chat_gpt_email")
    private String chatGptEmail;

    @Column(name = "session_chat")
    private String sessionChat;

    @Column(name = "access_token")
    private String accessToken;
    @Column(name = "author")
    private String author;
    @Column(name = "image")
    private String image;
}
