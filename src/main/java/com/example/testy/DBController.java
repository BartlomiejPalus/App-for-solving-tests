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
		if(connection == null || connection.isClosed()) {
			final String URL = "jdbc:mysql://127.0.0.1:3306/testy";
			final String USER = "root";
			final String PASSWORD = "";
			connection = DriverManager.getConnection(URL, USER, PASSWORD);
		}
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

	public static void addTest(Test test, int testID) throws SQLException {
		connect();
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

		if (testID == -1) {
			query = "UPDATE test SET originalID = id WHERE id = ?";
			statement = connection.prepareStatement(query);
			statement.setInt(1, resultSet.getInt(1));
		}
		else {
			query = "UPDATE test SET wasEdited = 1 WHERE id = ?";
			statement = connection.prepareStatement(query);
			statement.setInt(1, testID);
			statement.executeUpdate();

			query = "SELECT originalID FROM test WHERE id = ?";
			statement = connection.prepareStatement(query);
			statement.setInt(1, testID);
			ResultSet originalID = statement.executeQuery();
			originalID.next();

			query = "UPDATE test SET originalID = ? WHERE id = ?";
			statement = connection.prepareStatement(query);
			statement.setInt(1, originalID.getInt("originalID"));
			statement.setInt(2, resultSet.getInt(1));
		}
		statement.executeUpdate();

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
		String query = "SELECT originalID FROM test WHERE id = ?";
		statement = connection.prepareStatement(query);
		statement.setInt(1, testID);
		ResultSet resultSet = statement.executeQuery();
		resultSet.next();

		query = "DELETE FROM test WHERE originalID = ?";
		statement = connection.prepareStatement(query);
		statement.setInt(1, resultSet.getInt("originalID"));
		statement.execute();
	}

	public static ResultSet getListOfTests(String condition) throws SQLException {
		connect();
		String query = "SELECT id, name, category, amountOfQuestionsInApproach, (SELECT COUNT(*) FROM question WHERE" +
				" testID = test.id) as totalQuestionsAmount FROM test WHERE wasEdited = 0";
		if(condition != null && !condition.isBlank()) {
			query += " AND " + condition;
		}
		statement = connection.prepareStatement(query);
		return statement.executeQuery();
	}

	public static ResultSet getTestDetails(int testID) throws SQLException {
		connect();
		String query = "SELECT id, name, category, amountOfQuestionsInApproach, (SELECT COUNT(*) FROM question WHERE " +
				"testID = " + testID + ") as totalQuestionsAmount, amountOfApproach, (SELECT COUNT(*) > 0 " +
				"FROM test WHERE password <> '' AND id = " + testID + ") as hasPassword, isPublic, " +
				"(SELECT login FROM login_data WHERE id = (SELECT creatorID FROM test WHERE id = " + testID + ")) " +
				"as creator FROM test WHERE id = " + testID;
		statement = connection.prepareStatement(query);
		return statement.executeQuery();
	}

	public static boolean checkTestPassword(int testID, String password) throws SQLException {
		connect();
		String query = "SELECT COUNT(*) > 0 AS corrrectPassword FROM test WHERE id = ? AND password = ?";
		statement = connection.prepareStatement(query);
		statement.setInt(1, testID);
		statement.setString(2, password);
		ResultSet resultSet = statement.executeQuery();
		resultSet.next();
		return resultSet.getBoolean("corrrectPassword");
	}

	public static ResultSet getQuestionsForTest(int testID, int amountOfQuestions) throws SQLException {
		connect();
		String query = "SELECT id, content, pointsForGood, pointsForBad FROM question WHERE testID = " + testID +
				" ORDER BY RAND()";
		if(amountOfQuestions != -1) {
			query += " LIMIT " + amountOfQuestions;
		}
		statement = connection.prepareStatement(query);
		return statement.executeQuery();
	}

	public static ResultSet getQuestionsForEdit(int testID) throws SQLException {
		connect();
		String query = "SELECT id, content, pointsForGood, pointsForBad FROM question WHERE testID = " + testID;
		statement = connection.prepareStatement(query);
		return statement.executeQuery();
	}

	public static ResultSet getAnswersOfQuestion(int questionID) throws SQLException {
		connect();
		String query = "SELECT id, content, isTrue FROM answer WHERE questionID = " + questionID;
		statement = connection.prepareStatement(query);
		return statement.executeQuery();
	}

	public static ResultSet getTestProperties(int testID) throws SQLException {
		connect();
		String query = "SELECT name, category, amountOfQuestionsInApproach, amountOfApproach, isOverviewable," +
				" password, isPublic FROM test WHERE id = " + testID;
		statement = connection.prepareStatement(query);
		return statement.executeQuery();
	}

	public static int addSolution(int testID, int points, int maxPoints, List<UserAnswerRecord> userAnswers) throws SQLException {
		connect();
		String query = "INSERT INTO solution (testID, userID, points, maxPoints) VALUES (?, ?, ?, ?)";
		statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		statement.setInt(1, testID);
		statement.setInt(2, Account.getInstance().getId());
		statement.setInt(3, points);
		statement.setInt(4, maxPoints);

		statement.executeUpdate();
		ResultSet resultSet = statement.getGeneratedKeys();
		resultSet.next();
		addUserAnswers(resultSet.getInt(1), userAnswers);
		return resultSet.getInt(1);
	}

	public static void addUserAnswers(int solutionID, List<UserAnswerRecord> userAnswers) throws SQLException {
		String query = "INSERT INTO user_answer (solutionID, questionID, answerID, isChecked) VALUES (?, ?, ?, ?)";
		for(UserAnswerRecord answer : userAnswers) {
			statement = connection.prepareStatement(query);
			statement.setInt(1, solutionID);
			statement.setInt(2, answer.questionID());
			statement.setInt(3, answer.answerID());
			statement.setBoolean(4, answer.isChecked());
			statement.executeUpdate();
		}
	}

	public static ResultSet getUserSolutions(String condition) throws SQLException {
		connect();
		String query = "SELECT s.id as solutionID, t.id as testID, t.name, t.category, s.points, s.maxPoints FROM solution s " +
				"JOIN test t ON s.testID = t.id WHERE s.userID = ?";
		if(condition != null && !condition.isBlank()) {
			query += condition;
		}
		statement = connection.prepareStatement(query);
		statement.setInt(1, Account.getInstance().getId());
		return statement.executeQuery();
	}

	public static ResultSet getQuestionsOfSolution(int solutionID) throws SQLException {
		connect();
		String query = "SELECT DISTINCT q.id, q.content, q.pointsForGood, q.pointsForBad FROM solution s " +
				"JOIN user_answer ua ON s.id = ua.solutionID JOIN question q ON ua.questionID = q.id WHERE s.id = ?";
		statement = connection.prepareStatement(query);
		statement.setInt(1, solutionID);
		return statement.executeQuery();
	}

	public static ResultSet getAnswersOfSolution(int solutionID, int questionID) throws SQLException {
		connect();
		String query = "SELECT DISTINCT a.content, a.isTrue, ua.isChecked FROM solution s " +
				"JOIN user_answer ua ON s.id = ua.solutionID JOIN answer a ON ua.answerID = a.id " +
				"WHERE s.id = ? AND ua.questionID = ?";
		statement = connection.prepareStatement(query);
		statement.setInt(1, solutionID);
		statement.setInt(2, questionID);
		return statement.executeQuery();
	}

	public static ResultSet getTestSolutions(int testID, String condition) throws SQLException {
		connect();
		String query = "SELECT s.id AS solutionID, t.id AS testID, t.name, t.category, s.points, s.maxPoints, ld.login " +
				"FROM solution s JOIN test t ON s.testID = t.id JOIN login_data ld ON ld.id = s.userID WHERE s.testID = ?";
		if(condition != null && !condition.isBlank()) {
			query += condition;
		}
		System.out.println(query);
		statement = connection.prepareStatement(query);
		statement.setInt(1, testID);
		return statement.executeQuery();
	}
}
