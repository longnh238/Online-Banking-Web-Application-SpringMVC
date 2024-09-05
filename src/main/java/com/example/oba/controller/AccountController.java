package com.example.oba.controller;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;

import com.example.oba.model.entity.Account;
import com.example.oba.model.entity.Customer;
import com.example.oba.model.entity.Transaction;
import com.example.oba.model.repository.AccountRepository;
import com.example.oba.model.repository.TransactionRepository;
import com.example.oba.request.AccountSettingRequest;
import com.example.oba.request.TransferRequest;
import com.example.oba.response.DestinationAccountResponse;
import com.example.oba.response.MessageResponse;
import com.example.oba.service.EmailService;
import com.example.oba.type.AccountStatus;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/" + "${app.version}")
public class AccountController {

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	TransactionRepository transactionRepository;

	private final EmailService emailService;

	@Autowired
	public AccountController(EmailService emailService) {
		this.emailService = emailService;
	}

	@GetMapping("accounts/{id}")
	public ResponseEntity<Account> getAccountById(@PathVariable(name = "id") long id) {
		try {
			Optional<Account> account = accountRepository.findById(id);
			if (account.isEmpty()) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			} else {
				return new ResponseEntity<>(account.get(), HttpStatus.OK);
			}
		} catch (Exception ex) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("accounts/destination/{id}")
	public ResponseEntity<DestinationAccountResponse> getDestinationAccountById(@PathVariable(name = "id") long id) {
		try {
			Optional<Account> account = accountRepository.findById(id);
			if (account.isEmpty()) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			} else {
				Account destinationAccount = account.get();

				DestinationAccountResponse destinationAccountResponse = new DestinationAccountResponse();
				destinationAccountResponse.setId(destinationAccount.getId());
				destinationAccountResponse.setCustomerId(destinationAccount.getCustomer().getId());
				destinationAccountResponse.setCustomerName(destinationAccount.getCustomer().getFirstName() + " "
						+ destinationAccount.getCustomer().getLastName());
				return new ResponseEntity<>(destinationAccountResponse, HttpStatus.OK);
			}
		} catch (Exception ex) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("accounts/{id}/transactions")
	public ResponseEntity<List<Transaction>> getTransactionByAccountId(@PathVariable(name = "id") long id) {
		try {
			Optional<Account> account = accountRepository.findById(id);
			if (account.isEmpty()) {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			} else {
				List<Transaction> transactions = new ArrayList<Transaction>(account.get().getTransactions());
				if (transactions.isEmpty()) {
					return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
				} else {
					return new ResponseEntity<List<Transaction>>(transactions, HttpStatus.OK);
				}
			}
		} catch (Exception ex) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("accounts/{id}/disable")
	public ResponseEntity<Account> disableAccount(@PathVariable(name = "id") long id) {
		try {
			Optional<Account> account = accountRepository.findById(id);
			if (account.isEmpty()) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			} else {
				account.get().setStatus(AccountStatus.INACTIVE);
				return new ResponseEntity<>(accountRepository.save(account.get()), HttpStatus.OK);
			}
		} catch (Exception ex) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("accounts/{id}")
	public ResponseEntity<Account> updateAccount(@PathVariable(name = "id") long id, @RequestBody Account accountData) {
		try {
			Optional<Account> account = accountRepository.findById(id);
			if (account.isEmpty()) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			} else {
				Account updatedAccount = account.get();
				updatedAccount.setDailyLimit(accountData.getDailyLimit());
				updatedAccount.setMinBalanceThreshold(accountData.getMinBalanceThreshold());
				if (updatedAccount.getTodayLimit() > accountData.getDailyLimit()) {
					updatedAccount.setTodayLimit(accountData.getDailyLimit());
				}
				return new ResponseEntity<>(accountRepository.save(account.get()), HttpStatus.OK);
			}
		} catch (Exception ex) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("accounts/{id}/dailyLimit")
	public ResponseEntity<Account> updateDailyLimit(@PathVariable(name = "id") long id,
			@RequestBody AccountSettingRequest dailyLimitRequest) {
		try {
			Optional<Account> account = accountRepository.findById(id);
			if (account.isEmpty()) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			} else {
				account.get().setDailyLimit(dailyLimitRequest.getDailyLimit());
				return new ResponseEntity<>(accountRepository.save(account.get()), HttpStatus.OK);
			}
		} catch (Exception ex) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("accounts/{id}/transfer")
	public ResponseEntity<?> transferBetweenAccounts(@PathVariable(name = "id") long id,
			@RequestBody TransferRequest transferData) {
		MessageResponse message;
		try {
			if (id == transferData.getDestinationAccountId()) {
				message = new MessageResponse("Cannot transfer in a same account");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
			}
			Optional<Account> source = accountRepository.findById(id);
			Optional<Account> destination = accountRepository.findById(transferData.getDestinationAccountId());
			double amount = transferData.getAmount();

			if (source.isPresent() && destination.isPresent() && amount > 0) {
				Account sourceAccount = source.get();
				Account destinationAccount = destination.get();

				double sourceAccountBalance = sourceAccount.getBalance();
				double destinationAccountBalance = destinationAccount.getBalance();

				double destinationAccountTodayLimit = sourceAccount.getTodayLimit();

				if (sourceAccountBalance < amount) {
					message = new MessageResponse("Insufficient balance to transfer");
				} else if (sourceAccount.getStatus() == AccountStatus.INACTIVE) {
					message = new MessageResponse("Account " + id + " is being inactive");
				} else if (destinationAccount.getStatus() == AccountStatus.INACTIVE) {
					message = new MessageResponse(
							"Account " + transferData.getDestinationAccountId() + " is being inactive");
				} else if (destinationAccountTodayLimit < amount) {
					message = new MessageResponse("Transferring amount is more than the remaining daily limit");
				} else {
					destinationAccount.setBalance(destinationAccountBalance + amount);
					accountRepository.save(destinationAccount);

					double newSourceAccountBalance = sourceAccountBalance - amount;
					if (newSourceAccountBalance < sourceAccount.getMinBalanceThreshold()) {
						Customer sourceCustomer = sourceAccount.getCustomer();

						Context context = new Context();
						context.setVariable("name", sourceCustomer.getFirstName());
						context.setVariable("email", sourceCustomer.getEmail());

						DecimalFormat df = new DecimalFormat("$#.##");
						context.setVariable("balance", df.format(newSourceAccountBalance));
						context.setVariable("threshold", df.format(sourceAccount.getMinBalanceThreshold()));

						// To send email for activate account
						emailService.sendEmail(sourceCustomer.getEmail(), "Balance Notification",
								"balance-under-threshold-template", context);
					}
					sourceAccount.setBalance(newSourceAccountBalance);
					sourceAccount.setTodayLimit(destinationAccountTodayLimit - amount);

					// Create transactions
					Transaction sourceAccountTransaction = new Transaction(
							"Transfered to account " + transferData.getDestinationAccountId(), -amount,
							LocalDateTime.now(), sourceAccount);
					transactionRepository.save(sourceAccountTransaction);

					Transaction destinationAccountTransaction = new Transaction("Received transfer from account " + id,
							amount, LocalDateTime.now(), destinationAccount);
					transactionRepository.save(destinationAccountTransaction);

					return new ResponseEntity<>(accountRepository.save(sourceAccount), HttpStatus.OK);
				}
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
			} else {
				if (source.isEmpty()) {
					message = new MessageResponse("Source account is empty");
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
				} else if (destination.isEmpty()) {
					message = new MessageResponse("Destination account is empty");
					return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
				} else {
					message = new MessageResponse("The amount should be a positive number greater than zero");
				}
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
			}
		} catch (Exception ex) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("accounts/{id}/customers/{customerId}")
	public ResponseEntity<Object> deleteAccount(@PathVariable(name = "id") long id,
			@PathVariable(name = "customerId") long customerId) {
		try {
			Map<String, String> message = new HashMap<String, String>();

			Optional<Account> account = accountRepository.findById(id);
			if (account.isEmpty()) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			} else {
				Customer customer = account.get().getCustomer();
				if (customer.getId() == customerId) {
					if(account.get().getBalance() > 0) {
						message.put("error", "Accounts with a balance greater than 0 cannot be closed");
						return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
					} else {
						accountRepository.deleteById(id);
						return new ResponseEntity<>(HttpStatus.NO_CONTENT);
					}
				} else {
					message.put("error", "Account " + id + " does not belong to customer id " + customerId);
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
				}
			}
		} catch (Exception ex) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
