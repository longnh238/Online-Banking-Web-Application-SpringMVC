package com.example.oba.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.oba.model.entity.Account;
import com.example.oba.type.AccountType;

public interface AccountRepository extends JpaRepository<Account, Long> {

	List<Account> findByType(AccountType type);
	
	List<Account> findByCustomerId(long customerId);
}
