package com.choikang.poor.the_poor_back.controller.banking;

import com.choikang.poor.the_poor_back.dto.AccountDTO;
import com.choikang.poor.the_poor_back.model.Account;
import com.choikang.poor.the_poor_back.service.banking.AccountService;
import com.choikang.poor.the_poor_back.service.OAuth2UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost"})
@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final OAuth2UserService authService;

    @GetMapping("/list")
    public ResponseEntity<?> getAccountsList(HttpServletRequest request) {
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
}
