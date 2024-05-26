package vn.banking.academy.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoomsBookedResponse {
    private UserBook userBook;
    private Set<RoomBookResponse> roomBookResponses = new HashSet<>();

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class UserBook {
        private String id;
        private String email;
        private String phoneNumber;
        private String fullName;
        private String userName;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class RoomBookResponse {
        private Integer id;
        private Timestamp createdDate;
        private String userBookCode;
        private String reason;
        private Integer quantity;
        private String status;
        private Date dateBook;
        private Set<RoomBookDetailResponse> roomBookDetailResponses = new HashSet<>();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class RoomBookDetailResponse {
        private Integer id;
        private Integer roomBookingId;
        private String timeFrame;
        @JsonFormat(pattern = "dd/MM/yyyy")
        private Date dateBook;
        private RoomResponse roomResponse = new RoomResponse();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class RoomResponse {
        private String roomCode;
        // phân loại room
        private String roomType;
        // thiết bị bên trong phòng
        private String equipment;
        private String location;
        // trang thai cua phong : hoat dong? ko hoat dong
        private Boolean status;
        // Số lượng người cho phép sử dụng phòng cùng một lúc
        private Integer quantityAllowed;
    }
}

