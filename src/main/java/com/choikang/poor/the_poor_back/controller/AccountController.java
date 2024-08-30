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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List; 
import java.util.Optional; 
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

    @GetMapping("/transaction/list")
    public List<TransactionDTO> getTransactionList(@RequestParam("accountID") Long accountID) {
        return accountService.getTransactionsByAccountID(accountID);
    }

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
    public Map<String, String> manageCan(@RequestParam Long accountID, @RequestParam boolean isTerminated) {
        Map<String, String> response = new HashMap<>();
        response.put("redirectUrl", accountService.manageCan(accountID, isTerminated));

        return response;
    }

    @GetMapping("/password/{accountID}")
    public ResponseEntity<Integer> getAccountPassword(@PathVariable Long accountID) {
        Optional<Account> account = accountService.getAccountByID(accountID);
        return account.map(value -> ResponseEntity.ok(value.getAccountPW()))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/updateCanInfo")
    public ResponseEntity<String> updateAccountCanInfo(@RequestBody AccountDTO accountDTO) {
        boolean isUpdated = accountService.updateAccountCanInfo(accountDTO);

        if (isUpdated) {
            return ResponseEntity.ok("Account updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update account");
        }
    }

}
