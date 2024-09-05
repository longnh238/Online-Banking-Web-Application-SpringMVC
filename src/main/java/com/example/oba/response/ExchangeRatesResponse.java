package com.example.oba.response;

import java.time.LocalDateTime;
import java.util.Map;

public class ExchangeRatesResponse {
	private String base;
	private LocalDateTime time;
	private Map<String, Double> rates;

	public ExchangeRatesResponse() {
	}

	public ExchangeRatesResponse(String base, LocalDateTime time, Map<String, Double> rates) {
		this.base = base;
		this.time = time;
		this.rates = rates;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	public Map<String, Double> getRates() {
		return rates;
	}

	public void setRates(Map<String, Double> rates) {
		this.rates = rates;
	}
}
