package com.example.testy;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import static com.example.testy.SceneSwitcher.switchScene;

public class RegisterController {

    @FXML
    TextField loginField;
	@FXML
	PasswordField passwordField, repeatPasswordField;
    @FXML
    Text attentionText;

    public void onZarejestrujButtonClick(ActionEvent event) throws IOException, SQLException, NoSuchAlgorithmException {
        if(loginField.getText().isBlank() || passwordField.getText().isBlank() || repeatPasswordField.getText().isBlank()){
			attentionText.setText("Wypełnij wszystkie pola");
        }
		else if(!passwordField.getText().equals(repeatPasswordField.getText())) {
			attentionText.setText("Hasła nie są takie same");
		}
		else{
			DBController dbController = new DBController();
			if(dbController.register(loginField.getText(), passwordField.getText())) {
				switchScene(event, "mainMenu.fxml");
				return;
			}
			attentionText.setText("Login zajęty");
		}
    }

    public void onWrocButtonClick(ActionEvent event) throws IOException {
		switchScene(event, "startWindow.fxml");
    }

	public void onHelpClick(ActionEvent event) {

	}
}
