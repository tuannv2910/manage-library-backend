package vn.banking.academy.entity;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "room")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Room {
    @Id
    // id cua rom
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
