package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;


public class NewChatPane extends BorderPane{
    private TextField txtName;
    private PasswordField txtChatPassword;
    private CheckBox cbChatPassChecked, cbChatLogChecked;
    private Button btnCreate, btnTest;
    private GridPane gp;

    public NewChatPane() {
        gp = new GridPane();
        gp.setPadding(new Insets(80, 80, 80, 80));
        gp.setVgap(15);
        gp.setHgap(20);
        gp.setAlignment(Pos.CENTER);

        txtName = new TextField("");
        txtName.setPromptText("Room Name");
        txtChatPassword = new PasswordField();
        txtChatPassword.setPromptText("Chat Password");

        cbChatPassChecked = new CheckBox("Enabled");
        cbChatLogChecked = new CheckBox("Chat Logs");

        btnCreate = new Button("New Chat");
        btnTest = new Button("Test");

        gp.add(txtName,0,0);

        gp.add(txtChatPassword,0,1);
        gp.add(cbChatPassChecked,1,1);

        gp.add(cbChatLogChecked,0,2);

        gp.add(btnCreate,0,3);

        //gp.add(btnTest,0,4);

        this.setCenter(gp);

    }

    public String getTxtName() {
        return txtName.getText();
    }

    public String getTxtChatPassword() {
        return txtChatPassword.getText();
    }

    public Boolean getCbChatPassChecked() {
        return cbChatPassChecked.isSelected();
    }

    public CheckBox getCbChatLogChecked() {
        return cbChatLogChecked;
    }

    public void addCreateHandler(EventHandler<ActionEvent> handler) {
        btnCreate.setOnAction(handler);
    }

    public void addTestHandler(EventHandler<ActionEvent> handler) {
        btnTest.setOnAction(handler);
    }


}
