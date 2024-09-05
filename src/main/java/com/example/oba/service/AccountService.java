package com.example.oba.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.oba.model.entity.Account;
import com.example.oba.model.repository.AccountRepository;
import com.example.oba.utils.ConstantUtils;

@Service

public class AccountService {

	@Autowired
	AccountRepository accountRepository;

	@Scheduled(cron = ConstantUtils.DAILY_LIMIT_RESET_TIME) // At 12:00 AM every day
	public void resetDailyLimit() {
		List<Account> accounts = accountRepository.findAll();
		if (accounts.size() > 0) {
			for(Account account : accounts) {
				account.setTodayLimit(account.getDailyLimit());
				accountRepository.save(account);
			} 
		}	
	}
}
