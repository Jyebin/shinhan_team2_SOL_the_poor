package com.choikang.poor.the_poor_back.accountTest;

import com.choikang.poor.the_poor_back.dto.AccountDTO;
import com.choikang.poor.the_poor_back.model.Account;
import com.choikang.poor.the_poor_back.model.User;
import com.choikang.poor.the_poor_back.repository.AccountRepository;
import com.choikang.poor.the_poor_back.service.AccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class AccountServiceTests {
    @Autowired
    AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void testGetAccountsByUserId() {
        // Given: 테스트를 위한 데이터 준비
        User user = User.builder().userID(1L).build();

        Account account1 = Account.builder()
                .user(user)
                .accountNumber("110-576-040419")
                .accountName("쏠편한 입출금통장(저축예금)")
                .accountBalance(100000000)
                .accountHasCan(true)
                .build();

        Account account2 = Account.builder()
                .user(user)
                .accountNumber("110-233-298374")
                .accountName("신한 슈퍼SOL 통장")
                .accountBalance(100000)
                .accountHasCan(false)
                .build();

        // When: 계좌 정보 조회
        List<AccountDTO> accounts = accountService.getAccountsByUserID(1L);

        // Then: 계좌 정보 검증
        Assertions.assertNotNull(accounts);
        Assertions.assertEquals(2, accounts.size());

        AccountDTO accountDTO1 = accounts.get(0);
        Assertions.assertEquals("110-576-040419", accountDTO1.getAccountNumber());
        Assertions.assertEquals("쏠편한 입출금통장(저축예금)", accountDTO1.getAccountName());
        Assertions.assertEquals(100000000, accountDTO1.getAccountBalance());
        Assertions.assertTrue(accountDTO1.getAccountHasCan());

        AccountDTO accountDTO2 = accounts.get(1);
        Assertions.assertEquals("110-233-298374", accountDTO2.getAccountNumber());
        Assertions.assertEquals("신한 슈퍼SOL 통장", accountDTO2.getAccountName());
        Assertions.assertEquals(100000, accountDTO2.getAccountBalance());
        Assertions.assertFalse(accountDTO2.getAccountHasCan());
    }
}
