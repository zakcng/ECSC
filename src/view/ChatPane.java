package view;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class ChatPane extends BorderPane {

    public ChatPane() {
        HBox leftContainer = new HBox();
        leftContainer.setSpacing(5);
        VBox activePane = new VBox();
        activePane.setPadding(new Insets(20, 20, 20, 20));

        Label lblActive = new Label("Active");
        activePane.getChildren().add(lblActive);



        Separator separator1 = new Separator();
        separator1.setOrientation(Orientation.VERTICAL);
        leftContainer.getChildren().addAll(activePane,separator1);

        this.setLeft(leftContainer);
    }
}
