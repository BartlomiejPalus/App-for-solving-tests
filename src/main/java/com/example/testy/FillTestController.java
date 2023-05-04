package com.example.testy;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.example.testy.SceneSwitcher.switchScene;

public class FillTestController implements Initializable {

	@FXML
	Text nazwaTestuText;
	@FXML
	VBox listaPytanVBox;
	int testID;

	public void setTestID(int testID) {
		this.testID = testID;
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		nazwaTestuText.setText(String.valueOf(testID));

		for (int i=0; i<5; i++) {
			VBox vBox = new VBox();
			vBox.getStyleClass().add("TloWiersza");

			Text questionNumber = new Text("Pytanie "+(i+1));
			questionNumber.getStyleClass().add("NumerPytania");

			HBox hBox = new HBox(questionNumber);
			hBox.setAlignment(Pos.CENTER);

			Text questionContent = new Text("Tresc pytania");
			questionContent.getStyleClass().add("TrescPytania");

			VBox answersVBox = new VBox();
			answersVBox.setSpacing(5);

			ToggleGroup group = new ToggleGroup();

			RadioButton answerRadio1 = new RadioButton("1");
			RadioButton answerRadio2 = new RadioButton("2");
			answerRadio1.setToggleGroup(group);
			answerRadio2.setToggleGroup(group);

			RadioButton answerRadio3 = new RadioButton("3");
			answerRadio3.setToggleGroup(group);

			RadioButton answerRadio4 = new RadioButton("4");
			answerRadio4.setToggleGroup(group);

			answersVBox.getChildren().addAll(answerRadio1, answerRadio2, answerRadio3, answerRadio4);

			vBox.getChildren().addAll(hBox, questionContent, answersVBox);
			listaPytanVBox.getChildren().add(vBox);
		}
	}

	public void onZakonczButtonClick(ActionEvent event) throws IOException {
		switchScene(event, "testResult.fxml");
	}
}
