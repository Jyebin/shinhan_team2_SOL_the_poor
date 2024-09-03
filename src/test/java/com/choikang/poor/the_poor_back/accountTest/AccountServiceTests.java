package com.choikang.poor.the_poor_back.accountTest;

import com.choikang.poor.the_poor_back.dto.AccountDTO;
import com.choikang.poor.the_poor_back.dto.TransactionDTO;
import com.choikang.poor.the_poor_back.model.Account;
import com.choikang.poor.the_poor_back.model.Transaction;
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
        Assertions.assertEquals("110-576-040419", accountDTO1.getNumber());
        Assertions.assertEquals("쏠편한 입출금통장(저축예금)", accountDTO1.getName());
        Assertions.assertEquals(100000000, accountDTO1.getBalance());
        Assertions.assertTrue(accountDTO1.getHasCan());

        AccountDTO accountDTO2 = accounts.get(1);
        Assertions.assertEquals("110-233-298374", accountDTO2.getNumber());
        Assertions.assertEquals("신한 슈퍼SOL 통장", accountDTO2.getName());
        Assertions.assertEquals(100000, accountDTO2.getBalance());
        Assertions.assertFalse(accountDTO2.getHasCan());
    }

    @Test
    @DisplayName("accountID로 거래내역 찾기")
    public void testGetTransactionsByAccountID() {
        // Given: 테스트를 위한 데이터 준비
        Account account = Account.builder().accountID(1L).build();

        TransactionDTO transaction = TransactionDTO.builder()
                .date("2024-08-12")
                .time("11:35:48")
                .balance(100000800)
                .amount(800)
                .description("깡통")
                .status(false)
                .build();

        // When: 계좌 정보 조회
        List<TransactionDTO> transactions = accountService.getTransactionsByAccountID(account.getAccountID());

        // Then: 계좌 정보 검증
        Assertions.assertNotNull(transactions);
        Assertions.assertEquals(5, transactions.size());

        TransactionDTO transactionDTO = transactions.get(0);
        Assertions.assertEquals(transaction.getDate(), transactionDTO.getDate());
        Assertions.assertEquals(transaction.getTime(), transactionDTO.getTime());
        Assertions.assertEquals(transaction.getBalance(), transactionDTO.getBalance());
        Assertions.assertEquals(transaction.getAmount(), transactionDTO.getAmount());
        Assertions.assertEquals(transaction.getDescription(), transactionDTO.getDescription());
        Assertions.assertEquals(transaction.getStatus(), transactionDTO.getStatus());
    }

    @Test
    @DisplayName("깡통 잔액 테스트")
    public void getCanAmountTest() {
        int amount = 1700;
        int testAmount = accountService.getCanAmountByAccountID(1L);
        Assertions.assertEquals(amount, testAmount);
    }

    @Test
    @DisplayName("깡통 해지 테스트")
    public void canTerminationTest() {

    }

    @Test
    @DisplayName("깡통 가입 테스트")
    public void canRegisterTest() {

    }
}
