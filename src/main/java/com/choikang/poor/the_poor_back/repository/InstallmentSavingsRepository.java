package com.choikang.poor.the_poor_back.repository;

import com.choikang.poor.the_poor_back.model.InstallmentSavings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InstallmentSavingsRepository extends JpaRepository<InstallmentSavings, Long> {

//    List<InstallmentSavings> findByUserUserID(Long userID);

    Optional<InstallmentSavings> findById(Long ISID);
}
