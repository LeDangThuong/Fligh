package com.example.FlightBooking.Services.Planes;

import com.example.FlightBooking.Components.FactoryMethod.BusinessClassSeatFactory;
import com.example.FlightBooking.Components.FactoryMethod.EconomyClassSeatFactory;
import com.example.FlightBooking.Components.FactoryMethod.FirstClassSeatFactory;
import com.example.FlightBooking.Enum.SeatStatus;
import com.example.FlightBooking.Models.Airlines;
import com.example.FlightBooking.Models.Planes;
import com.example.FlightBooking.Models.Seats;
import com.example.FlightBooking.Repositories.AirlinesRepository;
import com.example.FlightBooking.Repositories.PlaneRepository;
import com.example.FlightBooking.Repositories.SeatRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PlaneService {
    @Autowired
    private PlaneRepository planeRepository;
    @Autowired
    private  AirlinesRepository airlinesRepository;
    @Autowired
    private ObjectMapper objectMapper; // Sử dụng Jackson ObjectMapper để chuyển đổi JSON

    public Planes createPlaneWithSeats(Long airlineId) throws Exception {
        Airlines airline = getAirlineById(airlineId); // Phương thức để lấy thông tin hãng hàng không
        String flightNumber = generateUniqueFlightNumber(airline);

        Planes plane = new Planes();
        plane.setFlightNumber(flightNumber);
        plane.setAirline(airline);

        Map<String, String> seatStatuses = new HashMap<>();
        seatStatuses.putAll(new FirstClassSeatFactory().createSeats(plane));
        seatStatuses.putAll(new BusinessClassSeatFactory().createSeats(plane));
        seatStatuses.putAll(new EconomyClassSeatFactory().createSeats(plane));

        String seatStatusesJson = objectMapper.writeValueAsString(seatStatuses);
        plane.setSeatStatuses(seatStatusesJson);

        return planeRepository.save(plane);
    }

    private String generateUniqueFlightNumber(Airlines airline) {
        String flightNumber;
        Random random = new Random();
        do {
            flightNumber = generateFlightNumber(airline, random);
        } while (planeRepository.existsByFlightNumber(flightNumber));
        return flightNumber;
    }

    private String generateFlightNumber(Airlines airline, Random random) {
        int number = 100 + random.nextInt(900); // Tạo số bất kỳ từ 100 đến 999
        switch (airline.getAirlineName()) {
            case "Vietnam Airlines":
                return "VN-" + number;
            case "VietJet Air":
                return "VJ-" + number;
            case "Bamboo Airways":
                return "BAV-" + number;
            case "Jetstar Airlines":
                return "JST-" + number;
            default:
                return "UNDEFINED";
        }
    }

    private Airlines getAirlineById(Long airlineId) {
        return airlinesRepository.findById(airlineId).orElseThrow(() -> new RuntimeException("Airline not found"));
    }
    public Map<String, String> getSeatStatuses(Long planeId) throws Exception {
        Planes plane = planeRepository.findById(planeId).orElseThrow(() -> new RuntimeException("Plane not found"));
        String seatStatusesJson = plane.getSeatStatuses();
        Map<String, String> seatStatuses = objectMapper.readValue(seatStatusesJson, Map.class);

        // Sắp xếp theo thứ tự từ A-Z
        Map<String, String> sortedSeatStatuses = new TreeMap<>(seatStatuses);
        return sortedSeatStatuses;
    }
    public boolean holdSeats(Long planeId, Set<String> seatNumbers) throws Exception {
        Planes plane = planeRepository.findById(planeId).orElseThrow(() -> new RuntimeException("Plane not found"));
        String seatStatusesJson = plane.getSeatStatuses();
        Map<String, String> seatStatuses = objectMapper.readValue(seatStatusesJson, Map.class);

        // Kiểm tra xem tất cả các chỗ ngồi có sẵn hay không
        for (String seatNumber : seatNumbers) {
            if (!seatStatuses.containsKey(seatNumber) || !SeatStatus.AVAILABLE.name().equals(seatStatuses.get(seatNumber))) {
                return false; // Nếu bất kỳ chỗ ngồi nào không có sẵn, trả về false
            }
        }

        // Cập nhật trạng thái của các chỗ ngồi được chọn
        for (String seatNumber : seatNumbers) {
            seatStatuses.put(seatNumber, SeatStatus.ON_HOLD.name());
        }

        // Chuyển đổi lại thành JSON và lưu vào cơ sở dữ liệu
        seatStatusesJson = objectMapper.writeValueAsString(seatStatuses);
        plane.setSeatStatuses(seatStatusesJson);
        planeRepository.save(plane);

        // Hẹn giờ để thay đổi trạng thái ON_HOLD trở lại AVAILABLE sau 5 phút nếu không được BOOKED
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        try {
                            releaseSeats(planeId, seatNumbers);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                300000 // 5 phút
        );

        return true;
    }

    public void releaseSeats(Long planeId, Set<String> seatNumbers) throws Exception {
        Planes plane = planeRepository.findById(planeId).orElseThrow(() -> new RuntimeException("Plane not found"));
        String seatStatusesJson = plane.getSeatStatuses();
        Map<String, String> seatStatuses = objectMapper.readValue(seatStatusesJson, Map.class);

        for (String seatNumber : seatNumbers) {
            if (SeatStatus.ON_HOLD.name().equals(seatStatuses.get(seatNumber))) {
                seatStatuses.put(seatNumber, SeatStatus.AVAILABLE.name());
            }
        }

        seatStatusesJson = objectMapper.writeValueAsString(seatStatuses);
        plane.setSeatStatuses(seatStatusesJson);
        planeRepository.save(plane);
    }

    public boolean bookSeats(Long planeId, Set<String> seatNumbers) throws Exception {
        Planes plane = planeRepository.findById(planeId).orElseThrow(() -> new RuntimeException("Plane not found"));
        String seatStatusesJson = plane.getSeatStatuses();
        Map<String, String> seatStatuses = objectMapper.readValue(seatStatusesJson, Map.class);

        for (String seatNumber : seatNumbers) {
            if (!SeatStatus.ON_HOLD.name().equals(seatStatuses.get(seatNumber))) {
                return false; // Nếu bất kỳ chỗ ngồi nào không ở trạng thái ON_HOLD, trả về false
            }
        }

        for (String seatNumber : seatNumbers) {
            seatStatuses.put(seatNumber, SeatStatus.BOOKED.name());
        }

        seatStatusesJson = objectMapper.writeValueAsString(seatStatuses);
        plane.setSeatStatuses(seatStatusesJson);
        planeRepository.save(plane);

        return true;
    }
}
