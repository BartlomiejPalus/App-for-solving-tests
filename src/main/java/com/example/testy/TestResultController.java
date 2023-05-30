package com.example.testy;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.text.Text;

import java.io.IOException;

import static com.example.testy.SceneSwitcher.switchScene;

public class TestResultController {

	@FXML
	Text wynikText, nazwaTestuText;

	int testID;

	public void fillData(int testID, String testName, int score, int maxScore) {
		this.testID = testID;
		nazwaTestuText.setText(testName);
		wynikText.setText("Wynik testu:\n" + score + "/" + maxScore + "(" +
				((double) score / (double) maxScore * 100) + "%)");
	}

	public void onPokazOdpowiedziButtonClick(ActionEvent event) throws IOException {
		switchScene(event, "fillTest.fxml");
	}

	public void onRozwiazPonownieButtonClick(ActionEvent event) throws IOException {
		FXMLLoader loader = switchScene(event, "testDetails.fxml");
		TestDetailsController controller = loader.getController();
		controller.printDetails(testID);
	}

	public void onWrocButtonClick(ActionEvent event) throws IOException {
		switchScene(event, "listOfTests.fxml");
	}

	public void onHelpClick(ActionEvent event) {

	}
}
