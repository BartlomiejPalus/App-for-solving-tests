package com.example.testy;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

import static com.example.testy.SceneSwitcher.switchScene;

public class TestsHistoryController implements Initializable {

	@FXML
	TextField nazwaTestuTextField;
	@FXML
	ComboBox<String> kategorieComboBox;
	@FXML
	VBox listaHistoriaTestowVBox;

	int testID = -1;
	String source;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		nazwaTestuTextField.setFocusTraversable(false);
		kategorieComboBox.getItems().add("Wszystkie");
		for(TestCategories t : TestCategories.values()) {
			kategorieComboBox.getItems().add(t.getText());
		}
		kategorieComboBox.getSelectionModel().selectFirst();
	}

	public void setParameters(int testID, String source) throws SQLException {
		this.testID = testID;
		this.source = source;
		String condition = "";
		if(testID != -1) {
			condition = " AND t.id = " + testID;
		}
		drawListOfMySolutions(DBController.getUserSolutions(condition));
	}

	public void drawListOfMySolutions(ResultSet resultSet) throws SQLException {
		listaHistoriaTestowVBox.getChildren().clear();
		while(resultSet.next()) {
			VBox vBox = new VBox();
			vBox.getStyleClass().add("TloWiersza");
			vBox.setSpacing(10);
			vBox.setPadding(new Insets(15));

			Text title = new Text(resultSet.getString("name"));
			title.getStyleClass().add("TestNazwaLista");
			vBox.getChildren().add(title);

			HBox hBox = new HBox();
			hBox.setSpacing(10);

			double percent = Math.round(resultSet.getDouble("points")/resultSet.getDouble("maxPoints") * 100);
			DecimalFormat format = new DecimalFormat("#.#");
			String formattedPercent = format.format(percent);

			Text text1 = new Text("Kategoria: " + resultSet.getString("category") + ", wynik: " +
					resultSet.getString("points") + "/" + resultSet.getString("maxPoints") +
					" (" + formattedPercent + "%)");

			Button showAnswersButton = new Button("Pokaż odpowiedzi");
			showAnswersButton.setUserData(resultSet.getInt("solutionID"));
			Button fillAgainButton = new Button("Rozwiąż ponownie");
			fillAgainButton.setUserData(resultSet.getInt("testID"));

			showAnswersButton.setOnAction(e -> {
				try {
					FXMLLoader loader = switchScene(e, "testReview.fxml");
					TestReviewController controller = loader.getController();
					controller.printSolution((int) showAnswersButton.getUserData(), "testsHistory.fxml"
							,source, testID, title.getText());
				} catch (IOException | SQLException ex) {
					throw new RuntimeException(ex);
				}
			});

			fillAgainButton.setOnAction(e -> {
				try {
					FXMLLoader loader = switchScene(e, "testDetails.fxml");
					TestDetailsController controller = loader.getController();
					controller.printDetails((int) fillAgainButton.getUserData(), "testsHistory.fxml");
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
			});

			Region region = new Region();
			region.setPrefWidth(1000);

			hBox.getChildren().addAll(text1, region, showAnswersButton, fillAgainButton);

			vBox.getChildren().add(hBox);

			listaHistoriaTestowVBox.getChildren().add(vBox);
		}
	}

	public void onFiltrujButtonClick() throws SQLException {
		String condition = "";
		if(nazwaTestuTextField.getText() != null) {
			condition += " AND t.name LIKE '%" + nazwaTestuTextField.getText() + "%'";
		}
		if(!kategorieComboBox.getValue().equals("Wszystkie")) {
			condition += " AND t.category = '" + kategorieComboBox.getValue() + "'";
		}
		ResultSet resultSet = DBController.getUserSolutions(condition);
		drawListOfMySolutions(resultSet);
	}

	public void onResetujButtonClick() throws SQLException {
		nazwaTestuTextField.setText("");
		kategorieComboBox.getSelectionModel().selectFirst();

		listaHistoriaTestowVBox.getChildren().clear();
		drawListOfMySolutions(DBController.getUserSolutions(null));
	}

	public void onWrocButtonClick(ActionEvent event) throws IOException {
		if(source.equals("testDetails.fxml")) {
			FXMLLoader fxmlLoader = switchScene(event, source);
			TestDetailsController controller = fxmlLoader.getController();
			controller.printDetails(testID, "listOfTests.fxml");
		}
		else {
			switchScene(event, source);
		}
	}

	public void onHelpClick() {
		InstructionOpener.openPage("historiaTestowEkran.html");
	}
}