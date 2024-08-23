package com.choikang.poor.the_poor_back.service;

import com.choikang.poor.the_poor_back.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public Boolean findUserHasCan(Long userID) {
        return accountRepository.findUserHasCanByUserId(userID);
    }
}
