package com.example.oba.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;

import com.example.oba.model.entity.Account;
import com.example.oba.model.entity.Customer;
import com.example.oba.model.repository.AccountRepository;
import com.example.oba.model.repository.CustomerRepository;
import com.example.oba.service.EmailService;
import com.example.oba.type.AccountStatus;
import com.example.oba.type.RequestType;
import com.example.oba.utils.ConstantUtils;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/" + "${app.version}")
public class CustomerController {

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	AccountRepository accountRepository;
	
	private final EmailService emailService;
	
	@Autowired
    public CustomerController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/customers")
	public ResponseEntity<List<Customer>> getAllCustomers() {
		try {
			List<Customer> customers = customerRepository.findAll();
			if (customers.isEmpty()) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			} else {
				return new ResponseEntity<>(customers, HttpStatus.OK); 
			}
		} catch (Exception ex) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/customers/{id}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable(name = "id") long id) {
		try {
			Optional<Customer> customer = customerRepository.findById(id);
			if (customer.isEmpty()) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			} else {
				return new ResponseEntity<>(customer.get(), HttpStatus.OK);
			}
		} catch (Exception ex) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("customers/{id}/accounts")
	public ResponseEntity<List<Account>> getAllAccountsByCustomerId(@PathVariable(name = "id") long id) {
		try {
			Optional<Customer> customer = customerRepository.findById(id);
			if (customer.isEmpty()) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			} else {
				List<Account> accounts = new ArrayList<>(customer.get().getAccounts());
				if (customer.isEmpty()) {
					return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
				} else {
					return new ResponseEntity<>(accounts, HttpStatus.OK);
				}
			}
		} catch (Exception ex) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("customers")
	public ResponseEntity<Customer> createCustomer(@RequestBody Customer customerData) {
		try {
			Customer customer = new Customer();
			customer.copyData(customerData, RequestType.POST);
			customer.createDefaultAccount();
			
			Customer newCustomer = customerRepository.save(customer);
			
			Context context = new Context();
			context.setVariable("name", customerData.getFirstName());
			context.setVariable("email", customerData.getEmail());
			
			// To send email for activate account
			emailService.sendEmail(customerData.getEmail(), "Welcome to Group 8 Bank", "email-customer-activation-template", context);
			
			return new ResponseEntity<>(newCustomer, HttpStatus.CREATED);
		} catch (Exception ex) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("customers/{id}/accounts")
	public ResponseEntity<Account> createNewAccount(@PathVariable(name = "id") long id,
			@RequestBody Account accountData) {
		try {
			Optional<Customer> customer = customerRepository.findById(id);
			if (customer.isEmpty()) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			} else {
				Account account = new Account(accountData.getType(), accountData.getBalance(), LocalDate.now(),
						null, AccountStatus.ACTIVE, accountData.getDailyLimit(), accountData.getMinBalanceThreshold());
				account.addToCustomer(customer.get());
				return new ResponseEntity<Account>(accountRepository.save(account), HttpStatus.OK);
			}
		} catch (Exception ex) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/customers/{id}")
	public ResponseEntity<Customer> updateCustomer(@PathVariable(name = "id") long id,
			@RequestBody Customer customerData) {
		try {
			Optional<Customer> customer = customerRepository.findById(id);
			if (customer.isEmpty()) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			} else {
				Customer updatedCustomer = customer.get();
				updatedCustomer.copyData(customerData, RequestType.UPDATE);
				return new ResponseEntity<>(customerRepository.save(updatedCustomer), HttpStatus.OK);
			}
		} catch (Exception ex) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/customers/{id}")
	public ResponseEntity<HttpStatus> deleteCustomer(@PathVariable(name = "id") long id) {
		try {
			customerRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception ex) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
}
