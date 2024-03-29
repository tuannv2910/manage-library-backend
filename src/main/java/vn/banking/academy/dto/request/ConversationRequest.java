package vn.banking.academy.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor@NoArgsConstructor
@Data
public class ConversationRequest {
    private String sessionChat;
    public String conversation;
}
