package vn.banking.academy.service.impl;

import jakarta.ws.rs.core.Link;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.banking.academy.dto.request.BookingRoomRequest;
import vn.banking.academy.dto.response.RoomResponse;
import vn.banking.academy.dto.response.RoomTimeFrameResponse;
import vn.banking.academy.entity.Room;
import vn.banking.academy.entity.RoomBookingDetail;
import vn.banking.academy.entity.eums.TimeFrame;
import vn.banking.academy.repository.RoomBookingDetailRepository;
import vn.banking.academy.repository.RoomRepository;
import vn.banking.academy.service.RoomService;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static vn.banking.academy.conf.StaticField.timeFrameAvailable;
import static vn.banking.academy.conf.StaticField.timeFrameBusy;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final RoomBookingDetailRepository roomBookingDetailRepository;

    @Override
    public List<RoomResponse> getRoomByDate(Date date) {
        List<Room> rooms = roomRepository.findAllByStatus(Boolean.TRUE);
        List<RoomResponse> roomResponses = new LinkedList<>();
        for (Room room : rooms) {
            RoomResponse roomResponse = new RoomResponse();
            roomResponse.setRoomCode(room.getRoomCode());
            roomResponse.setRoomType(room.getRoomType());
            roomResponse.setEquipment(new ArrayList<>(List.of(room.getEquipment().split(",".trim()))));
            roomResponse.setLocation(room.getLocation());
            roomResponse.setQuantityAllowed(room.getQuantityAllowed());
            List<RoomTimeFrameResponse> roomTimeFrameResponse = new LinkedList<>();
            //b1 : lấy ra toàn bộ booking room đã được đặt vào ngày được truyền vào
            List<RoomBookingDetail> bookingDateFrame = roomBookingDetailRepository.findAllByDateBookAndRoomCode(date,room.getRoomCode());
            // khoi tao ra 1 list TimeFrame
            List<TimeFrame> timeFrames = new ArrayList<>(List.of(TimeFrame.values()));
            //b1.1 : duyệt qua toàn bộ bookingDateFrame vào ngày được truyền vào
            for (RoomBookingDetail roomBookingDetail : bookingDateFrame) {
                TimeFrame timeFrame = TimeFrame.getTimeFrame(roomBookingDetail.getTimeFrame());
                if (null != timeFrame) {
                    // chỗ này nếu như đã tồn tại trong db thì có nghĩa là đã được book
                    RoomTimeFrameResponse timeFrameResponse = new RoomTimeFrameResponse(
                            timeFrame.toString(),
                            timeFrame.getStartTime(),
                            timeFrame.getEndTime(),
                            timeFrameBusy
                    );
                    timeFrames.remove(timeFrame);
                    roomTimeFrameResponse.add(timeFrameResponse);
                }
            }
            // còn lại thì các frame khác sẽ là available
            roomTimeFrameResponse.addAll(timeFrames.stream().map(timeFrame -> new RoomTimeFrameResponse(timeFrame.toString(),
                    timeFrame.getStartTime(), timeFrame.getEndTime(), timeFrameAvailable)).toList());
            roomResponse.setRoomTimeFrameStatus(roomTimeFrameResponse);
            roomResponses.add(roomResponse);
        }
        return roomResponses;
    }

    @Override
    public Object checkOut(BookingRoomRequest request) {
        return null;
    }
}
