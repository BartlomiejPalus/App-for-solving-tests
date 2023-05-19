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
		for(TestCategories t : TestCategories.values()) {
			kategorieComboBox.getItems().add(t.getText());
		}

		iloscPytanComboBox.getItems().addAll("Wszystkie", "1-10", "10-20", "20-30", "30+");
		iloscPytanComboBox.getSelectionModel().selectFirst();

		iloscPodejscComboBox.getItems().addAll("Wszystkie", "Jedno", "Nieograniczone");
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
					controller.printDetails((int) fillButton.getUserData());
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

	public void onFiltrujButtonClick(){

	}

	public void onResetujButtonClick(){
		nazwaTestuTextField.setText("");
		kategorieComboBox.getSelectionModel().selectFirst();
		iloscPytanComboBox.getSelectionModel().selectFirst();
		iloscPodejscComboBox.getSelectionModel().selectFirst();
		widocznoscComboBox.getSelectionModel().selectFirst();
	}

	public void onWrocButtonClick(ActionEvent event) throws IOException {
		switchScene(event, "mainMenu.fxml");
	}

	public void onHelpClick(ActionEvent event) {

	}
}
