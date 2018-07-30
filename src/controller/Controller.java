package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import javafx.scene.control.TextInputDialog;
import main.Client;
import main.Server;
import model.User;
import model.Chat;

import view.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Optional;


public class Controller {

    //fields to be used throughout the class
    private RootPane view;
    private MyMenuBar mmb;
    private LoginPane lp;
    private NewChatPane ncp;
    private ChatPane cp;
    private Client client;
    private final int OK = 0;
    private final int ERROR = 1;

    //transient fields
    private transient String hashedPass = null;

    public Controller(RootPane view) {
        //initialise model and view fields
        this.client = new Client();
        this.view = view;

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

        lp.addJoinHandler(new JoinHandler());
        lp.addRefreshHandler((new RefreshHandler()));

        lp.getNcp().addCreateHandler(new CreateChatHandler());

        lp.getNcp().addTestHandler(new TestHandler());

        //cp.addSendHandler(new HANDLER);

        mmb.addExitHandler(e -> System.exit(0));
        mmb.addAboutHandler(e -> this.alertDialogBuilder(AlertType.INFORMATION, "Information Dialog", null, "EPC v1.0" + System.lineSeparator() + System.lineSeparator() + "Zak Ng" + System.lineSeparator() + "Lewys Ward"));
    }

    /* this method attaches bindings in the view, e.g. for validation, and to the model to ensure synchronisation between the data model and view */
    private void attachBindings() {
        //attaches a binding such that the add button in the view will be disabled whenever either of the text fields in the NamePane are empty
        //bp.addBtnDisableBind(np.isEitherFieldEmpty());

    }

    /**
     * Client code goes to server when handled in this class
     */
    private class CreateChatHandler implements EventHandler<ActionEvent> {

        public void handle(ActionEvent e) {
            System.out.println("Hello");
            String name = lp.getNcp().getTxtName();
            hashedPass = client.hash(lp.getNcp().getTxtChatPassword());

            System.out.println(hashedPass);

            Boolean passEnabled = lp.getNcp().getCbChatPassChecked().isSelected();
            Boolean logEnabled = lp.getNcp().getCbChatLogChecked().isSelected();

            try {
                int response = -1;

                response = client.createChat(name, hashedPass, passEnabled, logEnabled);

                if (response == ERROR) {
                    System.out.println("Error, server could not create chat.");
                    System.out.println("Ensure a unique server name and non blank password if enabled.");
                }

            } catch (IOException E) {
                E.printStackTrace();
            }

			/*
			System.out.println("Chat room name:");
			System.out.println(lp.getNcp().getTxtName());

			System.out.println("Chat room password:");
			System.out.println(lp.getNcp().getTxtChatPassword());

			System.out.println("Is password checked?");
			System.out.println(lp.getNcp().getCbChatPassChecked().isSelected());

			System.out.println("Is chat logs checked?");
			System.out.println(lp.getNcp().getCbChatLogChecked().isSelected());
			/*
			/*
			if(lp.getNcp().getCbChatPassChecked().isSelected() == false) {
				createChat(lp.getNcp().getTxtName(),null,lp.getNcp().getCbChatLogChecked().isSelected());
			} else
			createChat(lp.getNcp().getTxtName(),lp.getNcp().getTxtChatPassword(),lp.getNcp().getCbChatLogChecked().isSelected());
			*/
        }
    }


    private class JoinHandler implements EventHandler<ActionEvent> {

        public void handle(ActionEvent e) {
            try {
                User user = new User(lp.getNickname());

                System.out.println(user.getIpAddress());
                System.out.println(user.getNickname());

                //Print selected chat name as String
                System.out.println(lp.getSelectedChat().toString());

                int response = client.joinChat(lp.getSelectedChat(), client.hash("password"), user);


                if (response == OK) {
					System.out.println("Cockman");
                    view.changeTab(1);
                }


            } catch (Exception ex) {
                ex.printStackTrace();
            }


        }
    }

    private class RefreshHandler implements EventHandler<ActionEvent> {

        public void handle(ActionEvent e) {
            view.changeTab(2);
            try {
                if (client.refreshChats() == OK) {
                    client.setChatNames(client.loadChats(client.getDataInputStream()));
                    lp.clearChatList();

                    for (String chat : client.getChatNames()) {
                        lp.addChat(chat);
                    }
                } else {
                    System.out.println("Couldn't get chats from server.");
                }
            } catch (IOException E) {
                E.printStackTrace();
            }
        }
    }

    private class SendHandler implements EventHandler<ActionEvent> {

        public void handle(ActionEvent e) {
           //Send Chat Button Handler

        }
    }


    private class TestHandler implements EventHandler<ActionEvent> {

        public void handle(ActionEvent e) {
            System.out.println("Testing");
            passwordDialog();
        }
    }


	/*
	private class AddHandler implements EventHandler<ActionEvent> {

		public void handle(ActionEvent e) {
			//CODE
		}
	}*/

	private String passwordDialog() {
	    String temp = "";
        TextInputDialog passwordDialog = new TextInputDialog();
        passwordDialog.setHeaderText("");
        passwordDialog.setContentText("Please enter the chat password:");

        Optional<String> result = passwordDialog.showAndWait();
        if (result.isPresent()){
            temp =  result.get();
        }

        System.out.println(temp);

        return temp;
    }

    private void createChat(String chatName, String chatPassword, Boolean chatLog, Boolean passwordEnabled) {
        Chat chat = new Chat(chatName, chatPassword, chatLog, passwordEnabled);
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
