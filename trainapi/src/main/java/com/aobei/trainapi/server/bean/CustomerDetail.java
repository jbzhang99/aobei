package com.aobei.trainapi.server.bean;

import java.util.List;

import com.aobei.train.model.Customer;
import com.aobei.train.model.CustomerAddress;

public class CustomerDetail {
	//顾客信息
	private Customer customer;
	//顾客地址
	private List<CustomerAddress> customerAddress;
	
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public List<CustomerAddress> getCustomerAddress() {
		return customerAddress;
	}
	public void setCustomerAddress(List<CustomerAddress> customerAddress) {
		this.customerAddress = customerAddress;
	}
	
	
}
