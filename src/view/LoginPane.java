package view;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import model.Chat;

import java.util.List;

public class LoginPane extends BorderPane {
    private NewChatPane ncp;

    private TextField txtNickname;
    private Button btnJoinChat, btnRefreshChats;

    private ListView<String> lvwChats;
    private ObservableList<String> olChats;

    public LoginPane() {
        olChats = FXCollections.observableArrayList();

        lvwChats = new ListView<>(olChats);
        lvwChats.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        ncp = new NewChatPane();

        GridPane gp = new GridPane();
        gp.setPadding(new Insets(80, 80, 80, 80));
        gp.setVgap(10);
        //gp.setHgap(20);
        gp.setAlignment(Pos.CENTER);


        Label lblNickname = new Label("Nickname:");
        Label lblChats = new Label("Available Chats:");

        txtNickname = new TextField();

        HBox ButtonPane = new HBox();
        btnJoinChat = new Button("Join Chat");
        btnRefreshChats = new Button("Refresh Chats");

        ButtonPane.setAlignment(Pos.CENTER);
        ButtonPane.setSpacing(5);

        ButtonPane.getChildren().addAll(btnJoinChat,btnRefreshChats);

        gp.add(lblNickname,0,0);
        gp.add(txtNickname,0,1);
        gp.add(lblChats,0,2);
        gp.add(lvwChats,0,3);
        gp.add(ButtonPane,0,4);

        this.setCenter(gp);
        this.setRight(ncp);
        //this.setBottom(ButtonPane);

    }

    public void addJoinChatBind(BooleanBinding property) {
        btnJoinChat.disableProperty().bind(property);
    }

    public BooleanBinding isAnyFieldEmpty() {
        return txtNickname.textProperty().isEmpty().or(lvwChats.getSelectionModel().selectedItemProperty().isNull());
    }

    public String getNickname() {
        return txtNickname.getText();
    }

    public void addJoinHandler(EventHandler<ActionEvent> handler) {
        btnJoinChat.setOnAction(handler);
    }

    public void addRefreshHandler(EventHandler<ActionEvent> handler) {
        btnRefreshChats.setOnAction(handler);
    }


    public void addChat(String chatName) {
        //Chat to String TODO
        olChats.add(chatName);
    }


    public String getSelectedChat() {
        return lvwChats.getSelectionModel().getSelectedItem();
    }

    public void clearChatList() {
        olChats.clear();
    }

    public NewChatPane getNcp() {
        return ncp;
    }

}
