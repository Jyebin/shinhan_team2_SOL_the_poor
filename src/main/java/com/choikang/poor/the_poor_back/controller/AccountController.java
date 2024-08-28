package com.choikang.poor.the_poor_back.controller;

import com.choikang.poor.the_poor_back.dto.AccountDTO;
import com.choikang.poor.the_poor_back.dto.TransactionDTO;
import com.choikang.poor.the_poor_back.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = {"http://localhost:3000/","http://localhost/"})
@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    AccountService accountService;

    @GetMapping("/list")
    public List<AccountDTO> getAccountList(@RequestParam("userID") Long userID) {
        return accountService.getAccountsByUserID(userID);
    }

    @GetMapping("/transaction/list")
    public List<TransactionDTO> getTransactionList(@RequestParam("accountID") Long accountID) {
        return accountService.getTransactionsByAccountID(accountID);
    }

    @GetMapping("/can/balance")
    public int getCanAmount(@RequestParam("accountID") Long accountID) {
        return accountService.getCanAmountByAccountID(accountID);
    }

    @PostMapping("/can/manage")
    public Map<String, String> manageCan(@RequestParam Long accountID, @RequestParam boolean isTerminated) {
        Map<String, String> response = new HashMap<>();
        response.put("redirectUrl", accountService.manageCan(accountID, isTerminated));

        return response;
    }
}
