package view;

import javafx.geometry.Insets;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class RootPane extends BorderPane {


	private MyMenuBar mmb;
	
	public RootPane() {
		//background
		this.setStyle("-fx-background-color: #415D78;");

		mmb = new MyMenuBar();

		this.setTop(mmb);
		//this.setCenter(rootContainer);
	}

	/* These methods provide a public interface for the root pane and allow
	 * each of the sub-containers to be accessed by the controller.
	 */

	public MyMenuBar getMenuBar() {
		return mmb;
	}
}
