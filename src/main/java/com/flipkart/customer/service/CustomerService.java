package com.flipkart.customer.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.flipkart.customer.entity.CustomerEntity;
import com.flipkart.customer.exception.DuplicateEmailException;
import com.flipkart.customer.exception.DuplicateMobileException;
import com.flipkart.customer.repository.CustomerRepository;
import com.flipkart.customer.request.CustomerRequest;
import com.flipkart.customer.response.CustomerResponse;

@Service
public class CustomerService
{

	private final CustomerRepository customerRepository;
	private final PasswordEncoder passwordEncoder;

	public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder)
	{
		this.customerRepository = customerRepository;
		this.passwordEncoder = passwordEncoder;
	}

	// Register a new customer
	public CustomerResponse registerCustomer(CustomerRequest request)
	{

		// Check duplicate email
		if (customerRepository.existsByEmail(request.getEmail()))
		{
			throw new DuplicateEmailException("Email already registered");
		}

		// Check duplicate mobile
		if (customerRepository.existsByMobile(request.getMobile()))
		{
			throw new DuplicateMobileException("Mobile number already registered");
		}

		// Convert Request DTO -> Entity
		CustomerEntity customer = mapToEntity(request);

		/*
		 * Encode the raw password before it reaches the database.
		 */
		customer.setPassword(passwordEncoder.encode(request.getPassword()));

		// Save customer in database
		CustomerEntity savedCustomer = customerRepository.save(customer);

		// Convert Entity -> Response DTO
		return mapToResponse(savedCustomer);
	}

	// Convert CustomerRequest to CustomerEntity
	private CustomerEntity mapToEntity(CustomerRequest request)
	{

		CustomerEntity customer = new CustomerEntity();

		customer.setName(request.getName());
		customer.setEmail(request.getEmail());
		customer.setMobile(request.getMobile());

		return customer;
	}

	// Convert CustomerEntity to CustomerResponse
	private CustomerResponse mapToResponse(CustomerEntity customer)
	{

		CustomerResponse response = new CustomerResponse();

		response.setCustomerId(customer.getCustomerId());
		response.setName(customer.getName());
		response.setEmail(customer.getEmail());
		response.setMobile(customer.getMobile());
		response.setCreatedAt(customer.getCreatedAt());

		return response;
	}
}