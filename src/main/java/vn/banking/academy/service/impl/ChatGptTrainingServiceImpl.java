package vn.banking.academy.service.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import vn.banking.academy.dto.response.BookTrainingResponse;
import vn.banking.academy.repository.ChatGptTrainingRepository;
import vn.banking.academy.service.ChatGptTrainingService;

import java.util.List;

@Service
@AllArgsConstructor
public class ChatGptTrainingServiceImpl implements ChatGptTrainingService {
    private final ChatGptTrainingRepository chatGptTrainingRepository;

    @Override
    public List<BookTrainingResponse> getAllBookTraining() {
        return chatGptTrainingRepository.findAllBookTraining();
    }
}
