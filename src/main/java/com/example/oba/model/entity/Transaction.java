package com.example.oba.model.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "transaction")
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_id_sequence")
	@SequenceGenerator(name = "transaction_id_sequence", sequenceName = "transaction_id_sequence", allocationSize = 1, initialValue = 10001)
	private long id;
	
	@Column(name = "description")
	private String description;

	@Column(name = "amount")
	private double amount;

	@Column(name = "time")
	private LocalDateTime time;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "account_id", nullable = false)
	@JsonIgnore
	private Account account;

	public Transaction() {
	}

	public Transaction(String description, double amount, LocalDateTime time, Account account) {
		this.description = description;
		this.amount = amount;
		this.time = time;
		addToAccount(account);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

		public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
	
	public void addToAccount(Account account) {
		account.getTransactions().add(this);
		this.account = account;
	}
}
