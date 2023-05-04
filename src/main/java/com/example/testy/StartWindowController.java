package com.example.testy;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;

import static com.example.testy.SceneSwitcher.switchScene;

public class StartWindowController {

    @FXML
    TextField loginField;
    @FXML
    PasswordField hasloField;
    @FXML
    Text uwagaText;

    public void onZalogujButtonClick(ActionEvent event) throws IOException {
        if(loginField.getText().isBlank() || hasloField.getText().isBlank()) {
            uwagaText.setText("Wypełnij wszystkie pola");
        }
        else {
            switchScene(event, "mainMenu.fxml");
        }
    }

    public void onRejestracjaButtonClick(ActionEvent event) throws IOException {
        switchScene(event, "register.fxml");
    }

    public void onHelpClick(ActionEvent event) {

    }
}
