package com.example.FlightBooking.Controller.Decorator;

import com.example.FlightBooking.Models.Decorator.Vouchers;
import com.example.FlightBooking.Services.DecoratorService.VoucherService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@Tag(name = "Decorator Design Pattern Controller", description = ".....")
@RequestMapping("/decorator")
public class VoucherController {
    @Autowired
    private VoucherService voucherService;

    @PostMapping("/create")
    public ResponseEntity<Vouchers> createVoucher(@RequestBody Vouchers voucher) {
        Vouchers createdVoucher = voucherService.createVoucher(voucher);
        return ResponseEntity.ok(createdVoucher);
    }

    @GetMapping("/get-by-id")
    public ResponseEntity<Vouchers> getVoucherById(@RequestParam Long id) {
        Vouchers voucher = voucherService.getVoucherById(id);
        return ResponseEntity.ok(voucher);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<Vouchers>> getAllVouchers() {
        List<Vouchers> vouchers = voucherService.getAllVouchers();
        return ResponseEntity.ok(vouchers);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Vouchers> updateVoucher(@PathVariable Long id, @RequestBody Vouchers voucherDetails) {
        Vouchers updatedVoucher = voucherService.updateVoucher(id, voucherDetails);
        return ResponseEntity.ok(updatedVoucher);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteVoucher(@PathVariable Long id) {
        voucherService.deleteVoucher(id);
        return ResponseEntity.noContent().build();
    }
}
