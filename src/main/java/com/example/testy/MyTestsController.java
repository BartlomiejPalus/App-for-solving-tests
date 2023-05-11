package com.example.testy;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
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
		kategorieComboBox.getItems().addAll("Wszystkie", "Matematyka", "Fizyka");
		kategorieComboBox.getSelectionModel().selectFirst();

		for(int i=0; i<7; i++) {
			VBox vBox = new VBox();
			vBox.getStyleClass().add("TloWiersza");
			vBox.setSpacing(10);
			vBox.setPadding(new Insets(15));

			Text title = new Text("Przykładowy test " + (i + 1));
			title.getStyleClass().add("TestNazwaLista");
			vBox.getChildren().add(title);

			HBox hBox = new HBox();
			hBox.setSpacing(10);
			Text text1 = new Text("Kategoria: " + "Matematyka" + ", Liczba pytań: " + 10);
			Button resultsButton = new Button("Wyniki");
			resultsButton.getStyleClass().add("ListButton");
			Button editButton = new Button("Edytuj");
			editButton.getStyleClass().add("ListButton");
			editButton.setUserData(i);

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
					controller.setState("editTest");
				} catch (IOException ex) {
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

	public void onFiltrujButtonClick(ActionEvent event){

	}

	public void onResetujButtonClick(){
		nazwaTestuTextField.setText("");
		kategorieComboBox.getSelectionModel().selectFirst();
		iloscPytanComboBox.getSelectionModel().selectFirst();
		iloscPodejscComboBox.getSelectionModel().selectFirst();
		widocznoscComboBox.getSelectionModel().selectFirst();
	}

	public void onDodajTestButtonClick(ActionEvent event) throws IOException {
		FXMLLoader loader = switchScene(event, "addTest.fxml");
		AddTestController controller = loader.getController();
		controller.setState("addTest");
	}

	public void onWrocButtonClick(ActionEvent event) throws IOException {
		switchScene(event, "mainMenu.fxml");
	}

	public void onHelpClick(ActionEvent event) {

	}
}
