package vn.banking.academy.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "room_booking")
@Builder
public class RoomBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Timestamp createdDate;
    private String userBookCode;
    private String reason;
    private Integer quantity;
    private String status;
    private Date dateBook;
    @OneToMany(mappedBy = "roomBookingId")
    private Set<RoomBookingDetail> roomBookingsDetail = new LinkedHashSet<>();
}
