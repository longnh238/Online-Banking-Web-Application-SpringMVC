package com.example.oba.request;

public class PasswordResetRequest {
	private long customerId;
	private String password;
	
	public PasswordResetRequest() {
	}
	
	public PasswordResetRequest(long customerId, String password) {
		this.customerId = customerId;
		this.password = password;
	}
	
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
