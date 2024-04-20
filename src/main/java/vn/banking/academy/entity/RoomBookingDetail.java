package vn.banking.academy.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Table(name = "room_booking_detail")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class RoomBookingDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer roomBookingId;
    private String timeFrame;
    @JsonFormat(pattern="dd/MM/yyyy")
    private Date dateBook;
    private String roomCode;
}
