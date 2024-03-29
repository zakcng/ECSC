package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import javafx.scene.control.TextInputDialog;
import main.Client;
import main.ClientThread;
import model.PasswordManager;
import model.User;
import model.Chat;
import model.Protocol;

import view.*;

import java.io.IOException;
import java.util.Optional;


public class Controller {

    //fields to be used throughout the class
    private RootPane view;
    private MyMenuBar mmb;
    private LoginPane lp;
    private NewChatPane ncp;
    private ChatPane cp;
    private Client client;
    private PasswordManager passwordManager;
    private User user;

    //transient fields
    private transient String hashedPass = null;

    public Controller(RootPane view, PasswordManager passwordManager) {
        //initialise model and view fields
        this.view = view;
        this.passwordManager = passwordManager;

        mmb = view.getMenuBar();
        ncp = view.getNcp();
        lp = view.getLoginPane();
        cp = view.getCp();

        //attach event handlers to view using private helper method
        this.attachEventHandlers();

        //attach bindings within view using private helper method
        this.attachBindings();

        try {
            this.client = new Client(passwordManager.getIp(), passwordManager.getStore(), passwordManager.getStorePass());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }

    }

    private void attachEventHandlers() {
        //attaching event handlers
        //bp.addAddHandler(new AddHandler());

        lp.addJoinHandler(new JoinHandler());
        lp.addRefreshHandler((new RefreshHandler()));

        lp.getNcp().addCreateHandler(new CreateChatHandler());

        lp.getNcp().addTestHandler(new TestHandler());

        cp.addSendHandler(new SendHandler());
        //cp.addSendHandler(new HANDLER);

        mmb.addExitHandler(e -> System.exit(0));
        mmb.addAboutHandler(e -> this.alertDialogBuilder(AlertType.INFORMATION, "Information Dialog", null, "EPC v1.0" + System.lineSeparator() + System.lineSeparator() + "Zak Ng" + System.lineSeparator() + "Lewys Ward"));
    }

    /* this method attaches bindings in the view, e.g. for validation, and to the model to ensure synchronisation between the data model and view */
    private void attachBindings() {


        lp.addJoinChatBind(lp.isAnyFieldEmpty());

    }

    /**
     * Client code goes to server when handled in this class
     */
    private class CreateChatHandler implements EventHandler<ActionEvent> {

        public void handle(ActionEvent e) {
            String name = lp.getNcp().getTxtName();
            String salt = client.salt();
            hashedPass = client.hash(lp.getNcp().getTxtChatPassword() + salt);
            Boolean passEnabled = lp.getNcp().getCbChatPassChecked();
            Boolean logEnabled = lp.getNcp().getCbChatLogChecked().isSelected();

            //Check user knows password was not enabled but text was entered
            Boolean passEmpty = lp.getNcp().getTxtChatPassword().isEmpty();
            Boolean cbChecked =  lp.getNcp().getCbChatPassChecked();
            if (!passEmpty && !cbChecked){
                System.out.println("Password was not enabled but text was entered");
                alertDialogBuilder(AlertType.WARNING,"Warning","Verification Error","You entered a password but did not enable one.");
            }

            try {
                int response = -1;

                response = client.createChat(name, hashedPass, passEnabled, logEnabled, salt);

                if (response == Protocol.ERROR.ordinal()) {
                    System.out.println("Error, server could not create chat.");
                    System.out.println("Ensure a unique server name and non blank password if enabled.");
                    alertDialogBuilder(AlertType.ERROR,"Error","Verification Error","Ensure a unique server name and non blank password if enabled.");
                }

            } catch (IOException E) {
                E.printStackTrace();
            }

        }
    }


    private class JoinHandler implements EventHandler<ActionEvent> {

        public void handle(ActionEvent e) {
            try {
                cp.clearTxtMessages();
                user = new User(lp.getNickname());

                //Retrieve password from user
                String password = passwordDialog();

                int response = client.joinChat(lp.getSelectedChat(), password, user);


                if (response == Protocol.OK.ordinal()) {
                    //Change tab on OK status
                    //TODO replace with constant variable
                    view.changeTab(1);
                    view.setTab0Disabled();
                    view.setTab1Enabled();
                    //TODO pass chatPane into client to update with messages
                    new Thread(new ClientThread(client, cp)).start();
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
                if (client.refreshChats() == Protocol.OK.ordinal()) {
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
            sendMessageFunc();

        }
    }


    private class TestHandler implements EventHandler<ActionEvent> {

        public void handle(ActionEvent e) {
            System.out.println(cp.getTxtMessage());
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
        PasswordDialog passwordDialog = new PasswordDialog();
        passwordDialog.setHeaderText("");
        passwordDialog.setContentText("Please enter the chat password:");

        Optional<String> result = passwordDialog.showAndWait();
        if (result.isPresent()) {
            temp = result.get();
        }

        return temp;
    }

    private void createChat(String chatName, String chatPassword, Boolean chatLog, Boolean passwordEnabled, String salt) {
        Chat chat = new Chat(chatName, chatPassword, chatLog, passwordEnabled, salt);
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

    public void sendMessageFunc() {
        if (cp.isTxtOutgoingEmpty()) {
            //Create Error Message
            System.out.println("Message is empty");
        } else {
            try {
                System.out.println(user.getNickname());
                String msg = user.getNickname() + ": " + cp.getTxtMessage();
                System.out.println(cp.getTxtMessage());
                int attempts = 0;
                int response = Protocol.ERROR.ordinal();

                do {
                    response = client.sendMsg(msg);
                    attempts++;
                } while (response == Protocol.ERROR.ordinal() && attempts < 3);

            } catch (IOException E) {
                E.printStackTrace();
            }

            cp.clearTextOutgoing();
        }
    }
}




