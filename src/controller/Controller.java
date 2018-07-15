package controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Model;

import view.LoginPane;
import view.MyMenuBar;
import view.NewChatPane;
import view.RootPane;



public class Controller {

	//fields to be used throughout the class
	private MyMenuBar mmb;
	private LoginPane lp;
	private NewChatPane ncp;
	private Model model;

	public Controller(RootPane view, Model model) {
		//initialise model and view fields
		this.model = model;

		mmb = view.getMenuBar();
		ncp = view.getNcp();
		lp = view.getLoginPane();

		//attach event handlers to view using private helper method
		this.attachEventHandlers();	

		//attach bindings within view using private helper method
		this.attachBindings();
	}

	private void attachEventHandlers() {
		//attaching event handlers
		//bp.addAddHandler(new AddHandler());
		//lp.addBtnNewChatHandler(new NewChatHandler());

		mmb.addExitHandler(e -> System.exit(0));
		mmb.addAboutHandler(e -> this.alertDialogBuilder(AlertType.INFORMATION, "Information Dialog", null, "EPC v1.0" + System.lineSeparator() + "Zak Ng" + System.lineSeparator() + "Lewys Ward"));
	}

	/* this method attaches bindings in the view, e.g. for validation, and to the model to ensure synchronisation between the data model and view */
	private void attachBindings() {
		//attaches a binding such that the add button in the view will be disabled whenever either of the text fields in the NamePane are empty
		//bp.addBtnDisableBind(np.isEitherFieldEmpty());

	}

	/*
	private class NewChatHandler implements EventHandler<ActionEvent> {

		public void handle(ActionEvent e) {
			final Stage dialog = new Stage();
			dialog.initModality(Modality.APPLICATION_MODAL);
			dialog.initOwner();

			Scene dialogScene = new Scene(ncp, 300, 200);
			dialog.setScene(dialogScene);
			dialog.show();

		}
	}*/

	/*
	private class AddHandler implements EventHandler<ActionEvent> {

		public void handle(ActionEvent e) {
			//CODE
		}
	}*/


	//helper method to build dialogs
	private void alertDialogBuilder(AlertType type, String title, String header, String content) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}

}
