package vn.banking.academy.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import okhttp3.*;
import org.springframework.stereotype.Service;
import vn.banking.academy.dto.request.ConversationRequest;
import vn.banking.academy.dto.response.BookTrainingResponse;
import vn.banking.academy.dto.response.ConversationResponse;
import vn.banking.academy.repository.ChatGptTrainingRepository;
import vn.banking.academy.service.ChatGptTrainingService;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class ChatGptTrainingServiceImpl implements ChatGptTrainingService {
    private final ChatGptTrainingRepository chatGptTrainingRepository;

    @Override
    public List<BookTrainingResponse> getAllBookTraining() {
        return chatGptTrainingRepository.findAllBookTraining();
    }

    @Override
    public Object conversation(ConversationRequest req) {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            Message message = new Message("user", req.conversation);
            Root root = new Root(req.sessionChat, Collections.singletonList(message));
            RequestBody body = RequestBody.create(mediaType, new Gson().toJson(root));
            Request request = new Request.Builder()
                    .url("https://api.chatpdf.com/v1/chats/message")
                    .method("POST", body)
                    .addHeader("x-api-key", "sec_nkfRkwgr4jdiAQJ7h4vWxkFtaMQgmur6")
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                assert response.body() != null;
                JsonObject obj = new Gson().fromJson(response.body().string(), JsonObject.class);
                return new ConversationResponse(obj.get("content").getAsString());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ConversationResponse("Co loi say ra");
        }
        return new ConversationResponse("He thong dang gap van de");
    }

    @AllArgsConstructor
    public class Message {
        public String role;
        public String content;
    }

    @AllArgsConstructor
    public class Root {
        public String sourceId;
        public List<Message> messages;
    }

}



