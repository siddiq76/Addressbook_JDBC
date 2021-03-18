package com.Addressbook_JDBC.Addressbook_JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AddressBook_DataBaseService {
	
	private PreparedStatement addressBookDataStatement;
	private static AddressBook_DataBaseService addressBookDBService;
	
	public static AddressBook_DataBaseService getInstance() {
		if (addressBookDBService == null)
			addressBookDBService = new AddressBook_DataBaseService();
		return addressBookDBService;
	}
	

	private Connection getConnection() throws SQLException {
		String jdbcURL = "jdbc:mysql://localhost:3306/Address_Book_Service?useSSL=false";
		String userName = "root";
		Connection connection;
		System.out.println("Connecting to database : "+jdbcURL);
		connection = DriverManager.getConnection(jdbcURL, userName, "9880115132Sid");
		System.out.println("Connection is successful !! " + connection);
		return connection;
	}
	
	public List<AddressBookData> readData()
	{
		String sql = "Select * from addressBook;";
		List<AddressBookData> addressBookList = new ArrayList<>();
		try(Connection connection = this.getConnection();)
		
		{
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(sql);
		while(resultSet.next()) {
			addressBookList.add(new AddressBookData(resultSet.getString("firstName"), resultSet.getString("lastName"),
					resultSet.getString("address"),resultSet.getString("city"),resultSet.getString("state"),resultSet.getInt("zip"),
					resultSet.getInt("phoneNumber"),resultSet.getString("email") ));
		}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return addressBookList;
	}

	public int updateEmployeeSalaryResult(String firstname, String city) {
		String sql = String.format("update address_table set city ='%s' where firstname = '%s';", city,firstname);
		try(Connection connection = this.getConnection();)
		{
		Statement statement = connection.createStatement();
		return statement.executeUpdate(sql);
				}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	public List<AddressBookData> getAddressBookData(String firstname) {
		List<AddressBookData> addressBookList = new ArrayList<>();
		if (addressBookDataStatement == null)
		{
			prepareStatementForAddressBookData();
		}
		try {
		addressBookDataStatement.setString(1, firstname);
		ResultSet resultSet = addressBookDataStatement.executeQuery();
		while (resultSet.next()) {
			addressBookList.add(new AddressBookData(resultSet.getString("firstName"), resultSet.getString("lastName"),
					resultSet.getString("address"),resultSet.getString("city"),resultSet.getString("state"),resultSet.getInt("zip"),
					resultSet.getInt("phoneNumber"),resultSet.getString("email") ));
         		}
						} catch (Exception e) {
				e.printStackTrace();
			}
			return addressBookList;
		}


	private void prepareStatementForAddressBookData() {
		try {
			Connection connection = getConnection();
			String sql = "SELECT * FROM address WHERE firstName = ?";
			addressBookDataStatement = connection.prepareStatement(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public int updateCityUsingSQL(String firstName, String city) {
		String sql = "UPDATE address_table SET city = ? WHERE firstName = ? ";
		try (Connection connection = getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, city);
			preparedStatement.setString(2, firstName);
			return preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}