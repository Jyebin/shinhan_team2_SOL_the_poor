package com.choikang.poor.the_poor_back.controller;

import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:3000/","http://localhost/"})
@RestController
@RequestMapping("/account")
public class AccountController {
    @GetMapping("/hasCan")
    public Boolean getAccountHasCan(int userID) {
        System.out.println(userID);
        return false;
    }
}
