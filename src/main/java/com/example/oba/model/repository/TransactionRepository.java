package com.example.oba.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.oba.model.entity.Account;
import com.example.oba.model.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	List<Transaction> findByAccount(Account account);
}
