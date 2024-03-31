package vn.banking.academy.bot;

import com.fasterxml.jackson.databind.util.BeanUtil;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import vn.banking.academy.repository.ChatGptTrainingRepository;
import vn.banking.academy.utils.BeanUtils;


public class AccessTokenBot extends TelegramLongPollingBot {
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String accessToken = update.getMessage().getText();
            // Xử lý tin nhắn từ người dùng tại đây
            ChatGptTrainingRepository chatGptTrainingRepository = BeanUtils.getBean(ChatGptTrainingRepository.class);
            chatGptTrainingRepository.updateAccessToken(accessToken);
        }
    }

    @Override
    public String getBotUsername() {
        return "chatgpt_access_token_bot";
    }

    @Override
    public String getBotToken() {
        return "7013026515:AAHQmYm6Pe-KAeXenLfGc7SlWJ8onE6HMvY";
    }
}
