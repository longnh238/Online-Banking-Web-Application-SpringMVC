package com.example.oba.model.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.example.oba.type.AccountStatus;
import com.example.oba.type.AccountType;
import com.example.oba.type.CustomerStatus;
import com.example.oba.type.RequestType;
import com.example.oba.utils.ConstantUtils;
import com.example.oba.utils.SecurityUtils;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_sequence")
	@SequenceGenerator(name = "user_id_sequence", sequenceName = "user_id_sequence", allocationSize = 1, initialValue = 10001)
	private long id;

	@Column(name = "password")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "date_of_birth")
	private LocalDate dateOfBirth;

	@Column(name = "email", unique = true)
	private String email;

	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "address")
	private String address;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private CustomerStatus status;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "customer", cascade = CascadeType.ALL)
	private Set<Account> accounts = new HashSet<>();

	public Customer() {
	}

	public Customer(String password, String firstName, String lastName, LocalDate dateOfBirth, String email,
			String phoneNumber, String address) {
		setPassword(password);
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.status = CustomerStatus.INACTIVE;
		createDefaultAccount();
	}

	// Only for adding customers when starting the server
	// Create accounts with defined settings
	public Customer(String password, String firstName, String lastName, LocalDate dateOfBirth, String email,
			String phoneNumber, String address, Account account) {
		setPassword(password);
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.status = CustomerStatus.ACTIVATED;
		this.addAccount(account);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = SecurityUtils.getInstance().encode(password);
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public CustomerStatus getStatus() {
		return status;
	}

	public void setStatus(CustomerStatus status) {
		this.status = status;
	}

	public Set<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(Set<Account> accounts) {
		this.accounts = accounts;
	}

	public void addAccount(Account account) {
		this.accounts.add(account);
		account.setCustomer(this);
	}

	public void copyData(Customer data, RequestType requestType) {
		if (requestType == RequestType.POST) {
			this.setPassword(data.getPassword());
		}
		this.setFirstName(data.getFirstName());
		this.setLastName(data.getLastName());
		this.setDateOfBirth(data.getDateOfBirth());
		this.setEmail(data.getEmail());
		this.setPhoneNumber(data.getPhoneNumber());
		this.setAddress(data.getAddress());
	}

	public void createDefaultAccount() {
		Account account = new Account(AccountType.CHEQUING, 0, LocalDate.now(), null, AccountStatus.ACTIVE,
				ConstantUtils.DAILY_LIMIT, ConstantUtils.MIN_BALANCE_THRESHOLD);
		this.addAccount(account);
	}
}
