package vn.banking.academy.entity.eums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BookingStatus {
    PENDING("Đang chờ"),
    EXPIRED("Hết hạn"),
    REJECT("Bị từ chối"),
    ACCEPT("Được duyệt");
    private final String status;

}
