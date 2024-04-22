package vn.banking.academy.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * đối tượng body của cuộc hội thoại
 */
public class ConversationBody {
    public String action;
    public ArrayList<Message> messages;
    public String conversation_id;
    public String parent_message_id;
    public String model;
    public int timezone_offset_min;
    public ArrayList<Object> suggestions;
    public boolean history_and_training_disabled;
    public ConversationMode conversation_mode;
    public boolean force_paragen;
    public boolean force_rate_limit;
    public String websocket_request_id;

    public static class Author {
        public String role;

        public Author() {
            this.role = "user";
        }
    }

    public static class Content {
        public String content_type;
        public ArrayList<String> parts;

        /**
         * @param question : nội dung câu hỏi mà mình gửi cho chatGPT
         */
        public Content(String question) {
            this.content_type = "text";
            this.parts = new ArrayList<>();
            this.parts.add(question);
        }
    }

    public static class ConversationMode {
        public String kind;
        public Object plugin_ids;

        public ConversationMode() {
            this.kind = "primary_assistant";
            this.plugin_ids = null;
        }
    }

    public static class Message {
        public String id;
        public Author author;
        public Content content;
        public Metadata metadata;

        /**
         * @param question : câu hỏi đặt cho chatGPT
         */
        public Message(String question) {
            this.id = UUID.randomUUID().toString();
            this.author = new Author();
            this.content = new Content(question);
            this.metadata = new Metadata();
        }
    }

    public static class Metadata {
    }

    /**
     * @param question : câu hỏi đặt cho chatGPT
     */
    public ConversationBody(String conversation_id, String question) {
        this.action = "next";
        Message message = new Message(question);
        this.messages = new ArrayList<>();
        this.messages.add(message);
        this.conversation_id = conversation_id;
        this.parent_message_id = UUID.randomUUID().toString();
        this.model = "text-davinci-002-render-sha";
        this.timezone_offset_min = -420;
        this.suggestions = new ArrayList<>();
        this.history_and_training_disabled = false;
        this.conversation_mode = new ConversationMode();
        this.force_paragen = false;
        this.force_rate_limit = false;
        this.websocket_request_id = UUID.randomUUID().toString();
    }
}
