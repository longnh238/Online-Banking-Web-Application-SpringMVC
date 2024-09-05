package com.example.oba.response;

public class DestinationAccountResponse {
	private long id;
	private long customerId;
	private String customerName;

	public DestinationAccountResponse() {
	}

	public DestinationAccountResponse(long id, long customerId, String customerName) {
		this.id = id;
		this.customerId = customerId;
		this.customerName = customerName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
}
