package com.example.oba.model.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.example.oba.type.AccountStatus;
import com.example.oba.type.AccountType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "account")
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_id_sequence")
	@SequenceGenerator(name = "account_id_sequence", sequenceName = "account_id_sequence", allocationSize = 1, initialValue = 10001)
	private long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "type")
	private AccountType type;

	@Column(name = "balance")
	private double balance;

	@Column(name = "date_opened")
	private LocalDate dateOpened;

	@Column(name = "date_closed")
	private LocalDate dateClosed;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private AccountStatus status;

	@Column(name = "daily_limit")
	private double dailyLimit;
	
	@Column(name = "todayLimit")
	private double todayLimit;
	
	@Column(name = "min_balance_threshold")
	private double minBalanceThreshold;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "account", cascade = CascadeType.ALL)
	private Set<Transaction> transactions = new HashSet<>();

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "customer_id", nullable = false)
	@JsonIgnore
	private Customer customer;

	public Account() {
	}

	public Account(AccountType type, double balance, LocalDate dateOpened, LocalDate dateClosed, AccountStatus status,
			double dailyLimit, double minBalanceThreshold) {
		this.type = type;
		this.balance = balance;
		this.dateOpened = dateOpened;
		this.dateClosed = dateClosed;
		this.status = status;
		this.dailyLimit = dailyLimit;
		this.todayLimit = dailyLimit; // Assign todayLimit = dailyLimit when creating a new Account
		this.minBalanceThreshold = minBalanceThreshold;
	}
	
	public Account(AccountType type, double balance, LocalDate dateOpened, LocalDate dateClosed, AccountStatus status,
			double dailyLimit, double minBalanceThreshold, Customer customer) {
		this.type = type;
		this.balance = balance;
		this.dateOpened = dateOpened;
		this.dateClosed = dateClosed;
		this.status = status;
		this.dailyLimit = dailyLimit;
		this.todayLimit = dailyLimit; // Assign todayLimit = dailyLimit when creating a new Account
		this.minBalanceThreshold = minBalanceThreshold;
		addToCustomer(customer);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public AccountType getType() {
		return type;
	}

	public void setType(AccountType type) {
		this.type = type;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public LocalDate getDateOpened() {
		return dateOpened;
	}

	public void setDateOpened(LocalDate dateOpened) {
		this.dateOpened = dateOpened;
	}

	public LocalDate getDateClosed() {
		return dateClosed;
	}

	public void setDateClosed(LocalDate dateClosed) {
		this.dateClosed = dateClosed;
	}

	public AccountStatus getStatus() {
		return status;
	}

	public void setStatus(AccountStatus status) {
		this.status = status;
	}

	public double getDailyLimit() {
		return dailyLimit;
	}

	public void setDailyLimit(double dailyLimit) {
		this.dailyLimit = dailyLimit;
	}
	
	public double getTodayLimit() {
		return todayLimit;
	}

	public void setTodayLimit(double todayLimit) {
		this.todayLimit = todayLimit;
	}

	public double getMinBalanceThreshold() {
		return minBalanceThreshold;
	}

	public void setMinBalanceThreshold(double minBalanceThreshold) {
		this.minBalanceThreshold = minBalanceThreshold;
	}

	public Set<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(Set<Transaction> transactions) {
		this.transactions = transactions;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public void addToCustomer(Customer customer) {
		customer.getAccounts().add(this);
		this.customer = customer;
	}
}
