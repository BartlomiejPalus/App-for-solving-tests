package com.example.testy;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.io.IOException;

import static com.example.testy.SceneSwitcher.switchScene;

public class MainMenuController {

	public void onListaTestowButtonClick(ActionEvent event) throws IOException {
		switchScene(event, "listOfTests.fxml");
	}

	public void onMojeTestyButtonClick(ActionEvent event) throws IOException {
		switchScene(event, "myTests.fxml");
	}

	public void onHistoriaTestowButtonClick(ActionEvent event) throws IOException {
		switchScene(event, "testsHistory.fxml");
	}

	public void onWylogujButtonClick(ActionEvent event) throws IOException {
		switchScene(event, "startWindow.fxml");
	}

	public void onWyjdzButtonClick(ActionEvent event) {
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.close();
	}

	public void onHelpClick(ActionEvent event) {

	}
}
