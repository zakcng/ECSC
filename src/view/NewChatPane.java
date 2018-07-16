package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;


public class NewChatPane extends BorderPane{
    private TextField txtName, txtChatPassword;
    private CheckBox cbChatPassChecked;
    private Button btnCreate;
    private GridPane gp;

    public NewChatPane() {
        gp = new GridPane();
        gp.setPadding(new Insets(80, 80, 80, 80));
        gp.setVgap(15);
        gp.setHgap(20);
        gp.setAlignment(Pos.CENTER);

        txtName = new TextField("Room Name");
        txtChatPassword = new TextField("Chat Password");

        cbChatPassChecked = new CheckBox("Enabled");

        btnCreate = new Button("New Chat");

        gp.add(txtName,0,0);

        gp.add(txtChatPassword,0,1);
        gp.add(cbChatPassChecked,1,1);

        gp.add(btnCreate,0,2);

        this.setCenter(gp);

    }

    public String getTxtName() {
        return txtName.getText();
    }

    public String getTxtChatPassword() {
        return txtChatPassword.getText();
    }

    public CheckBox getCbChatPassChecked() {
        return cbChatPassChecked;
    }

    public void addCreateHandler(EventHandler<ActionEvent> handler) {
        btnCreate.setOnAction(handler);
    }

}
