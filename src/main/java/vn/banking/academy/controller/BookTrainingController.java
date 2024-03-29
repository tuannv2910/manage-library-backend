package vn.banking.academy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.banking.academy.dto.request.ConversationRequest;
import vn.banking.academy.service.ChatGptTrainingService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/book/training")
public class BookTrainingController {

    private final ChatGptTrainingService chatGptTrainingService;

    @GetMapping("/findAll")
    public ResponseEntity<Object> findAllBookTraining() {
        return new ResponseEntity<>(chatGptTrainingService.getAllBookTraining(), HttpStatus.OK);
    }

    @PostMapping("/conversation")
    public ResponseEntity<Object> conversation(@RequestBody ConversationRequest request) {
        return null;
    }
}
