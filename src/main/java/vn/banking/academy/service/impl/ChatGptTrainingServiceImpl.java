package vn.banking.academy.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import vn.banking.academy.bot.AccessTokenBot;
import vn.banking.academy.dto.request.ConversationRequest;
import vn.banking.academy.dto.response.BookTrainingResponse;
import vn.banking.academy.dto.response.ConversationResponse;
import vn.banking.academy.entity.ChatGptTraining;
import vn.banking.academy.exception.SpringException;
import vn.banking.academy.processor.AskWithChatGPT;
import vn.banking.academy.processor.ChatRequirementsToken;
import vn.banking.academy.repository.ChatGptTrainingRepository;
import vn.banking.academy.service.ChatGptTrainingService;

import java.util.List;

@Service
@AllArgsConstructor
public class ChatGptTrainingServiceImpl implements ChatGptTrainingService {
    private final ChatGptTrainingRepository chatGptTrainingRepository;
    private final AskWithChatGPT askWithChatGPT = new AskWithChatGPT();
    private final ChatRequirementsToken chatRequirementsToken = new ChatRequirementsToken();

    @Override
    public List<BookTrainingResponse> getAllBookTraining() {
        return chatGptTrainingRepository.findAllBookTraining();
    }

    @Override
    public Object conversation(ConversationRequest request) {
        ChatGptTraining obj = chatGptTrainingRepository.findBySessionChat(request.getSessionChat());
        if (obj == null)
            throw new SpringException(HttpStatus.BAD_GATEWAY, "Không tồn tại session chat tương ứng");

        Pair<String, Integer> generator = chatRequirementsToken.generator(obj.getAccessToken(), obj.getSessionChat());
        if (generator == null)
            throw new SpringException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi hệ thống");

        if (generator.getSecond() == 400) {
            try {
                TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
                AccessTokenBot accessTokenBot = new AccessTokenBot();
                botsApi.registerBot(accessTokenBot);
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId("2134649036");
                sendMessage.setText("AccessToken cho phiên chat đã hết hạn. Vui lòng cập nhật accessToken qua phiên chat này.");
                accessTokenBot.execute(sendMessage);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            throw new SpringException(HttpStatus.BAD_GATEWAY, "Token đã hết hạn, vui lòng cập nhật qua telegram");
        }
        return new ConversationResponse(askWithChatGPT.startQuestion(obj.getSessionChat(),
                request.conversation, generator.getFirst()));
    }
}
