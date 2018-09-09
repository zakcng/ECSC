package main;

import controller.Controller;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.PasswordManager;
import view.RootPane;

import java.io.File;
import java.io.FileInputStream;

public class ApplicationLoader extends Application {

	private RootPane view;
	private Controller controller;
	private PasswordManager passwordManager;

	@Override
	public void init() {
		passwordManager = new PasswordManager(true);
		view = new RootPane();

		controller = new Controller(view, passwordManager);
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setMinWidth(650);
		stage.setMinHeight(550);
		stage.setTitle("EPC");


		stage.setScene(new Scene(view));

		File file = new File(System.getProperty("user.dir") + "/data/epclogo.png");

		if (file.exists()) {
			FileInputStream inputStream = new FileInputStream(System.getProperty("user.dir") + "/data/epclogo.png");
			Image anotherIcon = new Image(inputStream);
			stage.getIcons().add(anotherIcon);
		}

		stage.show();

		//Add function
		view.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (keyEvent.getCode() == KeyCode.ENTER && view.currentTab() == 1) {
					controller.sendMessageFunc();

				}
			}
		});

		}


	public static void main(String[] args) { launch(args); }

}