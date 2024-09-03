package com.choikang.poor.the_poor_back.service.banking;

import com.choikang.poor.the_poor_back.model.Account;
import com.choikang.poor.the_poor_back.model.User;
import com.choikang.poor.the_poor_back.repository.AccountRepository;
import com.choikang.poor.the_poor_back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CanService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    // Can 잔액 가져와서 보여주기
    public int getCanAmountByAccountID(Long accountID) {
        return accountRepository.findCanAmountByAccountID(accountID);
    }

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