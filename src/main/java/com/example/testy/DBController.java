package com.example.testy;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Base64;
import java.util.List;

public class DBController {
	private static Connection connection;
	private static PreparedStatement statement;

	private static void connect() throws SQLException {
		final String URL = "jdbc:mysql://127.0.0.1:3306/testy";
		final String USER = "root";
		final String PASSWORD = "";
		connection = DriverManager.getConnection(URL, USER, PASSWORD);
	}

	public static int login(String login, String password) throws SQLException, NoSuchAlgorithmException {
		connect();
		String query = "SELECT id FROM login_data WHERE login = ? AND password = ?";
		statement = connection.prepareStatement(query);
		statement.setString(1, login);
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		byte[] passwordHash = messageDigest.digest(password.getBytes());
		String passwordHashString = Base64.getEncoder().encodeToString(passwordHash);
		statement.setString(2, passwordHashString);
		ResultSet result = statement.executeQuery();
		if(result.next()) {
			return result.getInt(1);
		}
		else {
			return -1;
		}
	}

	public static boolean register(String login, String password) throws SQLException, NoSuchAlgorithmException {
		connect();
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
			statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, login);
			statement.setString(2, passwordHashString);

			statement.executeUpdate();
			result = statement.getGeneratedKeys();
			result.next();
			int id = result.getInt(1);
			Account.getInstance().logIn(login, id);
			return true;
		}
		return false;
	}

	public static void addTest(Test test) throws SQLException {
		connect();//todo
		String query = "INSERT INTO test (name, category, amountOfQuestionsInApproach, amountOfApproach, isOverviewable," +
				"password, isPublic, creatorID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		statement.setString(1, test.getName());
		statement.setString(2, test.getCategory());
		statement.setInt(3, test.getAmountOfQuestionsInApproach());
		statement.setInt(4, test.getAmountOfApproach());
		statement.setBoolean(5, test.isOverviewable());
		statement.setString(6, test.getPassword());
		statement.setBoolean(7, test.isPublic());
		statement.setInt(8, Account.getInstance().getId());

		statement.executeUpdate();
		ResultSet resultSet = statement.getGeneratedKeys();
		resultSet.next();

		addQuestions(resultSet.getInt(1), test.getQuestionsList());
	}

	private static void addQuestions(int testID, List<Question> listOfQuestions) throws SQLException {
		String query = "INSERT INTO question (content, pointsForGood, pointsForBad, testID) VALUES (?, ?, ?, ?)";
		for(Question question : listOfQuestions) {
			statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, question.getContent());
			statement.setInt(2, question.getGoodAnswerPoints());
			statement.setInt(3, question.getBadAnswerPoints());
			statement.setInt(4, testID);
			statement.executeUpdate();
			ResultSet resultSet = statement.getGeneratedKeys();
			resultSet.next();
			addAnswers(resultSet.getInt(1), question.getAnswers());
		}
	}

	private static void addAnswers(int questionID, List<Answer> listOfAnswers) throws SQLException {
		String query = "INSERT INTO answer (content, isTrue, questionID) VALUES (?, ?, ?)";
		for(Answer answer : listOfAnswers) {
			statement = connection.prepareStatement(query);
			statement.setString(1, answer.getContent());
			statement.setBoolean(2, answer.getCorrect());
			statement.setInt(3, questionID);
			statement.executeUpdate();
		}
	}

	public static void deleteTest(int testID) throws SQLException {
		String query = "DELETE FROM test WHERE id = ?";
		statement = connection.prepareStatement(query);
		statement.setInt(1, testID);
		statement.execute();
	}

	public static ResultSet getListOfTests(String condition) throws SQLException {
		connect();
		String query = "SELECT id, name, category, amountOfQuestionsInApproach, (SELECT COUNT(*) FROM question WHERE" +
				" testID = test.id) as totalQuestionsAmount FROM test";
		if(condition != null && !condition.isBlank()) {
			query += " WHERE " + condition;
		}
		statement = connection.prepareStatement(query);
		return statement.executeQuery();
	}

	public static ResultSet getTestDetails(int testID) throws SQLException {
		connect();
		String query = "SELECT id, name, category, amountOfQuestionsInApproach, (SELECT COUNT(*) FROM question WHERE testID = " +
				testID + ") as totalQuestionsAmount, amountOfApproach, isPublic, (SELECT login FROM login_data WHERE " +
				"id = (SELECT creatorID FROM test WHERE id = " + testID + ")) as creator FROM test WHERE id = " + testID;
		statement = connection.prepareStatement(query);
		return statement.executeQuery();
	}
}
