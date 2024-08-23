package com.choikang.poor.the_poor_back.controller;

import com.choikang.poor.the_poor_back.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:3000/","http://localhost/"})
@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    AccountService accountService;

    @GetMapping("/hasCan")
    public Boolean getAccountHasCan(int userID) {
        return accountService.findUserHasCan((long) userID);
    }
}
