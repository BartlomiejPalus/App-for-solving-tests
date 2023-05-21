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
import java.util.Objects;
import java.util.Optional;

import static com.example.testy.SceneSwitcher.switchScene;

public class FillTestController {

	@FXML
	Text nazwaTestuText;
	@FXML
	VBox listaPytanVBox;
	String testName;
	int testID, amountOfQuestions;

	public void prepareTest(int testID, String testName, int amountOfQuestions) throws SQLException {
		this.testName = testName;
		this.testID = testID;
		this.amountOfQuestions = amountOfQuestions;
		nazwaTestuText.setText(testName);
		drawQuestion(testID);
	}

	public void drawQuestion(int testID) throws SQLException {
		ResultSet resultSet = DBController.getQuestionsForTest(testID, amountOfQuestions);
		int i = 0;
		while(resultSet.next()) {
			i++;
			VBox vBox = new VBox();
			vBox.getStyleClass().add("TloWiersza");

			Text questionNumber = new Text("Pytanie " + i);
			questionNumber.getStyleClass().add("NumerPytania");

			HBox hBox = new HBox(questionNumber);
			hBox.setAlignment(Pos.CENTER);

			Text questionContent = new Text(resultSet.getString("content"));
			questionContent.getStyleClass().add("TrescPytania");

			vBox.getChildren().addAll(hBox, questionContent, drawAnswer(resultSet.getInt("id")));
			listaPytanVBox.getChildren().add(vBox);
		}
	}

	private VBox drawAnswer(int questionID) throws SQLException {
		ResultSet resultSet = DBController.getAnswersOfQuestion(questionID);
		VBox answersVBox = new VBox();
		answersVBox.setSpacing(5);
		while (resultSet.next()) {
			answersVBox.getChildren().add(new CheckBox(resultSet.getString("content")));
		}
		return answersVBox;
	}

	public void onZakonczButtonClick(ActionEvent event) throws IOException {
		ButtonType deleteButton = new ButtonType("Zakończ", ButtonBar.ButtonData.OK_DONE);
		ButtonType cancelButton = new ButtonType("Anuluj", ButtonBar.ButtonData.CANCEL_CLOSE);
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", deleteButton, cancelButton);

		alert.setTitle("Zakończ test");
		alert.setHeaderText("Czy na pewno chcesz zakończyć test?");

		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());

		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("icons/appIcon.png")).openStream()));

		Node discardButton = alert.getDialogPane().lookupButton(deleteButton);
		discardButton.getStyleClass().add("AlertRedButton");

		Optional<ButtonType> option = alert.showAndWait();

		if(option.isPresent()) {
			if (option.get() == deleteButton) {
				FXMLLoader fxmlLoader = switchScene(event, "testResult.fxml");
				TestResultController controller =fxmlLoader.getController();
				controller.fillData(testID, testName, amountOfQuestions);
			}
		}
	}
}
