package com.example.testy;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static com.example.testy.SceneSwitcher.switchScene;

public class ListOfTestsController implements Initializable {

	@FXML
	VBox listaTestowVBox;
	@FXML
	ComboBox<String> kategorieComboBox, iloscPytanComboBox, iloscPodejscComboBox, widocznoscComboBox;
	@FXML
	ScrollPane listaScrollPane;
	@FXML
	TextField nazwaTestuTextField;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		kategorieComboBox.getItems().add("Wszystkie");
		for(TestCategories t : TestCategories.values()) {
			kategorieComboBox.getItems().add(t.getText());
		}

		iloscPytanComboBox.getItems().addAll("Wszystkie", "1-10", "11-20", "21-30", "30+");
		iloscPytanComboBox.getSelectionModel().selectFirst();

		iloscPodejscComboBox.getItems().addAll("Wszystkie", "Ograniczona", "Nieograniczona");
		iloscPodejscComboBox.getSelectionModel().selectFirst();

		widocznoscComboBox.getItems().addAll("Wszystkie", "Testy innych", "Moje testy");
		widocznoscComboBox.getSelectionModel().selectFirst();

		try {
			drawListOfTests(DBController.getListOfTests(null));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void drawListOfTests(ResultSet resultSet) throws SQLException {
		while (resultSet.next()) {
			VBox vBox = new VBox();
			vBox.getStyleClass().add("TloWiersza");
			vBox.setSpacing(10);
			vBox.setPadding(new Insets(15));

			Text title = new Text(resultSet.getString("name"));
			title.getStyleClass().add("TestNazwaLista");
			vBox.getChildren().add(title);

			HBox hBox = new HBox();
			Text text1 = new Text("Kategoria: " + resultSet.getString("category") + ", Liczba pytań: " +
					resultSet.getInt("amountOfQuestionsInApproach"));
			if(resultSet.getInt("amountOfQuestionsInApproach") == -1) {
				text1.setText("Kategoria: " + resultSet.getString("category") + ", Liczba pytań: " +
						resultSet.getInt("totalQuestionsAmount"));
			}
			Button fillButton = new Button("Wypełnij");
			fillButton.setUserData(resultSet.getInt("id"));

			fillButton.setOnAction(e -> {
				try {
					FXMLLoader loader = switchScene(e, "testDetails.fxml");
					TestDetailsController controller = loader.getController();
					controller.printDetails((int) fillButton.getUserData(), "listOfTests.fxml");
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
			});

			Region region = new Region();
			region.setPrefWidth(1000);

			hBox.getChildren().addAll(text1, region, fillButton);

			vBox.getChildren().add(hBox);

			listaTestowVBox.getChildren().add(vBox);
		}
	}

	public void onFiltrujButtonClick() throws SQLException {
		String condition = "name LIKE '%" + nazwaTestuTextField.getText() + "%'";

		if (kategorieComboBox.getValue() != null && !kategorieComboBox.getValue().equals("Wszystkie")) {
			condition += " AND category = '" + kategorieComboBox.getValue() + "'";
		}

		if (iloscPytanComboBox.getValue() != null && !iloscPytanComboBox.getValue().equals("Wszystkie")) {
			switch(iloscPytanComboBox.getValue()){
				case "1-10" -> condition += " AND (amountOfQuestionsInApproach BETWEEN 1 AND 10 " +
						"OR (amountOfQuestionsInApproach = -1 AND " +
						"(SELECT COUNT(*) FROM question WHERE testID = test.id) BETWEEN 1 AND 10))";
				case "11-20" -> condition += " AND (amountOfQuestionsInApproach BETWEEN 11 AND 20 " +
						"OR (amountOfQuestionsInApproach = -1 AND " +
						"(SELECT COUNT(*) FROM question WHERE testID = test.id) BETWEEN 11 AND 20))";
				case "21-30" -> condition += " AND (amountOfQuestionsInApproach BETWEEN 21 AND 30 " +
						"OR (amountOfQuestionsInApproach = -1 AND " +
						"(SELECT COUNT(*) FROM question WHERE testID = test.id) BETWEEN 21 AND 30))";
				case "30+" -> condition += " AND (amountOfQuestionsInApproach > 30 " +
						"OR (amountOfQuestionsInApproach = -1 AND " +
						"(SELECT COUNT(*) FROM question WHERE testID = test.id) > 30))";
			}
		}

		if (iloscPodejscComboBox.getValue().equals("Ograniczona")) {
			condition += " AND amountOfApproach != -1";
		} else if (iloscPodejscComboBox.getValue().equals("Nieograniczona")) {
			condition += " AND amountOfApproach = -1";
		}

		if (widocznoscComboBox.getValue().equals("Testy innych")) {
			condition += " AND creatorID != " + Account.getInstance().getId();
		} else if (widocznoscComboBox.getValue().equals("Moje testy")) {
			condition += " AND creatorID = " + Account.getInstance().getId();
		}

		listaTestowVBox.getChildren().clear();
		drawListOfTests(DBController.getListOfTests(condition));
	}

	public void onResetujButtonClick() throws SQLException {
		nazwaTestuTextField.setText("");
		kategorieComboBox.getSelectionModel().selectFirst();
		iloscPytanComboBox.getSelectionModel().selectFirst();
		iloscPodejscComboBox.getSelectionModel().selectFirst();
		widocznoscComboBox.getSelectionModel().selectFirst();

		listaTestowVBox.getChildren().clear();
		drawListOfTests(DBController.getListOfTests(null));
	}

	public void onWrocButtonClick(ActionEvent event) throws IOException {
		switchScene(event, "mainMenu.fxml");
	}

	public void onHelpClick(ActionEvent event) {

	}
}
