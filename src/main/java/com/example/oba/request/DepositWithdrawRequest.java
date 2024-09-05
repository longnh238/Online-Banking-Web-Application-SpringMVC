package com.example.oba.request;

public class DepositWithdrawRequest {
	private double amount;

	public DepositWithdrawRequest() {
	}

	public DepositWithdrawRequest(double amount) {
		this.amount = amount;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
}
