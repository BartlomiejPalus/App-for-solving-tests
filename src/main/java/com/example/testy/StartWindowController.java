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

public class StartWindowController {

    @FXML
    TextField loginField;
    @FXML
    PasswordField passwordField;
    @FXML
    Text attentionText;

    public void onZalogujButtonClick(ActionEvent event) throws IOException, SQLException, NoSuchAlgorithmException {
        if(loginField.getText().isBlank() || passwordField.getText().isBlank()) {
            attentionText.setText("Wypełnij wszystkie pola");
        }
        else {
            int id = DBController.login(loginField.getText(), passwordField.getText());
            if(id != -1) {
                Account.getInstance().logIn(loginField.getText(), id);
                switchScene(event, "mainMenu.fxml");
            }
            else {
                attentionText.setText("Niepoprawne dane logowania");
            }
        }
    }

    public void onRejestracjaButtonClick(ActionEvent event) throws IOException {
        switchScene(event, "register.fxml");
    }

    public void onHelpClick(ActionEvent event) {

    }
}
