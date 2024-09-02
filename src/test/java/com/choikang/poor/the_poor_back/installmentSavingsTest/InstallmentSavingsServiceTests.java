package com.choikang.poor.the_poor_back.installmentSavingsTest;

import com.choikang.poor.the_poor_back.dto.InstallmentSavingsDTO;
import com.choikang.poor.the_poor_back.model.InstallmentSavings;
import com.choikang.poor.the_poor_back.repository.InstallmentSavingsRepository;
import com.choikang.poor.the_poor_back.service.InstallmentSavingsService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InstallmentSavingsServiceTests {

    // 1. 리포지토리를 Mock 객체로 선언
    @Mock
    private InstallmentSavingsRepository installmentSavingsRepository;

    // 2. 서비스 클래스를 테스트 대상으로 선언하고, Mock 객체 주입
    @InjectMocks
    private InstallmentSavingsService installmentSavingsService;

    // 3. 특정 사용자 ID에 대한 적금 리스트를 가져오는 메서드를 테스트
//    @Test
//    void testGetInstallmentSavingsByUserID() {
//        Long userID = 1L;
//
//        // 4. Mock 객체가 반환할 가짜 데이터를 설정
//        List<InstallmentSavings> savingsList = List.of(
//                new InstallmentSavings(1L, "쏠거지 적금"),
//                new InstallmentSavings(2L, "땡겨요 적금")
//        );
//
//        // 5. Mock 리포지토리의 메서드 동작을 정의
//        when(installmentSavingsRepository.findByUserUserID(userID)).thenReturn(savingsList);
//
//        // 6. 서비스 메서드 호출 및 결과 검증
//        List<InstallmentSavingsDTO> result = installmentSavingsService.getInstallmentSavingsByUserID(userID);
//
//        // 7. 결과가 기대한 값과 일치하는지 확인
//        assertEquals(2, result.size());
//        verify(installmentSavingsRepository, times(1)).findByUserUserID(userID);
//    }

    // 8. 특정 적금 ID에 대한 적금 정보를 가져오는 메서드를 테스트
    @Test
    void testGetInstallmentSavingsByID() {
        Long ISID = 1L;
        InstallmentSavings savings = new InstallmentSavings(ISID, "쏠거지 적금");

        // 9. Mock 리포지토리의 메서드 동작을 정의
        when(installmentSavingsRepository.findById(ISID)).thenReturn(Optional.of(savings));

        // 10. 서비스 메서드 호출 및 결과 검증
        Optional<InstallmentSavings> result = installmentSavingsService.getInstallmentSavingsByID(ISID);

        // 11. 결과가 기대한 값과 일치하는지 확인
        assertTrue(result.isPresent());
        assertEquals(savings, result.get());
        verify(installmentSavingsRepository, times(1)).findById(ISID);
    }
}
