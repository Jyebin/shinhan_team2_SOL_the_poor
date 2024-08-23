package com.choikang.poor.the_poor_back.repository;

import com.choikang.poor.the_poor_back.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
