package com.choikang.poor.the_poor_back.service;

import com.choikang.poor.the_poor_back.dto.AccountDTO;
import com.choikang.poor.the_poor_back.dto.TransactionDTO;
import com.choikang.poor.the_poor_back.model.Account;
import com.choikang.poor.the_poor_back.model.Transaction;
import com.choikang.poor.the_poor_back.repository.AccountRepository;
import com.choikang.poor.the_poor_back.repository.TransactionRepository;
import com.choikang.poor.the_poor_back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserRepository userRepository;

    public List<AccountDTO> getAccountsByUserID(Long userID) {
        List<Account> accounts = accountRepository.findByUserUserID(userID);
        return accounts.stream()
                .map(AccountDTO::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<TransactionDTO> getTransactionsByAccountID(Long accountID) {
        List<Transaction> transactions = transactionRepository.findByAccountAccountIDOrderByTransactionDateDesc(accountID);
        return transactions.stream()
                .map(TransactionDTO::convertToDTO)
                .collect(Collectors.toList());
    }

    // Can 잔액 가져와서 보여주기
    public int getCanAmountByAccountID(Long accountID) {
        return accountRepository.findCanAmountByAccountID(accountID);
    }

    public String manageCan(Long accountID, String state) {
        Long userID = accountRepository.findUserIDByAccountID(accountID);
        String redirectURL = "";

        // state = 'register'
        if ("register".equals(state)) {
            // hasCan = true
            accountRepository.updateAccountHasCanByAccountID(accountID, true);
            userRepository.updateUserHasCanById(userID, true);

            redirectURL = "/";
        } else {
            // state = 'terminateChecked'
            if ("terminateUnChecked".equals(state)) {
                // hasCan = false
                accountRepository.updateAccountHasCanByAccountID(accountID, false);
                userRepository.updateUserHasCanById(userID, false);

                redirectURL = "/myAccount";
            }
            accountRepository.updateBalanceAndResetCanAmount(accountID);
        }
        return redirectURL;
    }
}
