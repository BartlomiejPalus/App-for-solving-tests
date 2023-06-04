package com.example.testy;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.testy.DBController.getTestDetails;
import static com.example.testy.SceneSwitcher.switchScene;

public class TestDetailsController {

	@FXML
	Text testNameText, attentionText;
	@FXML
	VBox vBoxDetails;
	@FXML
	PasswordField testPasswordField;
	@FXML
	Button fillTestButton;

	private int testID, amountOfQuestionsInApproach;
	private String testName, source;

	public void printDetails(int testID, String source) {
		this.testID = testID;
		this.source = source;
		try {
			ResultSet resultSet = getTestDetails(testID);
			resultSet.next();
			testName = resultSet.getString("name");
			testNameText.setText(testName);
			Text category = new Text("Kategoria: " + resultSet.getString("category"));
			amountOfQuestionsInApproach = resultSet.getInt("amountOfQuestionsInApproach");
			Text questionsAmount = new Text("Ilość pytań w podejściu: " + amountOfQuestionsInApproach);
			if(resultSet.getInt("amountOfQuestionsInApproach") == -1) {
				questionsAmount.setText("Ilość pytań w podejściu: " + resultSet.getInt("totalQuestionsAmount"));
			}
			Text totalQuestionsAmount = new Text("Całkowita ilość pytań w teście: " + resultSet.getInt("totalQuestionsAmount"));

			int amountOfApproaches = resultSet.getInt("amountOfApproach");
			int usedApproaches = resultSet.getInt("usedApproaches");
			Text isRepeatable = new Text("Liczba podejść: nieograniczona");
			if(amountOfApproaches != -1) {
				isRepeatable.setText("Wykorzystane podejścia: " + usedApproaches + "/" + amountOfApproaches);
				if(usedApproaches >= amountOfApproaches) {
					fillTestButton.setDisable(true);
				}
			}

			Text visability = new Text("Widoczność: publiczny");
			if (!resultSet.getBoolean("isPublic")) {
				visability.setText("Widoczność: prywatny");
			}
			Text creator = new Text("Twórca: " + resultSet.getString("creator"));

			vBoxDetails.getChildren().addAll(category, questionsAmount, totalQuestionsAmount, isRepeatable, visability, creator);
			if(resultSet.getBoolean("hasPassword")) {
				testPasswordField.setVisible(true);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void onWypelnijTestButtonClick(ActionEvent event) throws IOException, SQLException {
		if(testPasswordField.isVisible()) {
			if(DBController.checkTestPassword(testID, testPasswordField.getText())) {
				FXMLLoader fxmlLoader = switchScene(event, "fillTest.fxml");
				FillTestController controller = fxmlLoader.getController();
				controller.prepareTest(testID, testName, amountOfQuestionsInApproach);
			}
			else {
				attentionText.setText("Błędne hasło");
			}
		}
		else {
			FXMLLoader fxmlLoader = switchScene(event, "fillTest.fxml");
			FillTestController controller = fxmlLoader.getController();
			controller.prepareTest(testID, testName, amountOfQuestionsInApproach);
		}
	}

	public void onHistoriaTestuButtonClick(ActionEvent event) throws IOException, SQLException {
		FXMLLoader fxmlLoader = switchScene(event, "testsHistory.fxml");
		TestsHistoryController controller = fxmlLoader.getController();
		controller.setParameters(testID, "testDetails.fxml");
	}

	public void onWrocButtonClick(ActionEvent event) throws IOException {
		switchScene(event, source);
	}

	public void onHelpClick(ActionEvent event) {

	}
}
