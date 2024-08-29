package com.choikang.poor.the_poor_back.controller;

import com.choikang.poor.the_poor_back.dto.AccountDTO;
import com.choikang.poor.the_poor_back.dto.TransactionDTO;
import com.choikang.poor.the_poor_back.model.Account;
import com.choikang.poor.the_poor_back.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = {"http://localhost:3000/","http://localhost/"})
@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    AccountService accountService;

    @GetMapping("/list")
    public List<AccountDTO> getAccountList(Long userID) {
        return accountService.getAccountsByUserID(userID);
    }

    @GetMapping("/transaction/list")
    public List<TransactionDTO> getTransactionList(Long accountID) {
        return accountService.getTransactionsByAccountID(accountID);
    }

    @GetMapping("/can/balance")
    public int getCanAmount(Long accountID) {
        return accountService.getCanAmountByAccountID(accountID);
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
