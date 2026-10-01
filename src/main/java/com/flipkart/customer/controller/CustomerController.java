package com.flipkart.customer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flipkart.customer.request.CustomerRequest;
import com.flipkart.customer.request.CustomerResponse;
import com.flipkart.customer.service.CustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/customers")
public class CustomerController
{

	private final CustomerService customerService;

	public CustomerController(CustomerService customerService)
	{
		this.customerService = customerService;
	}

	@PostMapping("/register")
	public ResponseEntity<CustomerResponse> registerCustomer(@Valid @RequestBody CustomerRequest request)
	{

		CustomerResponse response = customerService.registerCustomer(request);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}