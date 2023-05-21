package com.example.testy;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.io.IOException;

import static com.example.testy.SceneSwitcher.switchScene;

public class TestResultController {

	@FXML
	Text wynikText, nazwaTestuText;

	public void fillData(int testID, String testName, int amountOfQuestionsInApproach) {
		nazwaTestuText.setText(testName);
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
