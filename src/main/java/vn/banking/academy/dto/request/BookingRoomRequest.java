package vn.banking.academy.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class BookingRoomRequest {
    private String roomCode;
    private List<String> timeFrames;
    private String userCode;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dateBook;
    private String reason;
    private Integer quantity;
}
