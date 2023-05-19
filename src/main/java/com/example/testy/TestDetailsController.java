package com.example.testy;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.testy.DBController.getTestDetails;
import static com.example.testy.DBController.register;
import static com.example.testy.SceneSwitcher.switchScene;

public class TestDetailsController {

	private Stage stage;
	private Scene scene;
	private Parent root;

	@FXML
	Text nazwaTestuText;
	@FXML
	VBox vBoxSzczegoly;

	private int testID;

	public void printDetails(int testID) {
		this.testID = testID;

		try {
			ResultSet resultSet = getTestDetails(testID);
			resultSet.next();

			nazwaTestuText.setText(resultSet.getString("name"));
			Text category = new Text("Kategoria: " + resultSet.getString("category"));
			Text questionsAmount = new Text("Ilość pytań w podejściu: " + resultSet.getInt("amountOfQuestionsInApproach"));
			if(resultSet.getInt("amountOfQuestionsInApproach") == -1) {
				questionsAmount.setText("Ilość pytań w podejściu: " + resultSet.getInt("totalQuestionsAmount"));
			}
			Text totalQuestionsAmount = new Text("Całkowita ilość pytań w teście: " + resultSet.getInt("totalQuestionsAmount"));
			Text isRepeatable = new Text("Liczba podejść: " + resultSet.getInt("amountOfApproach"));
			if(resultSet.getInt("amountOfApproach") == -1) {
				isRepeatable.setText("Liczba podejść: nieograniczona");
			}
			Text visability = new Text("Widoczność: publiczny");
			if (!resultSet.getBoolean("isPublic")) {
				visability.setText("Widoczność: prywatny");
			}
			Text creator = new Text("Twórca: " + resultSet.getString("creator"));

			vBoxSzczegoly.getChildren().addAll(category, questionsAmount, totalQuestionsAmount, isRepeatable, visability, creator);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void onWypelnijTestButtonClick(ActionEvent event) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fillTest.fxml"));
		root = fxmlLoader.load();
		stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		double width = ((Node) event.getSource()).getScene().getWidth();
		double height = ((Node) event.getSource()).getScene().getHeight();
		scene = new Scene(root, width, height);
		stage.setScene(scene);

		FillTestController controller = fxmlLoader.getController();
		controller.setTestID(testID);
		stage.show();
	}

	public void onHistoriaTestuButtonClick(ActionEvent event) throws IOException {
		switchScene(event, "testsHistory.fxml");
	}

	public void onWrocButtonClick(ActionEvent event) throws IOException {
		switchScene(event, "listOfTests.fxml");
	}

	public void onHelpClick(ActionEvent event) {

	}
}
