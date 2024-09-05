package com.example.oba.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.oba.model.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	List<Customer> findByFirstName(String firstName);

	List<Customer> findByLastName(String lastName);
}
