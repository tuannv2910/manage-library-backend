package vn.banking.academy.processor.model;

public class ChatGPTInterfaces {

    // Interface for Register Websocket Response
    public interface RegisterWebsocketResponse {
        String getWssUrl();
        String getExpiresAt();
    }

    // Interface for ChatGPT Conversation Response
    public interface ChatGPTConversationResponse {
        String getConversationId();
        String getResponseId();
    }

    // Interface for ChatGPT WebSocket System Response
    public interface ChatGPTWsResponseSystem {
        String getType();
        String getEvent();
        String getUserId();
        String getConnectionId();
        String getReconnectionToken();
    }

    // Interface for ChatGPT WebSocket Message Response
    public interface ChatGPTWsResponseMessage {
        int getSequenceId();
        String getType();
        String getFrom();
        String getFromUserId();
        String getGroup();
        String getDataType();
        WsResponseMessageData getData();
    }

    // Interface for WebSocket Response Message Data
    public interface WsResponseMessageData {
        String getType();
        String getBody();
        boolean isMoreBody();
        String getResponseId();
        String getWebsocketRequestId();
        String getConversationId();
    }
}