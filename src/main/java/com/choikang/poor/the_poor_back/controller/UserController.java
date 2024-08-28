package com.choikang.poor.the_poor_back.controller;

import com.choikang.poor.the_poor_back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:3000/","http://192.168.0.63:3000/"})
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/hasCan")
    public Boolean getAccountHasCan(Long userID) {
        return userService.findUserHasCanByUserID(userID);
    }
}
