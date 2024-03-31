package vn.banking.academy.entity.eums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.sql.Time;
import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor
@Getter
public enum TimeFrame {
    TIME_1(8, 9),
    TIME_2(9, 10),
    TIME_3(10, 11),
    TIME_4(11, 12),
    TIME_5(12, 13),
    TIME_6(13, 14),
    TIME_7(14, 15),
    TIME_8(15, 16),
    TIME_9(16, 17),
    TIME_10(17, 18);
    private final int startTime;
    private final int endTime;

    public static TimeFrame getTimeFrame(String timeFrameString) {
        Optional<TimeFrame> result = Arrays.stream(TimeFrame.values())
                .filter(timeFrame -> timeFrame.toString().equalsIgnoreCase(timeFrameString)).findFirst();
        return result.orElse(null);
    }

    public static TimeFrame getTimeFrameByStartTimeAndEndTime(int startTime, int endTime) {
        for (TimeFrame timeFrame : TimeFrame.values()) {
            if (timeFrame.startTime == startTime && timeFrame.endTime == endTime)
                return timeFrame;
        }
        return null;
    }

}
