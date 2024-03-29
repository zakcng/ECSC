package view;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class PasswordDialog extends Dialog<String> {
    private PasswordField passwordField;

    public PasswordDialog() {
        setTitle("Password");
        setHeaderText("Please enter chat password.");

        ButtonType passwordButtonType = new ButtonType("Ok", ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(passwordButtonType, ButtonType.CANCEL);

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Label lblText = new Label("Please enter the chat password: ");

        HBox hBox = new HBox();
        hBox.getChildren().addAll(lblText, passwordField);
        hBox.setPadding(new Insets(20));

        HBox.setHgrow(passwordField, Priority.ALWAYS);

        getDialogPane().setContent(hBox);

        Platform.runLater(() -> passwordField.requestFocus());

        setResultConverter(dialogButton -> {
            if (dialogButton.getButtonData() == passwordButtonType.getButtonData()) {
                return passwordField.getText();
            }
            return null;
        });
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }
}
