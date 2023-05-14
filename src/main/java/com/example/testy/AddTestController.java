package com.example.testy;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.example.testy.SceneSwitcher.switchScene;

public class AddTestController implements Initializable {

	@FXML
	VBox questionsVBox;
	@FXML
	TextField defaultGoodTextField, defaultBadTextField, testNameTextField, amountOfQuestionsInApproachTextField,
			amountOfApproachTextField, passwordTextField;
	@FXML
	ComboBox<String> categoryComboBox, visabilityComboBox;
	@FXML
	CheckBox allCheckBox, unlimitedCheckBox;
	@FXML
	RadioButton isOverviewableRadio, isNotOverviewableRadio;
	@FXML
	ToggleGroup isOverviewableGroup;
	@FXML
	Button deleteTestButton;
	@FXML
	Text windowNameText ,attentionText;

	private String state = "addTest";
	private List<Question> questionsList;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		addQuestion();
	}

	public void setState(String state) {
		this.state = state;
		if(state.equals("addTest")) {
			windowNameText.setText("Dodaj test");
			deleteTestButton.setVisible(false);
		}
		else if(state.equals("editTest")) {
			windowNameText.setText("Edytuj test");
			deleteTestButton.setVisible(true);
		}
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
		addAnswerButton.setOnAction(e -> addAnswer(answersVBox));

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
		if(checkFieldsFill() && checkParametersFill()) {

			System.out.println("d");
			switchScene(event, "myTests.fxml");
		}
	}

	public void onAnulujButtonClick(ActionEvent event) throws IOException {
		switchScene(event, "myTests.fxml");
	}

	public void onAllCheckBoxClick() {
		amountOfQuestionsInApproachTextField.setDisable(allCheckBox.isSelected());
	}

	public void onUnlimitedCheckBoxClick() {
		amountOfApproachTextField.setDisable(unlimitedCheckBox.isSelected());
	}

	public void onDeleteTestButtonClick(ActionEvent event) throws IOException {
		ButtonType deleteButton = new ButtonType("Usuń", ButtonBar.ButtonData.OK_DONE);
		ButtonType cancelButton = new ButtonType("Anuluj", ButtonBar.ButtonData.CANCEL_CLOSE);
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Operacja jest nieodwracalna", deleteButton, cancelButton);

		alert.setTitle("Usuń test");
		alert.setHeaderText("Czy na pewno chcesz usunąć test?");

		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());

		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("icons/appIcon.png")).openStream()));

		Node discardButton = alert.getDialogPane().lookupButton(deleteButton);
		discardButton.getStyleClass().add("AlertRedButton");

		Optional<ButtonType> option = alert.showAndWait();

		if(option.isPresent()) {
			if (option.get() == deleteButton) {
				switchScene(event, "myTests.fxml");
			}
		}
	}

	public void onHelpClick() {

	}

	public boolean checkFieldsFill() {
		boolean haveCorrectAnswer = false;

		if(questionsVBox.getChildren().size() == 0) {
			attentionText.setText("Test nie posiada pytań");
			return false;
		}

		for(int i=0; i<questionsVBox.getChildren().size(); i++) {
			VBox questionVBox = (VBox) questionsVBox.getChildren().get(i);
			VBox answersVBox = (VBox) questionVBox.getChildren().get(2);
			TextField questionContent = (TextField) questionVBox.getChildren().get(1);

			if(questionContent.getText().isBlank()) {
				attentionText.setText("Pytanie nr "+ (i+1) +" nie posiada treści");
				return false;
			}

			if(answersVBox.getChildren().size() == 0) {
				attentionText.setText("Pytanie nr "+ (i+1) +" nie posiada żadnej odpowiedzi");
				return false;
			}

			for(int j=0; j<answersVBox.getChildren().size(); j++) {
				HBox answerHBox = (HBox) answersVBox.getChildren().get(j);
				CheckBox checkBox = (CheckBox) answerHBox.getChildren().get(0);
				TextField answerContent = (TextField) answerHBox.getChildren().get(2);
				if(checkBox.isSelected())
					haveCorrectAnswer = true;
				if(answerContent.getText().isBlank()) {
					attentionText.setText("Pytanie nr "+ (i+1) +" posiada odpowiedzi bez treści");
					return false;
				}
			}

			if(!haveCorrectAnswer) {
				attentionText.setText("Pytanie nr "+ (i+1) +" nie posiada poprawnej odpowiedzi");
				return false;
			}
			haveCorrectAnswer = false;
		}
		return true;
	}

	public boolean checkParametersFill() {
		if(defaultGoodTextField.getText().isBlank() || defaultBadTextField.getText().isBlank()) {
			attentionText.setText("Wypełnij domyślną punktację");
			return false;
		}
		if(testNameTextField.getText().isBlank()) {
			attentionText.setText("Wprowadź nazwę testu");
			return false;
		}
		if(!allCheckBox.isSelected() && amountOfQuestionsInApproachTextField.getText().isBlank()) {
			attentionText.setText("Wprowadź ilość pytań w podejściu");
			return false;
		}
		if(!unlimitedCheckBox.isSelected() && amountOfApproachTextField.getText().isBlank()) {
			attentionText.setText("Wprowadź ilość podejść do testu");
			return false;
		}
		//RadioButton t = (RadioButton)isOverviewableGroup.getSelectedToggle();
		//System.out.println(t.getText());
		return true;
	}
}
