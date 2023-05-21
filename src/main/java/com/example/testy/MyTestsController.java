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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static com.example.testy.SceneSwitcher.switchScene;

public class MyTestsController implements Initializable {

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
		kategorieComboBox.getItems().addAll("Wszystkie");
		for(TestCategories t : TestCategories.values()) {
			kategorieComboBox.getItems().add(t.getText());
		}
		kategorieComboBox.getSelectionModel().selectFirst();
		try {
			drawListOfMyTests(DBController.getListOfTests("creatorID = " + Account.getInstance().getId()));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void drawListOfMyTests(ResultSet resultSet) throws SQLException {
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
			Text text1 = new Text("Kategoria: " + resultSet.getString("category") + ", Liczba pytań: " +
					resultSet.getInt("amountOfQuestionsInApproach"));
			if(resultSet.getInt("amountOfQuestionsInApproach") == -1) {
				text1.setText("Kategoria: " + resultSet.getString("category") + ", Liczba pytań: " +
						resultSet.getInt("totalQuestionsAmount"));
			}
			Button resultsButton = new Button("Wyniki");
			resultsButton.getStyleClass().add("ListButton");
			Button editButton = new Button("Edytuj");
			editButton.getStyleClass().add("ListButton");
			editButton.setUserData(resultSet.getInt("id"));

			resultsButton.setOnAction(e -> {
				try {
					switchScene(e, "testResults.fxml");
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
			});

			editButton.setOnAction(e -> {
				try {
					FXMLLoader loader = switchScene(e, "addTest.fxml");
					AddTestController controller = loader.getController();
					controller.setState("edit", (int) editButton.getUserData());
				} catch (IOException | SQLException ex) {
					throw new RuntimeException(ex);
				}
			});

			Region region = new Region();
			region.setPrefWidth(1000);

			hBox.getChildren().addAll(text1, region, resultsButton, editButton);

			vBox.getChildren().add(hBox);

			listaTestowVBox.getChildren().add(vBox);
		}
	}

	public void onFiltrujButtonClick() throws SQLException {
		String condition = "creatorID = " + Account.getInstance().getId();

		if (!nazwaTestuTextField.getText().isBlank()) {
			condition += " AND name LIKE \"%" + nazwaTestuTextField.getText() + "%\"";
		}

		if (kategorieComboBox.getValue() != null && !kategorieComboBox.getValue().equals("Wszystkie")) {
			condition += " AND category = \"" + kategorieComboBox.getValue() + "\"";
		}

		listaTestowVBox.getChildren().clear();
		drawListOfMyTests(DBController.getListOfTests(condition));
	}

	public void onResetujButtonClick() throws SQLException {
		nazwaTestuTextField.setText("");
		kategorieComboBox.getSelectionModel().selectFirst();
		iloscPytanComboBox.getSelectionModel().selectFirst();
		iloscPodejscComboBox.getSelectionModel().selectFirst();
		widocznoscComboBox.getSelectionModel().selectFirst();

		listaTestowVBox.getChildren().clear();
		drawListOfMyTests(DBController.getListOfTests(null));
	}

	public void onDodajTestButtonClick(ActionEvent event) throws IOException, SQLException {
		FXMLLoader fxmlLoader = switchScene(event, "addTest.fxml");
		AddTestController controller = fxmlLoader.getController();
		controller.setState("addTest", -1);
	}

	public void onWrocButtonClick(ActionEvent event) throws IOException {
		switchScene(event, "mainMenu.fxml");
	}

	public void onHelpClick(ActionEvent event) {

	}
}
