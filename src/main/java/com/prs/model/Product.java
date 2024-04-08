package com.prs.model;

import jakarta.persistence.*;

@Entity
public class Product {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne   // need to identify foreign key field name
	@JoinColumn(name="VendorID") 
	public Vendor vendor;
	private String partnumber;
	private String name;
	private float price;
	private String unit;
	private String photopath;
	
	public Product() {
		super();
	}

	public Product(int id, Vendor vendor, String partnumber, String name, float price, String unit, String photopath) {
		super();
		this.id = id;
		this.vendor = vendor;
		this.partnumber = partnumber;
		this.name = name;
		this.price = price;
		this.unit = unit;
		this.photopath = photopath;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public String getPartnumber() {
		return partnumber;
	}

	public void setPartnumber(String partnumber) {
		this.partnumber = partnumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getPhotopath() {
		return photopath;
	}

	public void setPhotopath(String photopath) {
		this.photopath = photopath;
	}
	
	
	
}
