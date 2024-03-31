package vn.banking.academy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import vn.banking.academy.bot.AccessTokenBot;

@SpringBootApplication
public class ManageLibraryBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManageLibraryBackendApplication.class, args);
        TelegramBotsApi botsApi;
        try {
            botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new AccessTokenBot());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
