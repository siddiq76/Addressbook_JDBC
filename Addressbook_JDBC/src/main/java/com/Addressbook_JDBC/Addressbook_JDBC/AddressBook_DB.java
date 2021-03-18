package com.Addressbook_JDBC.Addressbook_JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.mysql.cj.jdbc.Driver;


public class AddressBook_DB {

	 private AddressBook_DataBaseService addressBookService;
	List<AddressBookData> addressBookList = new ArrayList<>();
	
	public AddressBook_DB() {
		addressBookService = AddressBook_DataBaseService.getInstance();
	}

	public AddressBook_DB(List<AddressBookData> addressBookList) {
		this();
		this.addressBookList = addressBookList;
	}
	
	public List<AddressBookData> readData() {
		this.addressBookList = addressBookService.readData(); 
		return  addressBookList;
	}

	public void updateAddressBookData(String firstName, String city)
	{
		int result = new AddressBook_DataBaseService().updateEmployeeSalaryResult(firstName, city);
		if(result == 0) return;
		AddressBookData AddressBookData = this.getAddressBookData(firstName);
		if(AddressBookData != null) AddressBookData.setCity(city);
	}
	
	private AddressBookData getAddressBookData(String firstName) {
      AddressBookData addressBookData ;
      addressBookData = this.addressBookList.stream()
    		             .filter(addressBookEntry  ->  (addressBookEntry.getFirstName()).equals(firstName))
    		             .findFirst()
    		             .orElse(null);
		return addressBookData;
	}

	public boolean checkAddressBookDataSyncWithDB(String firstName) {
		try {
			return addressBookService.getAddressBookData(firstName).get(0).getFirstName().equals(getAddressBookData(firstName).getFirstName());
		} catch (IndexOutOfBoundsException e) {
		}
		return false;
	}
}