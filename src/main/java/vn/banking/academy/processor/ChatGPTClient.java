package vn.banking.academy.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import vn.banking.academy.processor.model.ChatGPTInterfaces;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;


public class ChatGPTClient {


    private static final String BASE_URL = "https://chat.openai.com/backend-api/";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Function to get register websocket response
    public static ChatGPTInterfaces.RegisterWebsocketResponse getRegisterWebsocket(String token) throws IOException {
        OkHttpClient client = new OkHttpClient();

        // Build headers
        Headers headers = new Headers.Builder()
                .add("authority", "chat.openai.com")
                .add("accept", "*/*")
                .add("accept-language", "en-US")
                .add("authorization", "Bearer " + token)
                .add("content-type", "application/json")
                .add("origin", "https://chat.openai.com")
                .add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                .build();

        // Build request
        Request request = new Request.Builder()
                .url(BASE_URL + "register-websocket")
                .post(RequestBody.create(null, new byte[0]))
                .headers(headers)
                .build();

        // Execute request and parse response
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return objectMapper.readValue(response.body().string(), ChatGPTInterfaces.RegisterWebsocketResponse.class);
        }
    }

    // Function to initialize WebSocket connection
    public static WebSocket initWebSocket(String token) throws IOException {
        ChatGPTInterfaces.RegisterWebsocketResponse registerResponse = getRegisterWebsocket(token);

        // Create WebSocket client with proxy
        OkHttpClient client = new OkHttpClient.Builder()
                .proxy(new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", 7990)))
                .build();

        // Build WebSocket request
        Request request = new Request.Builder()
                .url(registerResponse.getWssUrl())
                .addHeader("Sec-Websocket-Protocol", "json.reliable.webpubsub.azure.v1")
                .addHeader("Origin", "https://chat.openai.com")
                .addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36")
                .build();

        // Create WebSocket listener
        WebSocketListener listener = new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                // Handle open event
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                // Handle message event
            }

            // Implement other WebSocketListener methods as needed
        };

        // Connect and return WebSocket
        return client.newWebSocket(request, listener);
    }
}