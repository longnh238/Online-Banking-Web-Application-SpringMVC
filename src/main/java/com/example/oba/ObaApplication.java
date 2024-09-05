package com.example.oba;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.example.oba.model.entity.Account;
import com.example.oba.model.entity.Customer;
import com.example.oba.model.repository.AccountRepository;
import com.example.oba.model.repository.CustomerRepository;
import com.example.oba.type.AccountStatus;
import com.example.oba.type.AccountType;
import com.example.oba.utils.ConstantUtils;

// Disable the login authentication
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
// Enable the scheduling
@EnableScheduling
public class ObaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ObaApplication.class, args);
	}

	@Bean
	ApplicationRunner init(CustomerRepository userRepository, AccountRepository accountRepository) {
		return args -> {
			List<Customer> customers = new ArrayList<>();

			Account accountChequingJohn = new Account(AccountType.CHEQUING, 2000, LocalDate.now(), null, AccountStatus.ACTIVE,
					ConstantUtils.DAILY_LIMIT, ConstantUtils.MIN_BALANCE_THRESHOLD);
			Customer customer = new Customer("john", "John", "Duo", LocalDate.parse("1990-01-01"), "john@abc.com",
					"0123456789", "Vancouver", accountChequingJohn);
			customers.add(customer);
			Account accountSavingsJohn = new Account(AccountType.SAVINGS, 2000, LocalDate.now(), null, AccountStatus.ACTIVE,
					ConstantUtils.DAILY_LIMIT, ConstantUtils.MIN_BALANCE_THRESHOLD, customer);

			Account accountAlice = new Account(AccountType.CHEQUING, 0, LocalDate.now(), null, AccountStatus.ACTIVE,
					ConstantUtils.DAILY_LIMIT, ConstantUtils.MIN_BALANCE_THRESHOLD);
			customers.add(new Customer("alice", "Alice", "Maria", LocalDate.parse("1998-01-01"), "alice@abc.com",
					"0123456789", "Burnaby", accountAlice));

			userRepository.saveAll(customers);
			
			accountRepository.save(accountSavingsJohn);
		};
	}
}
