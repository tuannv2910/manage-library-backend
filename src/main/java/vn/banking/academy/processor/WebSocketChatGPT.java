package vn.banking.academy.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class WebSocketChatGPT {

    // System Message Interface
    static class ISystem {
        public String type;
        public String event;
        public String userId;
        public String connectionId;
        public String reconnectionToken;

        public ISystem(String type, String event, String userId, String connectionId, String reconnectionToken) {
            this.type = type;
            this.event = event;
            this.userId = userId;
            this.connectionId = connectionId;
            this.reconnectionToken = reconnectionToken;
        }
    }

    // Message Interface
    static class IMessage {
        public int sequenceId;
        public String type;
        public String from_user;
        public String fromUserId;
        public String group;
        public String dataType;
        public Map<String, String> data;

        public IMessage(int sequenceId, String type, String from_user, String fromUserId, String group, String dataType, Map<String, String> data) {
            this.sequenceId = sequenceId;
            this.type = type;
            this.from_user = from_user;
            this.fromUserId = fromUserId;
            this.group = group;
            this.dataType = dataType;
            this.data = data;
        }
    }

    public static void main(String[] args) throws URISyntaxException, InterruptedException {
        String url = "wss://chatgpt-async-webps-prod-southcentralus-0.chatgpt.com/client/hubs/conversations?access_token=" +
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJodHRwczovL2NoYXRncHQtYXN5bmMtd2VicHMtcHJvZC1zb3V0aGNlbnRyYWx1cy0wLndlYnB1YnN1Yi5henVyZS5jb20vY2xpZW50L2h1YnMvY29udmVyc2F0aW9ucyIsImlhdCI6MTcxNDIyMDI5NywiZXhwIjoxNzE0MjIzODk3LCJzdWIiOiJ1c2VyLTRjVW1RdzZQSVR3RlY0VVJzUzhqTjN1SSIsInJvbGUiOlsid2VicHVic3ViLmpvaW5MZWF2ZUdyb3VwLnVzZXItNGNVbVF3NlBJVHdGVjRVUnNTOGpOM3VJIl0sIndlYnB1YnN1Yi5ncm91cCI6WyJ1c2VyLTRjVW1RdzZQSVR3RlY0VVJzUzhqTjN1SSJdfQ.0jvrZPxXqolmiXnkTaarE4S0B9Wy4OTfvfAoB5CrMgY";
        Map<String, String> httpHeaders = new HashMap<>();
        httpHeaders.put("Sec-Websocket-Protocol", "json.reliable.webpubsub.azure.v1");
        httpHeaders.put("Origin", "https://chat.openai.com");
        httpHeaders.put("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36");

        WebSocketClient wsClient = new WebSocketClient(new URI(url), httpHeaders) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                System.out.println("WebSocket connection opened.");
            }

            @Override
            public void onMessage(String message) {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    Map<String, Object> resp = mapper.readValue(message, Map.class);

                    if ("system".equals(resp.get("type"))) {
                        ISystem system = new ISystem(
                                (String) resp.get("type"),
                                (String) resp.get("event"),
                                (String) resp.get("userId"),
                                (String) resp.get("connectionId"),
                                (String) resp.get("reconnectionToken")
                        );
                        System.out.println(system.reconnectionToken);
                        ChatGPTClient.initWebSocket(system.reconnectionToken);
                    } else if ("message".equals(resp.get("type"))) {
                        IMessage messageObj = new IMessage(
                                (int) resp.get("sequenceId"),
                                (String) resp.get("type"),
                                (String) resp.get("from"),
                                (String) resp.get("fromUserId"),
                                (String) resp.get("group"),
                                (String) resp.get("dataType"),
                                (Map<String, String>) resp.get("data")
                        );
                        String data = new String(Base64.getDecoder().decode(messageObj.data.get("body")));
                        System.out.println(data);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                System.out.println("WebSocket connection closed.");
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        };

        wsClient.connectBlocking();
    }
}