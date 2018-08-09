package main;

import controller.Controller;
import javafx.application.Application;
import javafx.concurrent.Task;
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

        Task <Void> task = new Task<Void>() {
            @Override public Void call() throws InterruptedException {
                //CODE

                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

		stage.setScene(new Scene(view));
		stage.show();


	}

	public static void main(String[] args) {
		launch(args);
	}

}