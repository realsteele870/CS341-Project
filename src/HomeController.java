import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class HomeController implements Initializable {
	@FXML
	private Button logoutBtn;
	@FXML
	private Button createEvntBtn;
	@FXML
	private Button viewEventBtn;
	@FXML
	private Button volunteerBtn;
	@FXML
	private Label loggedInAs;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		if (Start.user == null) {
			loggedInAs.setText("Guest");
		} else {
			loggedInAs.setText(Start.user);
		}

	}

	@FXML
	private void volunteer(ActionEvent event) {

	}

	@FXML
	private void viewEvent(ActionEvent event) {

	}

	@FXML
	private void createEvent(ActionEvent event) throws IOException {
		Parent menuParent = FXMLLoader.load(getClass().getResource("CreateEvent.fxml"));
		Scene scene = logoutBtn.getScene(); // use button to get current scene
		Scene scene2 = new Scene(menuParent, scene.getWidth(), scene.getHeight()); // create new scene with last scenes
																					// dimensions
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(scene2);
		window.setTitle("Create Event");
		window.show();
	}

	@FXML
	private void logOut(ActionEvent event) throws IOException {
		Parent menuParent = FXMLLoader.load(getClass().getResource("Login_FINAL.fxml"));
		Scene scene = logoutBtn.getScene(); // use button to get current scene
		Scene scene2 = new Scene(menuParent, scene.getWidth(), scene.getHeight()); // create new scene with last scenes
																					// dimensions
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(scene2);
		window.setTitle("Login");
		window.show();
	}
}
