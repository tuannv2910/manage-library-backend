package vn.banking.academy.service.impl;

import com.google.gson.*;
import lombok.AllArgsConstructor;
import okhttp3.*;
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
import vn.banking.academy.utils.Choice;
import vn.banking.academy.utils.StaticUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        List<String> apiKeys = StaticUtils.apiKeys;
        int index = new Random().nextInt(apiKeys.size());
        try {
            String url = "https://api.openai.com/v1/chat/completions";
            String apiKey = "YOUR API KEY HERE";
            String model = "gpt-3.5-turbo";
                URL obj = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Authorization", "Bearer " + apiKeys.get(index));
                connection.setRequestProperty("Content-Type", "application/json");

                // The request body
                String body = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + request.conversation + "\"}]}";
                connection.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(body);
                writer.flush();
                writer.close();

                // Response from ChatGPT
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;

                StringBuffer response = new StringBuffer();

                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                br.close();
                JsonObject jsonObject = new Gson().fromJson(response.toString(), JsonObject.class);
            Choice.Root root = new Gson().fromJson(jsonObject.toString(), Choice.Root.class);
            return new ConversationResponse(root.choices.get(0).message.content);
            } catch (Exception e) {
            e.printStackTrace();
                return new ConversationResponse("Co loi say ra !");
            }
    }
}

