package vn.banking.academy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomTimeFrameResponse {
    private String timeFrame;
    private Integer startTime;
    private Integer endTime;
    private String status;
}
