package vn.banking.academy.service;

import vn.banking.academy.dto.request.ConversationRequest;
import vn.banking.academy.dto.response.BookTrainingResponse;

import java.util.List;

public interface ChatGptTrainingService {
     List<BookTrainingResponse> getAllBookTraining();

     Object conversation(ConversationRequest request);
}
