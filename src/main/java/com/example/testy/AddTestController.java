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
import java.sql.ResultSet;
import java.sql.SQLException;
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
	Button createButton, deleteTestButton;
	@FXML
	Text windowNameText ,attentionText;

	private int testID;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		for(TestCategories t : TestCategories.values()) {
			categoryComboBox.getItems().add(t.getText());
		}
		visabilityComboBox.getItems().addAll("Publiczny", "Prywatny");
		visabilityComboBox.getSelectionModel().selectFirst();
	}

	public void setState(String state, int testID) throws SQLException {
		this.testID = testID;
		if(state.equals("addTest")) {
			addQuestion(null);
		}
		else {
			windowNameText.setText("Edytuj test");
			createButton.setText("Edytuj");
			deleteTestButton.setVisible(true);

			ResultSet resultSet = DBController.getTestProperties(testID);
			resultSet.next();
			setTestProperties(resultSet);

			resultSet = DBController.getQuestionsForEdit(testID);
			while (resultSet.next()) {
				addQuestion(resultSet);
			}
		}
	}

	public void setTestProperties(ResultSet resultSet) throws SQLException {
		testNameTextField.setText(resultSet.getString("name"));
		categoryComboBox.setValue(resultSet.getString("category"));

		if(resultSet.getInt("amountOfQuestionsInApproach") != -1) {
			allCheckBox.setSelected(false);
			amountOfQuestionsInApproachTextField.setDisable(false);
			amountOfQuestionsInApproachTextField.setText(resultSet.getString("amountOfQuestionsInApproach"));
		}

		if(resultSet.getInt("amountOfApproach") != -1) {
			unlimitedCheckBox.setSelected(false);
			amountOfApproachTextField.setDisable(false);
			amountOfApproachTextField.setText(resultSet.getString("amountOfApproach"));
		}

		if(!resultSet.getBoolean("isOverviewable")) {
			isNotOverviewableRadio.setSelected(true);
		}

		passwordTextField.setText(resultSet.getString("password"));

		if(!resultSet.getBoolean("isPublic")) {
			visabilityComboBox.getSelectionModel().selectLast();
		}
	}

	public void onDodajPytanieButtonClick() throws SQLException {
		addQuestion(null);
	}

	public void addQuestion(ResultSet resultSet) throws SQLException {
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

		if(resultSet != null) {
			questionContent.setText(resultSet.getString("content"));
			goodAnswer.setText(resultSet.getString("pointsForGood"));
			badAnswer.setText(String.valueOf(resultSet.getInt("pointsForBad")));

			resultSet = DBController.getAnswersOfQuestion(resultSet.getInt("id"));
			while(resultSet.next()) {
				addAnswer(answersVBox, resultSet);
			}
		}
		else {
			addAnswer(answersVBox, null);
		}

		Button addAnswerButton = new Button("Dodaj odpowiedź");
		addAnswerButton.setOnAction(e -> {
			try {
				addAnswer(answersVBox, null);
			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}
		});

		deleteQuestionButton.setOnAction(e -> {
			questionsVBox.getChildren().remove(questionVBox);
			updateQuestionNumbering();
		});

		questionVBox.getChildren().addAll(answersVBox, addAnswerButton);
		questionsVBox.getChildren().add(questionVBox);

		updateQuestionNumbering();
	}

	public void addAnswer(VBox answersVBox, ResultSet resultSet) throws SQLException {
		HBox answersHBox = new HBox();
		answersHBox.setAlignment(Pos.CENTER_LEFT);
		answersHBox.setSpacing(10);

		CheckBox checkBox = new CheckBox();

		TextField contentTextField = new TextField();
		contentTextField.setPromptText("Wprowadź odpowiedź");

		Button deleteAnswerButton = new Button("-");
		deleteAnswerButton.getStyleClass().add("Usun");

		if(resultSet != null) {
			contentTextField.setText(resultSet.getString("content"));
			if(resultSet.getBoolean("isTrue")) {
				checkBox.setSelected(true);
			}
		}

		deleteAnswerButton.setOnAction(e -> {
			answersVBox.getChildren().remove(answersHBox);
			updateAnswerNumbering(answersVBox);
		});
		answersHBox.getChildren().addAll(checkBox, new Text((answersVBox.getChildren().size()+1) + "."), contentTextField, deleteAnswerButton);
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

	public void onUtworzClick(ActionEvent event) throws IOException, SQLException {
		if(addTest()) {
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

	public void onDeleteTestButtonClick(ActionEvent event) throws IOException, SQLException {
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
				DBController.deleteTest(testID);
				switchScene(event, "myTests.fxml");
			}
		}
	}

	public void onHelpClick() {

	}

	public boolean addTest() throws SQLException {
		if(checkFieldsFill() && checkParametersFill()) {
			String testName = testNameTextField.getText();
			String category = categoryComboBox.getValue();
			String amountOfQuestionsInApproach = "-1";
			if(!allCheckBox.isSelected()) {
				amountOfQuestionsInApproach = amountOfQuestionsInApproachTextField.getText();
			}
			String amountOfApproach = "-1";
			if(!unlimitedCheckBox.isSelected()) {
				amountOfApproach = amountOfApproachTextField.getText();
			}
			boolean isOverviewable = ((RadioButton) isOverviewableGroup.getSelectedToggle()).getText().equals("Tak");
			String password = passwordTextField.getText();
			boolean isPublic = visabilityComboBox.getValue().equals("Publiczny");
			Test test = new Test(testName, category, Integer.parseInt(amountOfQuestionsInApproach),
					Integer.parseInt(amountOfApproach), isOverviewable, password, isPublic);

			for(Node questionNode : questionsVBox.getChildren()) {
				VBox questionVBox = (VBox) questionNode;
				String questionContent = ((TextField)questionVBox.getChildren().get(1)).getText();
				HBox header = (HBox) questionVBox.getChildren().get(0);
				HBox points = (HBox) header.getChildren().get(4);
				TextField goodAnswerPointsTextField = (TextField) points.getChildren().get(0);
				TextField badAnswerPointsTextField = (TextField) points.getChildren().get(2);
				String goodAnswerPoints = defaultGoodTextField.getText();
				String badAnswerPoints = defaultBadTextField.getText();

				if(!goodAnswerPointsTextField.getText().isBlank())
					goodAnswerPoints = goodAnswerPointsTextField.getText();
				if(!badAnswerPointsTextField.getText().isBlank())
					badAnswerPoints = badAnswerPointsTextField.getText();
				System.out.println(Integer.parseInt(goodAnswerPoints));
				System.out.println(Integer.parseInt(badAnswerPoints));
				Question question = new Question(questionContent, Integer.parseInt(goodAnswerPoints),
						Integer.parseInt(badAnswerPoints));
				VBox answersVBox = (VBox) questionVBox.getChildren().get(2);

				for(Node answerNode : answersVBox.getChildren()) {
					HBox hBox = (HBox) answerNode;
					String answerContent = ((TextField) hBox.getChildren().get(2)).getText();
					Boolean isCorrect = ((CheckBox) hBox.getChildren().get(0)).isSelected();
					question.addAnswer(new Answer(answerContent, isCorrect));
				}
				test.addQuestion(question);
			}
			System.out.println(test.getName());
			for (int i=0; i<test.getQuestionsList().size();i++) {
				System.out.println(test.getQuestionsList().get(i).getContent());
				for (int j=0;j<test.getQuestionsList().get(i).getAnswers().size();j++) {
					System.out.println(test.getQuestionsList().get(i).getAnswers().get(j).getContent() + " "
							+ test.getQuestionsList().get(i).getAnswers().get(j).getCorrect());
				}
			}
			DBController.addTest(test, testID);
			return true;
		}
		return false;
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
		if(categoryComboBox.getValue() == null) {
			attentionText.setText("Wybierz kategorię testu");
			return false;
		}
		if(!allCheckBox.isSelected() && amountOfQuestionsInApproachTextField.getText().isBlank()) {
			attentionText.setText("Wprowadź ilość pytań w podejściu");
			return false;
		}
		if(!allCheckBox.isSelected() && !amountOfQuestionsInApproachTextField.getText().isBlank()
				&& Integer.parseInt(amountOfQuestionsInApproachTextField.getText()) > questionsVBox.getChildren().size()) {
			attentionText.setText("Ilość pytań w podejściu nie może być większa niż ilość pytań w teście");
			return false;
		}
		if(!unlimitedCheckBox.isSelected() && amountOfApproachTextField.getText().isBlank()) {
			attentionText.setText("Wprowadź ilość podejść do testu");
			return false;
		}
		return true;
	}
}
