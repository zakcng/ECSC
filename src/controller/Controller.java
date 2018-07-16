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

import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.User;
import model.Chat;
import model.Model;

import view.*;


public class Controller {

	//fields to be used throughout the class
	private MyMenuBar mmb;
	private LoginPane lp;
	private NewChatPane ncp;
	private ChatPane cp;
	private Model model;

	public Controller(RootPane view, Model model) {
		//initialise model and view fields
		this.model = model;

		mmb = view.getMenuBar();
		ncp = view.getNcp();
		lp = view.getLoginPane();
		cp = view.getCp();

		//attach event handlers to view using private helper method
		this.attachEventHandlers();

		//attach bindings within view using private helper method
		this.attachBindings();
	}

	private void attachEventHandlers() {
		//attaching event handlers
		//bp.addAddHandler(new AddHandler());

		lp.addJoinHandler(new ChatHandler());
		lp.getNcp().addCreateHandler(new CreateChatHandler());

		//cp.addSendHandler(new HANDLER);

		mmb.addExitHandler(e -> System.exit(0));
		mmb.addAboutHandler(e -> this.alertDialogBuilder(AlertType.INFORMATION, "Information Dialog", null, "EPC v1.0" +  System.lineSeparator() + System.lineSeparator() + "Zak Ng" + System.lineSeparator() + "Lewys Ward"));
	}

	/* this method attaches bindings in the view, e.g. for validation, and to the model to ensure synchronisation between the data model and view */
	private void attachBindings() {
		//attaches a binding such that the add button in the view will be disabled whenever either of the text fields in the NamePane are empty
		//bp.addBtnDisableBind(np.isEitherFieldEmpty());

	}

	private class CreateChatHandler implements EventHandler<ActionEvent> {

		public void handle(ActionEvent e) {
			System.out.println("Chat room name:");
			System.out.println(lp.getNcp().getTxtName());

			System.out.println("Chat room password:");
			System.out.println(lp.getNcp().getTxtChatPassword());

			System.out.println("Is password checked?");
			System.out.println(lp.getNcp().getCbChatPassChecked().isSelected());

			System.out.println("Is chat logs checked?");
			System.out.println(lp.getNcp().getCbChatLogChecked().isSelected());

			if(lp.getNcp().getCbChatPassChecked().isSelected() == false) {
				createChat(lp.getNcp().getTxtName(),null,lp.getNcp().getCbChatLogChecked().isSelected());
			} else
			createChat(lp.getNcp().getTxtName(),lp.getNcp().getTxtChatPassword(),lp.getNcp().getCbChatLogChecked().isSelected());
		}
	}




	private class ChatHandler implements EventHandler<ActionEvent> {

		public void handle(ActionEvent e) {
			User user = new User(lp.getNickname());
			
			System.out.println("Testing");

		}
	}

	/*
	private class AddHandler implements EventHandler<ActionEvent> {

		public void handle(ActionEvent e) {
			//CODE
		}
	}*/

	private void createChat(String chatName, String chatPassword, Boolean chatLog) {
		Chat chat = new Chat(chatName, chatPassword, chatLog);
	}

	private void appendTextArea(String line) {
		cp.appendLineToTxtMessages(line);
	}


	//helper method to build dialogs
	private void alertDialogBuilder(AlertType type, String title, String header, String content) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}

}
