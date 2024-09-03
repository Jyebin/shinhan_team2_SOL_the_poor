package com.choikang.poor.the_poor_back.service.banking;

import com.choikang.poor.the_poor_back.dto.TransactionDTO;
import com.choikang.poor.the_poor_back.model.Transaction;
import com.choikang.poor.the_poor_back.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public List<TransactionDTO> getTransactionsByAccountID(Long accountID) {
        List<Transaction> transactions = transactionRepository.findByAccountAccountIDOrderByTransactionDateDesc(accountID);
        return transactions.stream()
                .map(TransactionDTO::convertToDTO)
                .collect(Collectors.toList());
    }
}
