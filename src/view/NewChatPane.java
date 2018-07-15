package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;


public class NewChatPane extends GridPane{
    TextField txtName, txtChatPassword;
    CheckBox cbChatPassChecked;
    Button btnCreate;

    public NewChatPane() {
        this.setPadding(new Insets(80, 80, 80, 80));
        this.setVgap(15);
        this.setHgap(20);
        this.setAlignment(Pos.CENTER);

        txtName = new TextField("Nickname");
        txtChatPassword = new TextField("Chat Password");

        cbChatPassChecked = new CheckBox("Enabled");

        btnCreate = new Button("Create");

        this.add(txtName,0,0);

        this.add(txtChatPassword,0,1);
        this.add(cbChatPassChecked,1,1);

        this.add(btnCreate,0,2);

    }

}
