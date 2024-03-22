package vn.banking.academy.context;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.banking.academy.repository.ChatGptTrainingRepository;

@Component
@RequiredArgsConstructor
public class ContextRepository {
    private final ChatGptTrainingRepository chatGptTrainingRepository;
}
