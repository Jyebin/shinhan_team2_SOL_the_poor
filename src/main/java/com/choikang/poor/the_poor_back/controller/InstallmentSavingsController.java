package com.choikang.poor.the_poor_back.controller;

import com.choikang.poor.the_poor_back.dto.InstallmentSavingsDTO;
import com.choikang.poor.the_poor_back.model.InstallmentSavings;
import com.choikang.poor.the_poor_back.service.InstallmentSavingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/installment-savings")
public class InstallmentSavingsController {

    @Autowired
    private InstallmentSavingsService installmentSavingsService;

//    @GetMapping("/user/{userID}")
//    public List<InstallmentSavingsDTO> getInstallmentSavingsByUserID(@PathVariable Long userID) {
//        return installmentSavingsService.getInstallmentSavingsByUserID(userID);
//    }

    @GetMapping("/confirm/{ISID}")
    public ResponseEntity<InstallmentSavingsDTO> getInstallmentSavingsByID(@PathVariable Long ISID) {
        Optional<InstallmentSavings> installmentSavings = installmentSavingsService.getInstallmentSavingsByID(ISID);
        return installmentSavings.map(savings -> ResponseEntity.ok(InstallmentSavingsDTO.convertToDTO(savings)))
                .orElse(ResponseEntity.notFound().build());
    }
}
