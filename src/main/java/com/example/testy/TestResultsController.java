package com.example.testy;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.example.testy.SceneSwitcher.switchScene;

public class TestResultsController implements Initializable {

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {

	}

	public void onWrocButtonClick(ActionEvent event) throws IOException {
		switchScene(event, "myTests.fxml");
	}

	public void onHelpClick(ActionEvent event) {

	}
}
