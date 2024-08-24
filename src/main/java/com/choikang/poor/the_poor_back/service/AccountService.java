package com.choikang.poor.the_poor_back.service;

import com.choikang.poor.the_poor_back.dto.AccountDTO;
import com.choikang.poor.the_poor_back.model.Account;
import com.choikang.poor.the_poor_back.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {
    @Autowired
    AccountRepository accountRepository;

    public List<AccountDTO> getAccountsByUserID(Long userID) {
        List<Account> accounts = accountRepository.findByUserUserID(userID);
        return accounts.stream()
                .map(AccountDTO::convertToDTO)
                .collect(Collectors.toList());
    }
}
