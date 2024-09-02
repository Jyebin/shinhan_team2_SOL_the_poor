package com.choikang.poor.the_poor_back.accountTest;

import com.choikang.poor.the_poor_back.dto.TransactionDTO;
import com.choikang.poor.the_poor_back.model.Account;
import com.choikang.poor.the_poor_back.service.banking.CanService;
import com.choikang.poor.the_poor_back.service.banking.TransactionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TransactionServiceTests {
    @Autowired
    TransactionService transactionService;

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
        List<TransactionDTO> transactions = transactionService.getTransactionsByAccountID(account.getAccountID());

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
}
