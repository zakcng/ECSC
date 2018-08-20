package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import model.User;

import java.util.List;

public class ChatPane extends BorderPane {

    TextArea txtMessages;
    TextField txtOutgoing;
    Button btnSend;

    private ListView<String> lvwUsers;
    private ObservableList<String> olUsers;


    public ChatPane() {
        olUsers = FXCollections.observableArrayList();

        lvwUsers = new ListView<String>(olUsers);

        BorderPane inner = new BorderPane();
        inner.setPadding(new Insets(20, 20, 20, 20));

        HBox leftContainer = new HBox();
        leftContainer.setSpacing(5);
        VBox activePane = new VBox();
        activePane.setPadding(new Insets(5, 20, 20, 20));

        Separator separator1 = new Separator();

        Label lblActive = new Label("Active");

        VBox.setVgrow(lvwUsers, Priority.ALWAYS);

        activePane.getChildren().addAll(lblActive, separator1, lvwUsers);

        Separator separator2 = new Separator();
        separator2.setOrientation(Orientation.VERTICAL);
        leftContainer.getChildren().addAll(activePane, separator2);

        txtMessages = new TextArea();
        txtMessages.setDisable(true);

        HBox bottomContainer = new HBox();
        btnSend = new Button("Send");
        txtOutgoing = new TextField();
        HBox.setHgrow(txtOutgoing, Priority.ALWAYS);
        bottomContainer.setPadding(new Insets(5, 0, 0, 0));
        bottomContainer.setSpacing(5);
        bottomContainer.getChildren().addAll(txtOutgoing, btnSend);

        inner.setCenter(txtMessages);
        inner.setBottom(bottomContainer);

        this.setCenter(inner);
        this.setLeft(leftContainer);

    }

    public void appendLineToTxtMessages(String line) {
        txtMessages.appendText(line);
    }

    public void clearTxtMessages() {
        txtMessages.clear();
    }

    public String getTxtMessage() {
        return txtOutgoing.getText();
    }

    public void clearTextOutgoing(){
        txtOutgoing.clear();
    }

    public void addUserToList(User user) {
        olUsers.add(user.getNickname());
    }

    public void addSendHandler(EventHandler<ActionEvent> handler) {
        btnSend.setOnAction(handler);
    }

    public TextField getTxtOutgoing() {
        return txtOutgoing;
    }

    public Boolean isTxtOutgoingEmpty() {
        if (txtOutgoing.getText().isEmpty())
            return true;
        else
            return false;
    }

    public Button getBtnSend() {
        return btnSend;
    }
}
