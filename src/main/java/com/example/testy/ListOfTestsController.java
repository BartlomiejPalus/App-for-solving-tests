package com.example.testy;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
		kategorieComboBox.getItems().addAll("Wszystkie", "Matematyka", "Fizyka");
		kategorieComboBox.getSelectionModel().selectFirst();

		iloscPytanComboBox.getItems().addAll("Wszystkie", "1-10", "10-20", "20-30", "30+");
		iloscPytanComboBox.getSelectionModel().selectFirst();

		iloscPodejscComboBox.getItems().addAll("Wszystkie", "Jedno", "Nieograniczone");
		iloscPodejscComboBox.getSelectionModel().selectFirst();

		widocznoscComboBox.getItems().addAll("Wszystkie", "Moje testy");
		widocznoscComboBox.getSelectionModel().selectFirst();

		for(int i=0; i<7; i++) {
			VBox vBox = new VBox();
			vBox.getStyleClass().add("TloWiersza");
			vBox.setSpacing(10);
			vBox.setPadding(new Insets(15));

			Text title = new Text("Przykładowy test " + (i + 1));
			title.getStyleClass().add("TestNazwaLista");
			vBox.getChildren().add(title);

			HBox hBox = new HBox();
			Text text1 = new Text("Kategoria: " + "Matematyka" + ", Liczba pytań: " + 10);
			Button fillButton = new Button("Wypełnij");
			fillButton.setUserData(i);

			fillButton.setOnAction(e -> {
				try {
					switchScene(e, "testDetails.fxml");
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}

				//SzczegolyTestuController controller = fxmlLoader.getController();
				//controller.setTestID((int) fillButton.getUserData());
				//stage.show();
			});

			Region region = new Region();
			region.setPrefWidth(1000);

			hBox.getChildren().addAll(text1, region, fillButton);

			vBox.getChildren().add(hBox);

			listaTestowVBox.getChildren().add(vBox);
		}
	}

	public void onFiltrujButtonClick(ActionEvent event){

	}

	public void onResetujButtonClick(ActionEvent event){
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
