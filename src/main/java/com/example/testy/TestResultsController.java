package com.example.testy;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

import static com.example.testy.SceneSwitcher.switchScene;

public class TestResultsController {

	@FXML
	VBox listaHistoriaTestowVBox;
	@FXML
	TextField userNameTextField;

	int testID;

	public void setParameters(int testID) throws SQLException {
		this.testID = testID;
		drawListOfTestSolutions(DBController.getTestSolutions(testID, null));
	}

	public void drawListOfTestSolutions(ResultSet resultSet) throws SQLException {
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

			Text text1 = new Text("Użytkownik: " + resultSet.getString("login") + ", wynik: " +
					resultSet.getString("points") + "/" + resultSet.getString("maxPoints") +
					" (" + formattedPercent + "%)");

			Button showAnswersButton = new Button("Zobacz odpowiedzi");
			showAnswersButton.setUserData(resultSet.getInt("solutionID"));

			showAnswersButton.setOnAction(e -> {
				try {
					FXMLLoader fxmlLoader = switchScene(e, "testReview.fxml");
					TestReviewController controller = fxmlLoader.getController();
					controller.printSolution((int) showAnswersButton.getUserData(), "testResults.fxml",
							null, testID, title.getText());
				} catch (IOException | SQLException ex) {
					throw new RuntimeException(ex);
				}
			});

			Region region = new Region();
			region.setPrefWidth(1000);

			hBox.getChildren().addAll(text1, region, showAnswersButton);

			vBox.getChildren().add(hBox);

			listaHistoriaTestowVBox.getChildren().add(vBox);
		}
	}

	public void onSzukajButtonClick() throws SQLException {
		String condition = " AND ld.login LIKE '%" + userNameTextField.getText() + "%'";
		drawListOfTestSolutions(DBController.getTestSolutions(testID, condition));
	}

	public void onWrocButtonClick(ActionEvent event) throws IOException {
		switchScene(event, "myTests.fxml");
	}

	public void onHelpClick(ActionEvent event) {

	}
}
