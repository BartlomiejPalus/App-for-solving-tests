package com.example.testy;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;

import static com.example.testy.SceneSwitcher.switchScene;

public class RegisterController {

    @FXML
    TextField loginField, hasloField, powtorzHasloField, emailField;
    @FXML
    Text uwagaText;

    public void onZarejestrujButtonClick(ActionEvent event) throws IOException {
        if(loginField.getText().isBlank() || hasloField.getText().isBlank() || powtorzHasloField.getText().isBlank()
                || emailField.getText().isBlank()){
			uwagaText.setText("Wypełnij wszystkie pola");
        }
		else{
			switchScene(event, "mainMenu.fxml");
		}
    }

    public void onWrocButtonClick(ActionEvent event) throws IOException {
		switchScene(event, "startWindow.fxml");
    }

	public void onHelpClick(ActionEvent event) {

	}
}
