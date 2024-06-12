package com.example.FlightBooking.Services.AirlineService;
import com.example.FlightBooking.DTOs.Request.AirlineAndAirport.AirlineRequest;
import com.example.FlightBooking.Models.Airlines;
import com.example.FlightBooking.Models.Planes;
import com.example.FlightBooking.Models.Users;
import com.example.FlightBooking.Repositories.AirlinesRepository;

import com.example.FlightBooking.Repositories.PlaneRepository;
import com.example.FlightBooking.Services.CloudinaryService.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class AirlinesService {
    @Autowired
    private AirlinesRepository airlinesRepository;
    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private PlaneRepository planeRepository;

    public List<Airlines> getAllAirlines() {
        return airlinesRepository.findAll();
    }

    public Optional<Airlines> getAirlinesById(Long id) {
        return airlinesRepository.findById(id);
    }

    public Airlines addAirlines(Airlines airlines) {
        return airlinesRepository.save(airlines);
    }

    public Airlines updateAirlines(Long id, List<MultipartFile> files) throws IOException {
        List<String> promo = cloudinaryService.uploadAirlinePromo(files);
        Optional<Airlines> optionalAirlines = airlinesRepository.findById(id);
        if (optionalAirlines.isPresent()) {
            Airlines airlines = optionalAirlines.get();
            airlines.setPromoForAirline(promo);
            return airlinesRepository.save(airlines);
        } else {
            throw new RuntimeException("Airline not found with id " + id);
        }
    }

    public boolean deleteAirlines(Long id) {
        if(airlinesRepository.existsById(id)){
            airlinesRepository.deleteById(id);
            return true;
        }else {
            return false;
        }
    }
    private Airlines getAirlineById(Long airlineId) {
        return airlinesRepository.findById(airlineId).orElseThrow(() -> new RuntimeException("Airline not found"));
    }
    public Airlines createNewAirline(String airlineName, MultipartFile file, List<MultipartFile> files) throws IOException {
        // Tải lên logo và nhận URL mới
        String logoUrl = cloudinaryService.uploadAirlineLogo(file);
        List<String> popularPlaceUrl = cloudinaryService.uploadAirlinePromo(files);
        // Tạo mới hãng hàng không với tên và logo URL
        Airlines airlines = new Airlines();
        airlines.setAirlineName(airlineName);
        airlines.setPromoForAirline(popularPlaceUrl);
        airlines.setLogoUrl(logoUrl);

        return airlinesRepository.save(airlines);
    }

    public Airlines getAirlineByPlaneId(Long planeId)
    {
        Planes planes = planeRepository.findById(planeId).orElseThrow(() -> new RuntimeException("Plane not found"));
        return planes.getAirline();
    }
}
