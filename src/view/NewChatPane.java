package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;


public class NewChatPane extends BorderPane{
    TextField txtName, txtChatPassword;
    CheckBox cbChatPassChecked;
    Button btnCreate;

    public NewChatPane() {
        GridPane gp = new GridPane();
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

}
