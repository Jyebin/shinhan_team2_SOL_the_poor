package com.choikang.poor.the_poor_back.controller;

import com.choikang.poor.the_poor_back.dto.AccountDTO;
import com.choikang.poor.the_poor_back.dto.TransactionDTO;
import com.choikang.poor.the_poor_back.model.Account;
import com.choikang.poor.the_poor_back.service.AccountService;
import com.choikang.poor.the_poor_back.service.OAuth2UserService;
import com.choikang.poor.the_poor_back.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost"})
@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private OAuth2UserService authService;

    @Autowired
    private UserService userService;

    // 계좌 리스트 조회
    @GetMapping("/list")
    public ResponseEntity<?> getAccountList(HttpServletRequest request) {
        String token = authService.getJWTFromCookies(request);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("토큰이 존재하지 않습니다.");
        }
        try {
            Long userID = authService.getUserIDFromJWT(token);  // userID는 JWT에서 추출
            List<AccountDTO> accountList = accountService.getAccountsByUserID(userID);
            return ResponseEntity.ok(accountList);
        } catch (Exception e) {
            log.error("Error fetching account list", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("계좌 목록을 가져오는 중 오류가 발생했습니다.");
        }
    }

    // 거래내역 조회
    @GetMapping("/transaction/list")
    public ResponseEntity<List<TransactionDTO>> getTransactionList(@RequestParam("accountID") Long accountID) {
        try {
            List<TransactionDTO> transactions = accountService.getTransactionsByAccountID(accountID);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            log.error("Error fetching transaction list", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 깡통 조회
    @GetMapping("/can/balance")
    public ResponseEntity<?> getCanAmount(@RequestParam("accountID") Long accountID, HttpServletRequest request) {
        String token = authService.getJWTFromCookies(request);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("토큰이 존재하지 않습니다.");
        }
        try {
            Long userID = authService.getUserIDFromJWT(token);
            int userAttendanceCnt = userService.findUserAttendanceCntByUserID(userID);
            int canAmount = accountService.getCanAmountByAccountID(accountID);

            Map<String, Integer> response = new HashMap<>();
            response.put("canAmount", canAmount);
            response.put("userAttendanceCnt", userAttendanceCnt);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching CAN balance and user attendance", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("CAN 잔액 및 출석 정보를 가져오는 중 오류가 발생했습니다.");
        }
    }

    @PostMapping("/can/manage")
    public ResponseEntity<? extends Object> manageCan(@RequestBody Map<String, Object> request) {
        try {
            Long accountID = Long.valueOf(request.get("accountID").toString());
            String status = (String) request.get("status");
            Map<String, String> response = new HashMap<>();
            response.put("redirectUrl", accountService.manageCan(accountID, status));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error managing CAN", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/password/{accountID}")
    public ResponseEntity<Integer> getAccountPassword(@PathVariable Long accountID) {
        try {
            Optional<Account> account = accountService.getAccountByID(accountID);
            return account.map(value -> ResponseEntity.ok(value.getAccountPW()))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (Exception e) {
            log.error("Error fetching account password", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/updateCanInfo")
    public ResponseEntity<Void> updateAccountAndUserCanInfo(@RequestBody AccountDTO accountDTO) {
        try {
            accountService.updateAccountAndUserCanInfo(accountDTO.getAccountID(), accountDTO.getCanInterestRate());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error updating CAN info", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
