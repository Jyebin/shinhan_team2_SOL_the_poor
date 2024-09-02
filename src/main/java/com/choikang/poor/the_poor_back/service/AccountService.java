package com.choikang.poor.the_poor_back.service;

import com.choikang.poor.the_poor_back.dto.AccountDTO;
import com.choikang.poor.the_poor_back.dto.TransactionDTO;
import com.choikang.poor.the_poor_back.model.Account;
import com.choikang.poor.the_poor_back.model.Transaction;
import com.choikang.poor.the_poor_back.model.User;
import com.choikang.poor.the_poor_back.repository.AccountRepository;
import com.choikang.poor.the_poor_back.repository.TransactionRepository;
import com.choikang.poor.the_poor_back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository; // UserRepository 추가

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

    public Optional<AccountDTO> getCanAccountByUserID(Long userID) {
        return accountRepository.findByUserUserID(userID)
                .stream()
                .filter(Account::isAccountHasCan)
                .findFirst()
                .map(AccountDTO::convertToDTO);
    }

    public Optional<Account> getAccountByID(Long accountID) {
        return accountRepository.findById(accountID);
    }

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    public void updateAccountAndUserCanInfo(Long accountID, double canInterestRate) {
        // Account 정보 업데이트
        Optional<Account> accountOptional = accountRepository.findById(accountID);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            account.setAccountCanInterestRate(canInterestRate);
            account.setAccountHasCan(true);
            accountRepository.save(account);

            // User 정보 업데이트
            User user = account.getUser();
            user.setUserHasCan(true);
            userRepository.save(user);
        }
    }

    public String manageCan(Long accountID, String state) {
        terminateCanByAccountID(accountID); // 깡통 잔액 계좌로 입금
        String redirectURL = "";
        Long userID = accountRepository.findUserIDByAccountID(accountID);
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

    private void terminateCanByAccountID(Long accountID) {
        // 깡통 해지 관련 비즈니스 로직
    }
}