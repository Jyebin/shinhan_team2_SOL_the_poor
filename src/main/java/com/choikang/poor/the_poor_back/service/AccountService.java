package com.choikang.poor.the_poor_back.service;

import com.choikang.poor.the_poor_back.dto.AccountDTO;
import com.choikang.poor.the_poor_back.dto.TransactionDTO;
import com.choikang.poor.the_poor_back.model.Account;
import com.choikang.poor.the_poor_back.model.Transaction;
import com.choikang.poor.the_poor_back.repository.AccountRepository;
import com.choikang.poor.the_poor_back.repository.TransactionRepository;
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

    public String manageCan(Long accountID, boolean isTerminated) {
        terminateCanByAccountID(accountID); // 깡통 잔액 계좌로 입금

        if (isTerminated) {
            // 계좌의 hasCan -> false로 변경
            return "/myAccount"; // 리다이렉트할 URL 반환
        } else {
            return ""; // 현재 페이지에 머무르도록 빈 문자열 반환
        }
    }

    private void terminateCanByAccountID(Long accountID) {
        // 깡통 해지 관련 비즈니스 로직

    }
}
