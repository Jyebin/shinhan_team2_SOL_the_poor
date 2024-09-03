package com.choikang.poor.the_poor_back.controller.banking;

import com.choikang.poor.the_poor_back.dto.AccountDTO;
import com.choikang.poor.the_poor_back.service.OAuth2UserService;
import com.choikang.poor.the_poor_back.service.UserService;
import com.choikang.poor.the_poor_back.service.banking.CanService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost"})
@RestController
@RequestMapping("/api/can")
@RequiredArgsConstructor
public class CanController {
    private final OAuth2UserService authService;
    private final UserService userService;
    private final CanService canService;

    // 깡통 조회
    @GetMapping("/balance")
    public ResponseEntity<?> getCanAmount(@RequestParam("accountID") Long accountID, HttpServletRequest request) {
        String token = authService.getJWTFromCookies(request);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("토큰이 존재하지 않습니다.");
        }
        try {
            Long userID = authService.getUserID(token);
            int userAttendanceCnt = userService.findUserAttendanceCntByUserID(userID);
            int canAmount = canService.getCanAmountByAccountID(accountID);

            Map<String, Integer> response = new HashMap<>();
            response.put("canAmount", canAmount);
            response.put("userAttendanceCnt", userAttendanceCnt);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching CAN balance and user attendance", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("CAN 잔액 및 출석 정보를 가져오는 중 오류가 발생했습니다.");
        }
    }

    @PostMapping("/manage")
    public ResponseEntity<? extends Object> manageCan(@RequestBody Map<String, Object> request) {
        try {
            Long accountID = Long.valueOf(request.get("accountID").toString());
            String status = (String) request.get("status");
            Map<String, String> response = new HashMap<>();
            response.put("redirectUrl", canService.manageCan(accountID, status));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error managing CAN", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/updateCanInfo")
    public ResponseEntity<Void> updateAccountAndUserCanInfo(@RequestBody AccountDTO accountDTO) {
        try {
            canService.updateAccountAndUserCanInfo(accountDTO.getAccountID(), accountDTO.getCanInterestRate());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error updating CAN info", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
