package main;

import controller.Controller;
import javafx.application.Application;
import javafx.scene.Scene;
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


	}

	public static void main(String[] args) {
		launch(args);
	}

}