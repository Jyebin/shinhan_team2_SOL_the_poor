package com.choikang.poor.the_poor_back.controller;

import com.choikang.poor.the_poor_back.service.AccountService;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:3000/","http://localhost/"})
@RestController
@RequestMapping("/account")
public class AccountController {
    AccountService accountService;

    @GetMapping("/hasCan")
    public Boolean getAccountHasCan(Long userID) {
        return accountService.findUserHasCan(userID);
    }
}
