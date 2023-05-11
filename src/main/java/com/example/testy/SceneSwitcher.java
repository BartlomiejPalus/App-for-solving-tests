package com.example.testy;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneSwitcher {
	public static FXMLLoader switchScene(ActionEvent event, String fxmlFile) throws IOException {
		FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource(fxmlFile));
		Parent root = loader.load();
		double width = ((Node) event.getSource()).getScene().getWidth();
		double height = ((Node) event.getSource()).getScene().getHeight();
		Scene scene = new Scene(root, width, height);

		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();

		return loader;
	}

	public void onHelpClick(ActionEvent event) {

	}
}
