package view;

import javafx.geometry.Insets;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * RootPane class
 */
public class RootPane extends BorderPane {


	private MyMenuBar mmb;
	private TabPane tp;
	private LoginPane lp;
	private ChatPane cp;
	private NewChatPane ncp;
	
	public RootPane() {
		//background
		//this.setStyle("-fx-background-color: #415D78;");

		mmb = new MyMenuBar();
		tp = new TabPane();
		lp = new LoginPane();
		cp = new ChatPane();
		ncp = new NewChatPane();

		tp.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

		Tab t1 = new Tab("Login", lp);
		Tab t2 = new Tab("Chat", cp);
		//t2.setDisable(true);
		//Tab t3 = new Tab("New Chat Pane", ncp);

		tp.getTabs().addAll(t1,t2);


		this.setTop(mmb);
		this.setCenter(tp);
	}

	/* These methods provide a public interface for the root pane and allow
	 * each of the sub-containers to be accessed by the controller.
	 */

	public MyMenuBar getMenuBar() {
		return mmb;
	}

	public LoginPane getLoginPane() {
		return lp;
	}

	public NewChatPane getNcp() {
		return ncp;
	}

	public RootPane getRootPane() {
		return this;
	}
}
