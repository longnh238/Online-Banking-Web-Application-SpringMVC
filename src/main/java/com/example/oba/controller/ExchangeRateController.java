package com.example.oba.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.oba.response.ExchangeRatesResponse;
import com.example.oba.service.ExchangeRateService;
import com.example.oba.utils.ConstantUtils;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/" + "${app.version}")
public class ExchangeRateController {

	@Autowired
	private ExchangeRateService exchangeRateService;

	@GetMapping("/exchange-rates")
	public ResponseEntity<ExchangeRatesResponse> getExchangeRates(@RequestParam(required = false) String baseCurrency) {
		try {
			// Free plan is only allowed base as "USD"
			baseCurrency = ConstantUtils.base;
			ExchangeRatesResponse exchangeRatesResponse = exchangeRateService.getExchangeRates(baseCurrency);
			exchangeRatesResponse.setTime(LocalDateTime.now());
			return new ResponseEntity<>(exchangeRatesResponse, HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
