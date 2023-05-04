package com.example.testy;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.example.testy.SceneSwitcher.switchScene;

public class TestDetailsController implements Initializable {

	private Stage stage;
	private Scene scene;
	private Parent root;

	@FXML
	Text nazwaTestuText;
	@FXML
	VBox vBoxSzczegoly;

	private int testID;

	public void setTestID(int testID) {
		this.testID = testID;
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

	public void onEdytujTestButtonClick(ActionEvent event) throws IOException {
		switchScene(event, "addTest.fxml");
	}

	public void onWrocButtonClick(ActionEvent event) throws IOException {
		switchScene(event, "listOfTests.fxml");
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		nazwaTestuText.setText(String.valueOf(testID));

		Text category = new Text("Kategoria: " + "Matematyka");
		category.getStyleClass().add("SzczegolyTestuText");
		Text questionsAmount = new Text("Ilość pytań w podejściu: " + "10");
		questionsAmount.getStyleClass().add("SzczegolyTestuText");
		Text totalQuestionsAmount = new Text("Całkowita ilość pytań w teście: " + "50");
		totalQuestionsAmount.getStyleClass().add("SzczegolyTestuText");
		Text isRepeatable = new Text("Możliwość wielokrotnego rozwiązywania: " + "tak");
		isRepeatable.getStyleClass().add("SzczegolyTestuText");
		Text visability = new Text("Widoczność: " + "publiczny");
		visability.getStyleClass().add("SzczegolyTestuText");
		Text creator = new Text("Twórca: " + "Użytkownik");
		creator.getStyleClass().add("SzczegolyTestuText");

		vBoxSzczegoly.getChildren().addAll(category, questionsAmount, totalQuestionsAmount, isRepeatable, visability, creator);
	}

	public void onHelpClick(ActionEvent event) {

	}
}
