package vn.banking.academy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomResponse {
    private String roomCode;
    private String roomType;
    private List<String> equipment;
    private String location;
    private Integer quantityAllowed;
    List<RoomTimeFrameResponse> roomTimeFrameStatus;
}
