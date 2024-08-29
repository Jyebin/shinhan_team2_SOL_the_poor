package com.choikang.poor.the_poor_back.service;

import com.choikang.poor.the_poor_back.dto.InstallmentSavingsDTO;
import com.choikang.poor.the_poor_back.model.InstallmentSavings;
import com.choikang.poor.the_poor_back.repository.InstallmentSavingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstallmentSavingsService {

    @Autowired
    private InstallmentSavingsRepository installmentSavingsRepository;

    public List<InstallmentSavingsDTO> getInstallmentSavingsByUserID(Long userID) {
        List<InstallmentSavings> savingsList = installmentSavingsRepository.findByUserUserID(userID);
        return savingsList.stream()
                .map(InstallmentSavingsDTO::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<InstallmentSavings> getInstallmentSavingsByID(Long ISID) {
        return installmentSavingsRepository.findById(ISID);
    }
}
