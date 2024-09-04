package com.choikang.poor.the_poor_back.controller.banking;

import com.choikang.poor.the_poor_back.dto.TransactionDTO;
import com.choikang.poor.the_poor_back.service.banking.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost"})
@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    // 거래내역 조회
    @GetMapping("/list")
    public ResponseEntity<List<TransactionDTO>> getTransactionList(@RequestParam("accountID") Long accountID) {
        try {
            List<TransactionDTO> transactions = transactionService.getTransactionsByAccountID(accountID);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            log.error("Error fetching transaction list", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
