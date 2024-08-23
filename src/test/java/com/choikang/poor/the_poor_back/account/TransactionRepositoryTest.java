package com.choikang.poor.the_poor_back.account;

import com.choikang.poor.the_poor_back.model.Account;
import com.choikang.poor.the_poor_back.model.Transaction;
import com.choikang.poor.the_poor_back.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

@SpringBootTest
public class TransactionRepositoryTest {
    @Autowired
    TransactionRepository transactionRepository;

    @Test
    public void insertDummiesToAccount1() {
        Account account = Account.builder().accountID((long)1).build();

        String name[] = {"하소영", "태리약국", "깡통", "지에스25 연남", "깡통"};
        int money[] = {100000, 3100, 900, 7200, 800};
        boolean isDeposit[] = {true, false, false, false, false};
        int balance[] = {99912000, 100012000, 100008900, 100008000, 100000800, 100000000};
        int year = 2024, month = 8, dayOfMonth = 12;
        int hour[] = {11, 12, 12, 19, 19};
        int min[] = {35, 37, 37, 8, 8};
        int sec[] = {48, 3, 33, 29, 59};

        // account 1 거래내역
        IntStream.rangeClosed(0, 4).forEach(i -> {
            Transaction transaction = Transaction.builder()
                    .transactionName(name[i])
                    .transactionMoney(money[i])
                    .transactionBalance(balance[i])
                    .transactionDate(LocalDateTime.of(year, month, dayOfMonth, hour[i], min[i], sec[i]))
                    .transactionIsDeposit(isDeposit[i])
                    .account(account)
                    .build();
            transactionRepository.save(transaction);
        });
    }

    @Test
    public void insertDummiesToAccount2() {
        Account account = Account.builder().accountID((long)2).build();

        // account 2 거래내역

    }
}
