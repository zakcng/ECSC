package main;

import controller.Controller;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import view.RootPane;

public class ApplicationLoader extends Application {

	private RootPane view;

	@Override
	public void init() {
		view = new RootPane();

		new Controller(view);
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setMinWidth(650);
		stage.setMinHeight(550);
		stage.setTitle("EPC");

		stage.setScene(new Scene(view));
		stage.show();

		//Add function
		view.setOnKeyPressed((event) -> { if(event.getCode() == KeyCode.ENTER) { view.getCp(); } });

	}


	public static void main(String[] args) {
		launch(args);
	}

}