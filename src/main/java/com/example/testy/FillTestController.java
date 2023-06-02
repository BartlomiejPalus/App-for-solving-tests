package com.example.testy;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.example.testy.SceneSwitcher.switchScene;

public class FillTestController {

	@FXML
	Text testNameText;
	@FXML
	VBox questionsVBox;
	String testName;
	int testID, amountOfQuestions, maxScore = 0;

	public void prepareTest(int testID, String testName, int amountOfQuestions) throws SQLException {
		this.testName = testName;
		this.testID = testID;
		this.amountOfQuestions = amountOfQuestions;
		testNameText.setText(testName);
		drawQuestion(testID);
	}

	public void drawQuestion(int testID) throws SQLException {
		ResultSet resultSet = DBController.getQuestionsForTest(testID, amountOfQuestions);
		int i = 0;
		while(resultSet.next()) {
			i++;
			VBox vBox = new VBox();
			vBox.getStyleClass().add("TloWiersza");
			vBox.setUserData(resultSet.getInt("id"));

			Text questionNumber = new Text("Pytanie " + i);
			questionNumber.getStyleClass().add("NumerPytania");

			HBox hBox = new HBox(questionNumber);
			hBox.setAlignment(Pos.CENTER);

			Text questionContent = new Text(resultSet.getString("content"));
			questionContent.getStyleClass().add("TrescPytania");

			VBox answersVBox = drawAnswer(resultSet.getInt("id"));
			answersVBox.setUserData(new QuestionPoints(resultSet.getInt("pointsForGood"),
					resultSet.getInt("pointsForBad")));

			vBox.getChildren().addAll(hBox, questionContent, answersVBox);
			questionsVBox.getChildren().add(vBox);
		}
	}

	private VBox drawAnswer(int questionID) throws SQLException {
		ResultSet resultSet = DBController.getAnswersOfQuestion(questionID);
		VBox answersVBox = new VBox();
		answersVBox.setSpacing(5);
		CheckBox checkBox;
		while (resultSet.next()) {
			checkBox = new CheckBox(resultSet.getString("content"));
			checkBox.setUserData(resultSet.getBoolean("isTrue"));
			HBox answerHBox = new HBox(checkBox);
			answerHBox.setUserData(resultSet.getInt("id"));
			answersVBox.getChildren().add(answerHBox);
		}
		return answersVBox;
	}

	public void onZakonczButtonClick(ActionEvent event) throws IOException, SQLException {
		ButtonType finishButton = new ButtonType("Zakończ", ButtonBar.ButtonData.OK_DONE);
		ButtonType cancelButton = new ButtonType("Anuluj", ButtonBar.ButtonData.CANCEL_CLOSE);
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", finishButton, cancelButton);

		alert.setTitle("Zakończ test");
		alert.setHeaderText("Czy na pewno chcesz zakończyć test?");

		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());

		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("icons/appIcon.png")).openStream()));

		Node discardButton = alert.getDialogPane().lookupButton(finishButton);
		discardButton.getStyleClass().add("AlertRedButton");

		Optional<ButtonType> option = alert.showAndWait();

		if(option.isPresent()) {
			if (option.get() == finishButton) {
				FXMLLoader fxmlLoader = switchScene(event, "testResult.fxml");
				TestResultController controller =fxmlLoader.getController();
				int score = calculateScore();
				controller.fillData(testID, testName, score, maxScore);
				DBController.addSolution(testID, score, maxScore,getUserAnswers());
			}
		}
	}

	public List<UserAnswerRecord> getUserAnswers() {
		List<UserAnswerRecord> userAnswers = new ArrayList<>();

		for(int i=0; i<questionsVBox.getChildren().size(); i++) {
			VBox question = (VBox) questionsVBox.getChildren().get(i);
			VBox answers = (VBox) question.getChildren().get(2);
			int questionID = (int) question.getUserData();

			for(int j=0; j<answers.getChildren().size(); j++) {
				HBox answer = (HBox) answers.getChildren().get(j);
				int answerID = (int) answer.getUserData();
				CheckBox checkBox = (CheckBox) answer.getChildren().get(0);

				userAnswers.add(new UserAnswerRecord(questionID, answerID, checkBox.isSelected()));
			}
		}

		return userAnswers;
	}

	public int calculateScore() {
		int score = 0;

		for(int i=0; i<questionsVBox.getChildren().size(); i++) {
			VBox question = (VBox) questionsVBox.getChildren().get(i);
			VBox answers = (VBox) question.getChildren().get(2);
			QuestionPoints questionPoints = (QuestionPoints) answers.getUserData();
			maxScore += questionPoints.getPointsForGoodAnswer();
			int userAnswer = 0;
			boolean allGoodChecked = true;

			for(int j=0; j<answers.getChildren().size(); j++) {
				HBox answer = (HBox) answers.getChildren().get(j);
				CheckBox checkBox = (CheckBox) answer.getChildren().get(0);

				if(checkBox.isSelected() && !((Boolean) checkBox.getUserData())) {
					userAnswer = -1;
					break;
				}
				else if(checkBox.isSelected() && ((Boolean) checkBox.getUserData())) {
					userAnswer = 1;
				}
				else if(!checkBox.isSelected() && (Boolean) checkBox.getUserData()) {
					allGoodChecked = false;
				}
			}
			if(userAnswer == -1 || (userAnswer == 1 && !allGoodChecked)) {
				score += questionPoints.getPointsForBadAnswer();
			}
			else if (userAnswer == 1) {
				score += questionPoints.getPointsForGoodAnswer();
			}
		}
		if(score < 0)
			score = 0;
		return score;
	}
}
