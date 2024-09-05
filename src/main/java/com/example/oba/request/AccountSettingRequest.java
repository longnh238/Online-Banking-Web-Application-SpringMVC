package com.example.oba.request;

public class AccountSettingRequest {
	private double dailyLimit;
	private double minBalanceThreshold;

	public AccountSettingRequest() {
	}

	public AccountSettingRequest(double dailyLimit) {
		this.dailyLimit = dailyLimit;
	}

	public double getDailyLimit() {
		return dailyLimit;
	}

	public void setDailyLimit(double dailyLimit) {
		this.dailyLimit = dailyLimit;
	}

	public double getMinBalanceThreshold() {
		return minBalanceThreshold;
	}

	public void setMinBalanceThreshold(double minBalanceThreshold) {
		this.minBalanceThreshold = minBalanceThreshold;
	}
	
	
}
