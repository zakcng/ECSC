package main;

import controller.Controller;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Model;
import view.RootPane;

public class ApplicationLoader extends Application {

	private RootPane view;

	@Override
	public void init() {
		view = new RootPane();
		Model model = new Model();

		new Controller(view, model);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		//whilst you can set a min width and height (example shown below) for the stage window,
		//you should not set a max width or height and the application should
		//be able to be maximised to fill the screen and ideally behave sensibly when resized
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