package com.flipkart.customer.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "customers")
public class CustomerEntity
{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "customer_id")
	private Long customerId;

	@Column(name = "name", nullable = false, length = 100)
	private String name;

	@Column(name = "email", nullable = false, unique = true, length = 100)
	private String email;

	@Column(name = "mobile", nullable = false, unique = true, length = 15)
	private String mobile;

	/*
	 * Stores the BCrypt-encoded password. The raw password received from the API
	 * request must never be stored here.
	 */
	@Column(name = "password", nullable = false, length = 255)
	private String password;

	/*
	 * createdAt is set only when a customer is first persisted. updatable = false
	 * prevents JPA from changing this value during updates.
	 */
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	/*
	 * updatedAt is initialized during creation and refreshed automatically whenever
	 * the entity is updated.
	 */
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	/*
	 * Executes before the entity is inserted into the database. Both timestamps
	 * start with the same value for a newly created customer.
	 */
	@PrePersist
	protected void onCreate()
	{
		LocalDateTime now = LocalDateTime.now();
		createdAt = now;
		updatedAt = now;
	}

	/*
	 * Executes before an existing customer is updated. Only updatedAt changes;
	 * createdAt remains unchanged.
	 */
	@PreUpdate
	protected void onUpdate()
	{
		updatedAt = LocalDateTime.now();
	}

	public Long getCustomerId()
	{
		return customerId;
	}

	public void setCustomerId(Long customerId)
	{
		this.customerId = customerId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getMobile()
	{
		return mobile;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public LocalDateTime getCreatedAt()
	{
		return createdAt;
	}

	public LocalDateTime getUpdatedAt()
	{
		return updatedAt;
	}
}