package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.List;

public class ChatPane extends BorderPane {

    TextArea txtMessages;
    TextField txtOutgoing;
    Button btnSend;

    private ListView<List> lvwUsers;
    private ObservableList<List> olUsers;


    public ChatPane() {
        olUsers = FXCollections.observableArrayList();

        lvwUsers = new ListView<>(olUsers);

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
        this.txtMessages.appendText(line);
    }
}
