package com.example.testy;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static com.example.testy.SceneSwitcher.switchScene;

public class AddTestController implements Initializable {

	@FXML
	VBox questionsVBox;
	@FXML
	TextField defaultGoodTextField, defaultBadTextField, testNameTextField, amountOfQuestionsInApproachTextField,
			amountOfApproachTextField, passwordTextField;
	@FXML
	ComboBox<String> categoryComboBox, isOverviewableComboBox, visabilityComboBox;
	@FXML
	CheckBox amountOfQuestionsInApproachCheckBox, amountOfApproachCheckBox;
	@FXML
	Text attentionText;
	@FXML
	Label attentionLabel;

	private List<Question> questionsList;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		addQuestion();
	}

	public void onDodajPytanieButtonClick() {
		addQuestion();
	}

	public void addQuestion() {
		VBox questionVBox = new VBox();
		questionVBox.setSpacing(10);
		questionVBox.getStyleClass().add("TloWiersza");

		HBox hBox = new HBox();
		hBox.setSpacing(10);
		hBox.setAlignment(Pos.CENTER);

		HBox points = new HBox();
		points.setSpacing(5);
		TextField goodAnswer = new TextField();
		TextField badAnswer = new TextField();
		goodAnswer.setPromptText("+");
		badAnswer.setPromptText("-");
		goodAnswer.getStyleClass().add("PointsTextField");
		badAnswer.getStyleClass().add("PointsTextField");
		Text separator = new Text("/");
		separator.getStyleClass().add("Separator");
		points.getChildren().addAll(goodAnswer, separator, badAnswer);

		Region r1 = new Region();
		Region r2 = new Region();
		Text questionNumber = new Text("Pytanie " + questionsVBox.getChildren().size());
		questionNumber.getStyleClass().add("NumerPytania");
		Button deleteQuestionButton = new Button("-");

		deleteQuestionButton.getStyleClass().add("Usun");

		hBox.getChildren().addAll(r1, questionNumber, deleteQuestionButton, r2, points);
		questionVBox.getChildren().add(hBox);
		HBox.setHgrow(r1, Priority.ALWAYS);
		HBox.setHgrow(r2, Priority.ALWAYS);
		r1.setPadding(new Insets(0,100,0, 0));
		TextField questionContent = new TextField();
		questionContent.setPromptText("Treść pytania");
		questionVBox.getChildren().add(questionContent);

		VBox answersVBox = new VBox();
		answersVBox.setSpacing(5);
		addAnswer(answersVBox);

		Button addAnswerButton = new Button("Dodaj odpowiedź");
		addAnswerButton.setOnAction(e -> {
			addAnswer(answersVBox);
		});

		deleteQuestionButton.setOnAction(e -> {
			questionsVBox.getChildren().remove(questionVBox);
			updateQuestionNumbering();
		});

		questionVBox.getChildren().addAll(answersVBox, addAnswerButton);
		questionsVBox.getChildren().add(questionVBox);

		updateQuestionNumbering();
	}

	public void addAnswer(VBox answersVBox) {
		HBox answersHBox = new HBox();
		answersHBox.setAlignment(Pos.CENTER_LEFT);
		answersHBox.setSpacing(10);

		TextField textField = new TextField();
		textField.setPromptText("Wprowadź odpowiedź");

		Button deleteAnswerButton = new Button("-");
		deleteAnswerButton.getStyleClass().add("Usun");

		deleteAnswerButton.setOnAction(e -> {
			answersVBox.getChildren().remove(answersHBox);
			updateAnswerNumbering(answersVBox);
		});
		answersHBox.getChildren().addAll(new CheckBox(), new Text((answersVBox.getChildren().size()+1) + "."), textField, deleteAnswerButton);
		answersVBox.getChildren().add(answersHBox);
	}

	public void updateQuestionNumbering() {
		for(int i = 0; i< questionsVBox.getChildren().size(); i++) {
			VBox vBox = (VBox) questionsVBox.getChildren().get(i);
			HBox hBox = (HBox) vBox.getChildren().get(0);
			Text text = (Text) hBox.getChildren().get(1);
			text.setText("Pytanie " + (i + 1));
			VBox answersVBox = (VBox) vBox.getChildren().get(2);
			answersVBox.setUserData(i);
		}
	}

	public void updateAnswerNumbering(VBox answersVBox) {
		for( int i = 0; i<answersVBox.getChildren().size(); i++) {
			HBox hBox = (HBox) answersVBox.getChildren().get(i);
			Text text = (Text) hBox.getChildren().get(1);
			text.setText((i + 1) + ".");
		}
	}

	public void onUtworzClick(ActionEvent event) throws IOException {
		int checkReturn = checkFieldsFill();

		switch(checkReturn) {
			case 0:
				switchScene(event, "myTests.fxml");
				break;
			case 1:
				attentionText.setText("Wypełnij wszystkie treści pytań");
				break;
			case 2:
				attentionText.setText("Nie wszystkie pytania mają odpowiedź");
				break;
			case 3:
				attentionText.setText("Wypełnij wszystkie odpowiedzi");
				break;
			case 4:
				attentionText.setText("Nie wszystkie pytania mają poprawną odpowiedź");
				break;
		}
	}

	public void onAnulujButtonClick(ActionEvent event) throws IOException {
		switchScene(event, "myTests.fxml");
	}

	public void onHelpClick(ActionEvent event) {

	}

	public int checkFieldsFill() {
		for(int i=0; i<questionsVBox.getChildren().size(); i++) {
			VBox questionVBox = (VBox) questionsVBox.getChildren().get(i);
			VBox answersVBox = (VBox) questionVBox.getChildren().get(2);
			TextField questionContent = (TextField) questionVBox.getChildren().get(1);

			if(questionContent.getText().isBlank())
				return 1;

			if(answersVBox.getChildren().size() == 0)
				return 2;

			for(int j=0; j<answersVBox.getChildren().size(); j++) {
				HBox answerHBox = (HBox) answersVBox.getChildren().get(j);
				TextField answerContent = (TextField) answerHBox.getChildren().get(2);
				if(answerContent.getText().isBlank())
					return 3;
			}
		}
		return 0;
	}
}
