package com.example.oba.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.oba.response.ExchangeRatesResponse;

@Service
public class ExchangeRateService {

	@Value("${exchange.rates.api.url}")
	private String apiUrl;
	
	@Value("${exchange.rates.api.app-id}")
    private String appId;

	public ExchangeRatesResponse getExchangeRates(String baseCurrency) {
		String url = apiUrl + "?app_id=" + appId;
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(url, ExchangeRatesResponse.class);
	}
}
