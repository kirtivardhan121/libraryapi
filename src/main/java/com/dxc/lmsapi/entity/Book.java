package com.dxc.lmsapi.entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity
@Table(name = "books")
public class Book implements Serializable {

	@Id
	@Column(name = "bcode")
	@NotNull(message = "bcode can not be null")
	private int bcode;

	@Column(name = "bname", nullable = false)
	@NotBlank(message = "bname can not be blank")
	@Size(min = 4, max = 20, message = "bname must be of 4 to 20 chars in length")
	private String bname;

	@Column(name = "price", nullable = false)
	@NotNull(message = "price can not be blank")
	@Min(value = 100, message = "price is expected to be not less than 100")
	@Max(value = 25000, message = "price is expected not more than 25000")
	private double price;

	@Column(name = "pdate", nullable = true)
	@NotNull(message = "packageDate can not be blank")
	@PastOrPresent(message = "Package date can not be a future date")
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate packageDate;

	public Book() {
		// Left unimplemented
	}

	public Book(@NotNull(message = "bcode can not be null") int bcode,
			@NotBlank(message = "bname can not be blank") @Size(min = 5, max = 45, message = "bname must be of 5 to 45 chars in length") String bname,
			@NotNull(message = "price can not be blank") @Min(value = 100, message = "price is expected to be not less than 100") @Max(value = 25000, message = "price is expected not more than 25000") double price,
			@NotNull(message = "packageDate can not be blank") @PastOrPresent(message = "Package date can not be a future date") LocalDate packageDate) {
		super();
		this.bcode = bcode;
		this.bname = bname;
		this.price = price;
		this.packageDate = packageDate;
	}

	public int getBcode() {
		return bcode;
	}

	public void setBcode(int bcode) {
		this.bcode = bcode;
	}

	public String getBname() {
		return bname;
	}

	public void setBname(String bname) {
		this.bname = bname;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public LocalDate getPackageDate() {
		return packageDate;
	}

	public void setPackageDate(LocalDate packageDate) {
		this.packageDate = packageDate;
	}

}
