package com.example.mapssamplefor247.dao;

public class Business {
	private String name, category, phone;
	private int rating;
	private BusinessLocation location;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public BusinessLocation getLocation() {
		return location;
	}

	public void setLocation(BusinessLocation location) {
		this.location = location;
	}
	
}
