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
import vn.banking.academy.dto.response.RoomsBookedResponse;
import vn.banking.academy.entity.Room;
import vn.banking.academy.entity.RoomBooking;
import vn.banking.academy.entity.RoomBookingDetail;
import vn.banking.academy.entity.User;
import vn.banking.academy.entity.eums.BookingStatus;
import vn.banking.academy.entity.eums.TimeFrame;
import vn.banking.academy.exception.SpringException;
import vn.banking.academy.repository.RoomBookingDetailRepository;
import vn.banking.academy.repository.RoomBookingRepository;
import vn.banking.academy.repository.RoomRepository;
import vn.banking.academy.repository.UserRepository;
import vn.banking.academy.service.RoomService;
import vn.banking.academy.utils.CopyUtils;
import vn.banking.academy.utils.DateUtils;
import vn.banking.academy.validator.BookingRequestValidator;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
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
    private final UserRepository userRepository;

    @Override
    public List<RoomResponse> getRoomByDate(Date date) {
        List<Room> rooms = roomRepository.findAllByStatus(Boolean.TRUE);
        List<RoomResponse> roomResponses = new LinkedList<>();
        for (Room room : rooms) {
            RoomResponse roomResponse = new RoomResponse();
            roomResponse.setRoomCode(room.getRoomCode());
            roomResponse.setRoomType(room.getRoomType());
            roomResponse.setEquipment(Arrays.asList(room.getEquipment().split(",".trim())));
            roomResponse.setLocation(room.getLocation());
            roomResponse.setQuantityAllowed(room.getQuantityAllowed());
            List<RoomTimeFrameResponse> roomTimeFrameResponse = new LinkedList<>();
            //b1 : lấy ra toàn bộ booking room đã được đặt vào ngày được truyền vào
            List<RoomBookingDetail> bookingDateFrame = roomBookingDetailRepository.findAllByDateBookAndRoomCode(date, room.getRoomCode());
            //b2 : lấy ra danh sách room_booking đã được đặt , những  Date_frame nào nằm  trong số booking_room đã được đặt thì sẽ ở trạng thái là busy
            List<String> statusList = new ArrayList<>();
            statusList.add(BookingStatus.ACCEPT.toString());
            statusList.add(BookingStatus.PENDING.toString());
            List<Integer> roomBookingReject = roomBookingRepository.getAllRoomBookingByStatusAndDateBook(statusList
                    , date
            ).stream().map(RoomBooking::getId).collect(Collectors.toList());
            // khoi tao ra 1 list TimeFrame
            List<TimeFrame> timeFrames = new ArrayList<>(Arrays.asList(TimeFrame.values()));
            //b1.1 : duyệt qua toàn bộ bookingDateFrame vào ngày được truyền vào
            for (RoomBookingDetail roomBookingDetail : bookingDateFrame) {
                TimeFrame timeFrame = TimeFrame.getTimeFrame(roomBookingDetail.getTimeFrame());
                if (roomBookingReject.contains(roomBookingDetail.getRoomBookingId())) {
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
        LocalDate currentDate = DateUtils.localDate();
        LocalDate convertCurrentDate = request.getDateBook().toLocalDate();
        // So sánh ngày hiện tại với thời gian được truyền vào
        if (convertCurrentDate.isBefore(currentDate)) {
            return "Ngày chọn phải sau hoặc bằng ngày hiện tại";
        }
        // save room booking
        RoomBooking roomBooking = RoomBooking.builder().
                createdDate(Timestamp.from(Instant.now()))
                .userBookCode(request.getUserCode())
                .reason(request.getReason())
                .quantity(request.getQuantity())
                .status(BookingStatus.ACCEPT.toString())
                .dateBook(request.getDateBook())
                .build();
        List<Integer> listRoomBookingFail = roomBookingRepository.getAllRoomBookingByStatusAndDateBook(Arrays.asList(BookingStatus.PENDING.toString(), BookingStatus.REJECT.toString()),
                request.getDateBook()).stream().map(RoomBooking::getId).collect(Collectors.toList());
        for (String timeFrame : request.getTimeFrames()) {
            RoomBookingDetail roomBookingDetail = roomBookingDetailRepository.getRoomBookingDetailByTimeFrameAndDateBook(timeFrame, request.getDateBook());
            if (roomBookingDetail != null && listRoomBookingFail.contains(roomBookingDetail.getRoomBookingId()))
                return "Đã có người đặt phòng với khung giờ và thời gian bạn đã chọn !";
        }
        User uerBook = userRepository.getById(request.getUserCode());
        RoomBooking roomBookingSave = roomBookingRepository.save(roomBooking);
        // save room booking detail
        for (String timeFrame : request.getTimeFrames()) {
            //validate time frame
            RoomBookingDetail roomBookingDetail = RoomBookingDetail.builder()
                    .roomBookingId(roomBookingSave.getId())
                    .timeFrame(timeFrame)
                    .dateBook(request.getDateBook())
                    .roomCode(request.getRoomCode())
                    .build();
            roomBookingDetailRepository.save(roomBookingDetail);
        }
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            AccessTokenBot accessTokenBot = new AccessTokenBot();
            botsApi.registerBot(accessTokenBot);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId("2134649036");
            String message =
                    "Có đơn đặt phòng mới ! \n" +
                            "Mã booking : " + roomBooking.getId() + "\n" +
                            "Tên người đặt : " + uerBook.getFullName() + "\n" +
                            "SDT : " + uerBook.getPhoneNumber() + "\n" +
                            "Email : " + uerBook.getEmail() + "\n";
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
        if (!roomBooking.isPresent()) {
            throw new SpringException(HttpStatus.BAD_REQUEST, "không tồn tại booking với id = " + id);
        }
        List<RoomBookingDetail> allByRoomBookingId = roomBookingDetailRepository.findAllByRoomBookingId(id);
        Map<String, Object> resp = new HashMap<>();
        resp.put("room_booking", roomBooking);
        resp.put("room_booking_detail", allByRoomBookingId);
        return resp;
    }

    public Object rejectBooking(Integer id) {
        Optional<RoomBooking> roomBooking = roomBookingRepository.findById(id);
        if (!roomBooking.isPresent()) {
            throw new SpringException(HttpStatus.BAD_REQUEST, "không tồn tại booking với id = " + id);
        }
        roomBooking.get().setStatus(BookingStatus.REJECT.toString());
        roomBookingRepository.save(roomBooking.get());
        Map<String, Object> resp = new HashMap<>();
        resp.put("booking_id", id);
        resp.put("message", "Booking canceled");
        return resp;
    }

    @Override
    public Object getRoomBookedByUserId(String id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent())
            return "không tồn tại user với id là " + id;

        User user = userOptional.get();

        RoomsBookedResponse roomsBookedResponse = new RoomsBookedResponse();
        RoomsBookedResponse.UserBook userBook = new RoomsBookedResponse.UserBook();
        CopyUtils.copy(user, userBook);
        roomsBookedResponse.setUserBook(userBook);
        user.getRoomBookings().forEach(roomBooking -> {
            RoomsBookedResponse.RoomBookResponse roomBookResponse = new RoomsBookedResponse.RoomBookResponse();
            CopyUtils.copy(roomBooking, roomBookResponse);
            roomBooking.getRoomBookingsDetail().forEach(roomBookingDetail -> {
                RoomsBookedResponse.RoomBookDetailResponse roomBookDetailResponse = new RoomsBookedResponse.RoomBookDetailResponse();
                CopyUtils.copy(roomBookingDetail, roomBookDetailResponse);
                Room room = roomRepository.getById(roomBookingDetail.getRoomCode());
                RoomsBookedResponse.RoomResponse roomResponse = new RoomsBookedResponse.RoomResponse();
                CopyUtils.copy(room, roomResponse);
                roomBookDetailResponse.setRoomResponse(roomResponse);
                roomBookResponse.getRoomBookDetailResponses().add(roomBookDetailResponse);
            });
            roomsBookedResponse.getRoomBookResponses().add(roomBookResponse);
        });
        return roomsBookedResponse;
    }
}
