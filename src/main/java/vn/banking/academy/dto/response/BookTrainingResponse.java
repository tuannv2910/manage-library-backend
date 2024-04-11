package vn.banking.academy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class BookTrainingResponse {
    public Long id;
    public String bookName;
    public String sessionChat;
}
