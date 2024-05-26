package vn.banking.academy.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @Size(max = 255)
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "created_date")
    private Instant createdDate;

    @Size(max = 255)
    @Column(name = "email")
    private String email;

    @Size(max = 255)
    @Column(name = "full_name")
    private String fullName;

    @Size(max = 255)
    @Column(name = "otp_count")
    private String otpCount;

    @Size(max = 255)
    @Column(name = "pass_word")
    private String passWord;

    @Size(max = 255)
    @Column(name = "phone_number")
    private String phoneNumber;

    @Size(max = 255)
    @Column(name = "status")
    private String status;

    @Column(name = "update_date")
    private Instant updateDate;

    @Size(max = 255)
    @Column(name = "user_name")
    private String userName;

    @OneToMany(mappedBy = "userBookCode")
    private Set<RoomBooking> roomBookings = new LinkedHashSet<>();

}