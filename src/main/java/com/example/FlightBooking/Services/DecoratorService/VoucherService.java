package com.example.FlightBooking.Services.DecoratorService;

import com.example.FlightBooking.Models.Decorator.Vouchers;
import com.example.FlightBooking.Repositories.Decorator.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoucherService {
    @Autowired
    private VoucherRepository voucherRepository;

    public Vouchers createVoucher(Vouchers voucher) {
        return voucherRepository.save(voucher);
    }

    public Vouchers getVoucherById(Long id) {
        return voucherRepository.findById(id).orElseThrow(() -> new RuntimeException("Voucher not found with this id: " + id));
    }

    public List<Vouchers> getAllVouchers() {
        return voucherRepository.findAll();
    }

    public Vouchers updateVoucher(Long id, Vouchers voucherDetails) {
        Vouchers voucher = getVoucherById(id);
        voucher.setCode(voucherDetails.getCode());
        voucher.setDiscountAmount(voucherDetails.getDiscountAmount());
        return voucherRepository.save(voucher);
    }

    public void deleteVoucher(Long id) {
        voucherRepository.deleteById(id);
    }
}
