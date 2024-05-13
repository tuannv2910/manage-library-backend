package vn.banking.academy.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;


public class AccessTokenBot extends TelegramLongPollingBot {
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String accessToken = update.getMessage().getText();
            System.out.println("message == " + accessToken);
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
