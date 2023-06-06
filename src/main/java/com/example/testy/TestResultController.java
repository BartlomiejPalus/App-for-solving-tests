package com.example.testy;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;

import static com.example.testy.SceneSwitcher.switchScene;

public class TestResultController {

	@FXML
	Text wynikText, nazwaTestuText;

	int testID, solutionID;

	public void fillData(int testID, int solutionID, String testName, int score, int maxScore) {
		this.testID = testID;
		this.solutionID = solutionID;
		nazwaTestuText.setText(testName);
		Double percent = ((double) score / (double) maxScore * 100);
		DecimalFormat format = new DecimalFormat("#.#");
		String formattedPercent = format.format(percent);
		wynikText.setText("Wynik testu:\n" + score + "/" + maxScore + " (" + formattedPercent + "%)");
	}

	public void onPokazOdpowiedziButtonClick(ActionEvent event) throws IOException, SQLException {
		FXMLLoader fxmlLoader = switchScene(event, "testReview.fxml");
		TestReviewController controller = fxmlLoader.getController();
		controller.printSolution(solutionID, "listOfTests.fxml", null, testID, nazwaTestuText.getText());
	}

	public void onRozwiazPonownieButtonClick(ActionEvent event) throws IOException {
		FXMLLoader loader = switchScene(event, "testDetails.fxml");
		TestDetailsController controller = loader.getController();
		controller.printDetails(testID, "listOfTests.fxml");
	}

	public void onWrocButtonClick(ActionEvent event) throws IOException {
		switchScene(event, "listOfTests.fxml");
	}

	public void onHelpClick() {
		InstructionOpener.openPage("wynikTestuEkran.html");
	}
}
