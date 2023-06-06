package com.example.testy;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.testy.SceneSwitcher.switchScene;

public class TestReviewController {

	@FXML
	VBox questionsVBox;
	@FXML
	Text testNameText;

	String source, thsource;
	int testID;

	public void printSolution(int solutionID, String source, String thsource, int testID, String testName) throws SQLException {
		testNameText.setText(testName);
		this.source = source;
		this.thsource = thsource;
		this.testID = testID;
		ResultSet resultSet = DBController.getQuestionsOfSolution(solutionID);
		int i=0;
		while(resultSet.next()) {
			i++;
			VBox vBox = new VBox();
			vBox.getStyleClass().add("TloWiersza");

			Text questionNumber = new Text("Pytanie " + i);
			questionNumber.getStyleClass().add("NumerPytania");
			Region region1 = new Region();
			region1.setPrefWidth(1000);
			Region region2 = new Region();
			region2.setPrefWidth(1000);

			Text punctation = new Text(resultSet.getString("pointsForGood") + "/"
					+ resultSet.getString("pointsForBad"));

			HBox hBox = new HBox(region1, questionNumber, region2, punctation);
			hBox.setAlignment(Pos.CENTER);

			Text questionContent = new Text(resultSet.getString("content"));
			HBox contentHBox = new HBox(questionContent);
			contentHBox.setStyle("-fx-padding: 10px");
			questionContent.getStyleClass().add("TrescPytania");
			VBox answersVBox = drawAnswer(solutionID, resultSet.getInt("id"));

			vBox.getChildren().addAll(hBox, contentHBox, answersVBox);
			questionsVBox.getChildren().add(vBox);
		}
	}

	private VBox drawAnswer(int solutionID, int questionID) throws SQLException {
		ResultSet resultSet = DBController.getAnswersOfSolution(solutionID, questionID);
		VBox answersVBox = new VBox();
		answersVBox.setSpacing(5);
		CheckBox checkBox;
		while (resultSet.next()) {
			checkBox = new CheckBox(resultSet.getString("content"));
			checkBox.setDisable(true);
			checkBox.setSelected(resultSet.getBoolean("isChecked"));
			HBox answerHBox = new HBox(checkBox);
			if(resultSet.getBoolean("isChecked") && !resultSet.getBoolean("isTrue")) {
				answerHBox.getStyleClass().add("BadAnswer");
			}
			else if(resultSet.getBoolean("isTrue")) {
				answerHBox.getStyleClass().add("GoodAnswer");
			}

			answersVBox.getChildren().add(answerHBox);
		}
		return answersVBox;
	}

	public void onWrocButtonClick(ActionEvent event) throws IOException, SQLException {
		FXMLLoader fxmlLoader = switchScene(event, source);

		if(source.equals("testsHistory.fxml")) {
			TestsHistoryController controller = fxmlLoader.getController();
			controller.setParameters(testID, thsource);
		}
		else if(source.equals("testResults.fxml")) {
			TestResultsController controller = fxmlLoader.getController();
			controller.setParameters(testID);
		}
	}

	public void onHelpClick() {
		InstructionOpener.openPage("pokazOdpowiedziEkran.html");
	}
}
