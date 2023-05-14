package com.example.testy;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Base64;

public class DBController {
	Connection connection;
	PreparedStatement statement;
	private final String URL = "jdbc:mysql://127.0.0.1:3306/testy";
	private final String USER = "root";
	private final String PASSWORD = "";

	public DBController() throws SQLException {
		connection = DriverManager.getConnection(URL, USER, PASSWORD);
	}

	public boolean login(String login, String password) throws SQLException, NoSuchAlgorithmException {
		String query = "SELECT COUNT(*) FROM login_data WHERE login = ? AND password = ?";
		statement = connection.prepareStatement(query);
		statement.setString(1, login);
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		byte[] passwordHash = messageDigest.digest(password.getBytes());
		String passwordHashString = Base64.getEncoder().encodeToString(passwordHash);
		statement.setString(2, passwordHashString);
		ResultSet result = statement.executeQuery();
		result.next();
		return result.getInt(1) > 0;
	}

	public boolean register(String login, String password) throws SQLException, NoSuchAlgorithmException {
		String query = "SELECT COUNT(*) FROM login_data WHERE login = ?";
		statement = connection.prepareStatement(query);
		statement.setString(1, login);
		ResultSet result = statement.executeQuery();
		result.next();
		if(result.getInt(1) == 0) {
			query = "INSERT INTO login_data (login, password) VALUES (?, ?)";
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			byte[] passwordHash = messageDigest.digest(password.getBytes());
			String passwordHashString = Base64.getEncoder().encodeToString(passwordHash);
			statement = connection.prepareStatement(query);
			statement.setString(1, login);
			statement.setString(2, passwordHashString);
			statement.execute();
			return true;
		}
		return false;
	}
}
