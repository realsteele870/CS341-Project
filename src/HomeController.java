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
	private Button dashBtn;
	@FXML
	private Button createUserBtn;
	@FXML
	private Button logoutBtn;
	@FXML
	private Button createEvntBtn;
	@FXML
	private Button viewEventBtn;
	@FXML
	private Button volunteerBtn;
	@FXML
	private Button adminBtn;
	@FXML
	private Label loggedInAs;
	@FXML
	private Button helpBtn;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		viewEventBtn.setVisible(false);
		if (Start.user == null) {
			loggedInAs.setText("Guest");
			Start.userType = "Guest";
			dashBtn.setDisable(true);
			dashBtn.setVisible(false);

		} else {
			loggedInAs.setText(Start.user);
		}
		if (!Start.userType.equals("Administrator")) {
			createUserBtn.setDisable(true);
			createUserBtn.setVisible(false);
			createEvntBtn.setDisable(true);
			createEvntBtn.setVisible(false);
			adminBtn.setVisible(false);

		}
	}

	@FXML
	private void createUser(ActionEvent event) throws IOException {
		Parent menuParent = FXMLLoader.load(getClass().getResource("CreateUser.fxml"));
		Scene scene = createUserBtn.getScene(); // use button to get current scene
		Scene scene2 = new Scene(menuParent, scene.getWidth(), scene.getHeight()); // create new scene with last scenes
																					// dimensions
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(scene2);
		window.setTitle("Create User");
		window.show();
	}

	@FXML
	private void volunteer(ActionEvent event) throws IOException {
		Parent menuParent = FXMLLoader.load(getClass().getResource("Volunteer.fxml"));
		Scene scene = volunteerBtn.getScene(); // use button to get current scene
		Scene scene2 = new Scene(menuParent, scene.getWidth(), scene.getHeight()); // create new scene with last scenes
																					// dimensions
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(scene2);
		window.setTitle("Volunteer");
		window.show();

	}

	@FXML
	private void viewEvent(ActionEvent event) throws IOException {
		Parent menuParent = FXMLLoader.load(getClass().getResource("ViewEvents.fxml"));
		Scene scene = viewEventBtn.getScene(); // use button to get current scene
		Scene scene2 = new Scene(menuParent, scene.getWidth(), scene.getHeight()); // create new scene with last scenes
																					// dimensions
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(scene2);
		window.setTitle("View Events");
		window.show();

	}

	@FXML
	private void createEvent(ActionEvent event) throws IOException {
		Parent menuParent = FXMLLoader.load(getClass().getResource("CreateEvent.fxml"));
		Scene scene = createEvntBtn.getScene(); // use button to get current scene
		Scene scene2 = new Scene(menuParent, scene.getWidth(), scene.getHeight()); // create new scene with last scenes
																					// dimensions
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(scene2);
		window.setTitle("Create Event");
		window.show();
	}

	@FXML
	private void goToAdmin(ActionEvent event) throws IOException {
		Parent menuParent = FXMLLoader.load(getClass().getResource("AdminPage.fxml"));
		Scene scene = createEvntBtn.getScene(); // use button to get current scene
		Scene scene2 = new Scene(menuParent, scene.getWidth(), scene.getHeight()); // create new scene with last scenes
																					// dimensions
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(scene2);
		window.setTitle("Admin Dashboard");
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
		Start.user = null;
	}

	@FXML
	private void dashboard(ActionEvent event) throws IOException {
		Parent menuParent = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
		Scene scene = dashBtn.getScene(); // use button to get current scene
		Scene scene2 = new Scene(menuParent, scene.getWidth(), scene.getHeight()); // create new scene with last scenes
																					// dimensions
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(scene2);
		window.setTitle("Dashboard");
		window.show();

	}

	@FXML
	private void helpPage(ActionEvent event) throws IOException {
		Parent menuParent = FXMLLoader.load(getClass().getResource("Help.fxml"));
		Scene scene = helpBtn.getScene(); // use button to get current scene
		Scene scene2 = new Scene(menuParent, scene.getWidth(), scene.getHeight()); // create new scene with last scenes
																					// dimensions
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(scene2);
		window.setTitle("Dashboard");
		window.show();

	}
}
