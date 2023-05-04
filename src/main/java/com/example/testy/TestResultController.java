package com.example.testy;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.example.testy.SceneSwitcher.switchScene;

public class TestResultController implements Initializable {

	@FXML
	Text wynikText;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {

	}

	public void onPokazOdpowiedziButtonClick(ActionEvent event) throws IOException {
		switchScene(event, "fillTest.fxml");
	}

	public void onRozwiazPonownieButtonClick(ActionEvent event) throws IOException {
		switchScene(event, "fillTest.fxml");
	}

	public void onWrocButtonClick(ActionEvent event) throws IOException {
		switchScene(event, "listOfTests.fxml");
	}

	public void onHelpClick(ActionEvent event) {

	}
}
