package vn.banking.academy.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import vn.banking.academy.bot.AccessTokenBot;
import vn.banking.academy.dto.request.BookingRoomRequest;
import vn.banking.academy.dto.response.RoomResponse;
import vn.banking.academy.dto.response.RoomTimeFrameResponse;
import vn.banking.academy.entity.Room;
import vn.banking.academy.entity.RoomBooking;
import vn.banking.academy.entity.RoomBookingDetail;
import vn.banking.academy.entity.eums.BookingStatus;
import vn.banking.academy.entity.eums.TimeFrame;
import vn.banking.academy.exception.SpringException;
import vn.banking.academy.repository.RoomBookingDetailRepository;
import vn.banking.academy.repository.RoomBookingRepository;
import vn.banking.academy.repository.RoomRepository;
import vn.banking.academy.service.RoomService;
import vn.banking.academy.validator.BookingRequestValidator;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static vn.banking.academy.conf.StaticField.timeFrameAvailable;
import static vn.banking.academy.conf.StaticField.timeFrameBusy;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final RoomBookingDetailRepository roomBookingDetailRepository;
    private final RoomBookingRepository roomBookingRepository;

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
            List<RoomBookingDetail> bookingDateFrame = roomBookingDetailRepository.findAllByDateBookAndRoomCode(date, room.getRoomCode());
            //b2 : lấy ra danh sách room_booking đã được đặt , những  Date_frame nào nằm  trong số booking_room đã được đặt thì sẽ ở trạng thái là busy
            List<Integer> roomBookingReject = roomBookingRepository.getAllRoomBookingByStatusAndDateBook(
                    BookingStatus.REJECT.toString(), date
            ).stream().map(RoomBooking::getId).collect(Collectors.toList());

            // khoi tao ra 1 list TimeFrame
            List<TimeFrame> timeFrames = new ArrayList<>(List.of(TimeFrame.values()));
            //b1.1 : duyệt qua toàn bộ bookingDateFrame vào ngày được truyền vào
            for (RoomBookingDetail roomBookingDetail : bookingDateFrame) {
                TimeFrame timeFrame = TimeFrame.getTimeFrame(roomBookingDetail.getTimeFrame());
                if (null != timeFrame && !roomBookingReject.contains(roomBookingDetail.getRoomBookingId())) {
                    // chỗ này nếu như đã tồn tại trong db thì có nghĩa là đã được book
                    // check trường hợp frame này đã được book, và đơn book đã được accept
                    //thì trạng thái của frame sẽ là busy
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
                    timeFrame.getStartTime(), timeFrame.getEndTime(), timeFrameAvailable)).collect(Collectors.toList()));
            roomResponse.setRoomTimeFrameStatus(roomTimeFrameResponse);
            roomResponses.add(roomResponse);
        }
        return roomResponses;
    }

    @Override
    public Object checkOut(BookingRoomRequest request) {
        BookingRequestValidator.validate(request);
        // save room booking
        RoomBooking roomBooking = RoomBooking.builder().
                createdDate(Timestamp.from(Instant.now()))
                .userBookCode(request.getUserCode())
                .reason(request.getReason())
                .quantity(request.getQuantity())
                .status(BookingStatus.PENDING.toString())
                .dateBook(request.getDateBook())
                .build();
        RoomBooking roomBookingSave = roomBookingRepository.save(roomBooking);
        // save room booking detail
        request.getTimeFrames().forEach(timeFrame -> {
            RoomBookingDetail roomBookingDetail = RoomBookingDetail.builder()
                    .roomBookingId(roomBookingSave.getId())
                    .timeFrame(timeFrame)
                    .dateBook(request.getDateBook())
                    .roomCode(request.getRoomCode())
                    .build();
            roomBookingDetailRepository.save(roomBookingDetail);
        });
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            AccessTokenBot accessTokenBot = new AccessTokenBot();
            botsApi.registerBot(accessTokenBot);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId("2134649036");
            String message = "```\n" +
                    "Mã booking  Người đặt\n" +
                    roomBookingSave.getId() + " " + request.getUserCode() + "\n" +
                    "```";
            sendMessage.setText(message);
            accessTokenBot.execute(sendMessage);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        Map<String, Object> resp = new HashMap<>();
        resp.put("booking_id", roomBookingSave.getId());
        resp.put("mess", "Book thành công");
        return resp;
    }

    @Override
    public Object getDetail(Integer id) {
        Optional<RoomBooking> roomBooking = roomBookingRepository.findById(id);
        if (roomBooking.isEmpty()) {
            throw new SpringException(HttpStatus.BAD_REQUEST, "không tồn tại booking với id = " + id);
        }
        List<RoomBookingDetail> allByRoomBookingId = roomBookingDetailRepository.findAllByRoomBookingId(id);
        Map<String, Object> resp = new HashMap<>();
        resp.put("room_booking", roomBooking);
        resp.put("room_booking_detail", allByRoomBookingId);
        return resp;
    }

    @Override
    public Object acceptBooking(Integer id) {
        Optional<RoomBooking> roomBooking = roomBookingRepository.findById(id);
        if (roomBooking.isEmpty()) {
            throw new SpringException(HttpStatus.BAD_REQUEST, "không tồn tại booking với id = " + id);
        }
        roomBooking.get().setStatus(BookingStatus.ACCEPT.toString());
        roomBookingRepository.save(roomBooking.get());
        Map<String, Object> resp = new HashMap<>();
        resp.put("booking_id", id);
        resp.put("message", "Booking accepted");
        return resp;
    }
}
