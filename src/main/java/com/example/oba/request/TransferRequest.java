package com.example.oba.request;

public class TransferRequest {
	private long destinationAccountId;
	private double amount;
	
	public TransferRequest() {	
	}
	
	public TransferRequest(long destinationAccountId, double amount) {
		this.destinationAccountId = destinationAccountId;
		this.amount = amount;
	}

	public long getDestinationAccountId() {
		return destinationAccountId;
	}

	public void setDestinationAccountId(long destinationAccountId) {
		this.destinationAccountId = destinationAccountId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
}
