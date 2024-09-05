package com.example.oba.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.oba.model.entity.Customer;
import com.example.oba.model.repository.CustomerRepository;
import com.example.oba.request.LoginRequest;
import com.example.oba.request.PasswordResetRequest;
import com.example.oba.response.MessageResponse;
import com.example.oba.utils.SecurityUtils;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/" + "${app.version}")
public class AuthenticationController {

	@Autowired
	CustomerRepository customerRepository;

	@PostMapping("login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginData) {
		try {
			MessageResponse message;

			long customerId = loginData.getCustomerId();
			Optional<Customer> customer = customerRepository.findById(customerId);
			if (customer.isEmpty()) {
				message = new MessageResponse("The customer does not exist");
				return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
			} else { 
				if (SecurityUtils.getInstance().match(loginData.getPassword(), customer.get().getPassword())) {
					return new ResponseEntity<>(customer.get(), HttpStatus.OK);
				} else {
					message = new MessageResponse("Password mismatch");
					return new ResponseEntity<>(message, HttpStatus.FORBIDDEN);
				} 
			}
		} catch (Exception ex) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("password-reset")
	public ResponseEntity<Object> passwordReset(@RequestBody PasswordResetRequest passwordResetData) {
		try {		
			long customerId = passwordResetData.getCustomerId();
			Optional<Customer> customer = customerRepository.findById(customerId);
			if (customer.isEmpty()) {
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			} else {
				customer.get().setPassword(passwordResetData.getPassword());
				return new ResponseEntity<>(customerRepository.save(customer.get()), HttpStatus.OK);
			}
		} catch (Exception ex) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
