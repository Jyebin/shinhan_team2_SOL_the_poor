package com.choikang.poor.the_poor_back.controller;

import com.choikang.poor.the_poor_back.dto.AccountDTO;
import com.choikang.poor.the_poor_back.dto.TransactionDTO;
import com.choikang.poor.the_poor_back.service.AccountService;
import com.choikang.poor.the_poor_back.service.OAuth2UserService;
import com.choikang.poor.the_poor_back.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = {"http://localhost:3000/","http://localhost/"})
@RestController
@RequestMapping("/api/account")
public class AccountController {
    @Autowired
    AccountService accountService;

    @Autowired
    OAuth2UserService authService;

    @Autowired
    UserService userService;

    // 계좌 리스트 조회
    @GetMapping("/list")
    public ResponseEntity<?> getAccountList(HttpServletRequest request) {
        String token = authService.getJWTFromCookies(request);
        if (token == null) // 토큰 값이 없으면 BAD_REQUEST 반환
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("토큰이 존재하지 않습니다.");
        try { // userID, hasCan 값 찾는 Service 호출
            Long userID = authService.getUserID(token);
            List<AccountDTO> accountList = accountService.getAccountsByUserID(userID);
            return ResponseEntity.ok(accountList);
        } catch(Exception e) { // 실패 시 INTERNAL SERVER ERROR 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("INTERNAL SERVER ERROR");
        }
    }

    // 거래내역 조회
    @GetMapping("/transaction/list")
    public List<TransactionDTO> getTransactionList(@RequestParam("accountID") Long accountID) {
        return accountService.getTransactionsByAccountID(accountID);
    }

    // 깡통 조회
    @GetMapping("/can/balance")
    public ResponseEntity<? extends Object> getCanAmount(@RequestParam("accountID") Long accountID, HttpServletRequest request) {
        String token = authService.getJWTFromCookies(request);
        if (token == null) // 토큰 값이 없으면 BAD_REQUEST 반환
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("토큰이 존재하지 않습니다.");
        try { // userID, hasCan 값 찾는 Service 호출
            // 필요한 값 받아오기
            Long userID = authService.getUserID(token);
            int userAttendanceCnt = userService.findUserAttendanceCtnByUserID(userID);
            int canAmount = accountService.getCanAmountByAccountID(accountID);

            // response에 매핑하여 저장
            Map<String, Integer> response = new HashMap<>();
            response.put("canAmount", canAmount);
            response.put("userAttendanceCnt", userAttendanceCnt);

            return ResponseEntity.ok(response);
        } catch(Exception e) { // 실패 시 INTERNAL SERVER ERROR 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("INTERNAL SERVER ERROR");
        }
    }

    @PostMapping("/can/manage")
    public Map<String, String> manageCan(@RequestBody Map<String, Object> request) {
        Long accountID = Long.valueOf(request.get("accountID").toString());
        String status = (String) request.get("status");
        Map<String, String> response = new HashMap<>();
        response.put("redirectUrl", accountService.manageCan(accountID, status));

        return response;
    }
}
