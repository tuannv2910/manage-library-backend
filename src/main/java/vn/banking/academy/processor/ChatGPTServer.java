//package vn.banking.academy.processor;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import okhttp3.Response;
//import org.java_websocket.WebSocket;
//import org.java_websocket.client.WebSocketClient;
//import vn.banking.academy.processor.model.ErrorResponse;
//
//import java.io.IOException;
//import java.net.URI;
//import java.util.Base64;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//public class ChatGPTServer {
//
//    private static final String OPENAI_API_KEY = System.getenv("OPENAI_API_KEY"); // Replace with your environment variable
//    private static final ObjectMapper objectMapper = new ObjectMapper();
//    private static final ConcurrentHashMap<String, WebSocket> webSocketPool = new ConcurrentHashMap<>();
//
//    // ... (Other functions and interfaces as defined previously)
//
//    public static void handleChatCompletions(RequestWithToken req, Response res) throws IOException {
//        // Extract token from authorization header
//        String token = extractToken(req.getHeaders().get("Authorization"));
//        if (token == null) {
//            res.status(401).json(ErrorResponse.errorResponse(401, "No token provided"));
//            return;
//        }
//
//        // Validate request body (using your preferred validation library)
//        // ...
//
//        // Get or initialize WebSocket connection
//        WebSocket ws = getOrCreateWebSocket(token);
//        if (ws == null) {
//            res.status(500).json(ErrorResponse.errorResponse(500, "WebSocket connection failed"));
//            return;
//        }
//
//        // Convert API request and make conversation request (using your preferred HTTP client)
//        // ...
//
//        // Handle response based on content type
//        // ... (Similar logic as in the original code)
//    }
//
//    private static String extractToken(String authorizationHeader) {
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            String token = authorizationHeader.split(" ")[1];
//            return !token.isEmpty() ? token : OPENAI_API_KEY;
//        }
//        return OPENAI_API_KEY;
//    }
//
//    private static WebSocket getOrCreateWebSocket(String token) throws IOException {
//        if (!webSocketPool.containsKey(token) || webSocketPool.get(token).getReadyState() != WebSocket.READYSTATE.OPEN) {
//            WebSocket ws = ChatGPTClient.initWebSocket(token);
//            if (!(ws instanceof WebSocketClient)) {
//                // Handle error response from InitWebSocket
//                return null;
//            }
//            webSocketPool.put(token, ws);
//        }
//        return webSocketPool.get(token);
//    }
//}