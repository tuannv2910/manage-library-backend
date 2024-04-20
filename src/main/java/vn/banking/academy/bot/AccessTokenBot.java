package vn.banking.academy.bot;

import com.fasterxml.jackson.databind.util.BeanUtil;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import vn.banking.academy.repository.ChatGptTrainingRepository;
import vn.banking.academy.utils.BeanUtils;


public class AccessTokenBot extends TelegramLongPollingBot {
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String accessToken = update.getMessage().getText();
            System.out.println("msssage == " + accessToken);
//            // Xử lý tin nhắn từ người dùng tại đây
//            ChatGptTrainingRepository chatGptTrainingRepository = BeanUtils.getBean(ChatGptTrainingRepository.class);
//            chatGptTrainingRepository.updateAccessToken(accessToken);
        }
    }

    @Override
    public String getBotUsername() {
        return "manage_library_bot";
    }

    @Override
    public String getBotToken() {
        return "7151267232:AAGFZxqOXv204drQDQt8D2B6Y91m1pY0p2E";
    }
}
