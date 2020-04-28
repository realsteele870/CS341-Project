import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Start extends Application {
	private Stage loginPage;
	@FXML
	private Button loginBtn;
	@FXML
	private Button guestBtn;
	@FXML
	private TextField usernameField;
	@FXML
	private PasswordField passwordField;
	public static Database db;
	public static String user;
	public static String userType;
	public static int userId;

	public static void main(String[] args) {
		db = new Database();
		launch(args);

	}

	public void start(Stage loginStage) throws IOException, SQLException {

		Parent root = FXMLLoader.load(getClass().getResource("Login_FINAL.fxml"));
		Scene scene = new Scene(root);
		loginPage = loginStage;
		loginPage.setTitle("Login");
		loginPage.setMaximized(true);
		loginPage.setScene(scene);
		loginPage.show();

	}

	@FXML
	public void login(ActionEvent event) throws IOException, SQLException {
		db.connect();

		String userName = usernameField.getText(); // get users entered username
		String userPass = passwordField.getText(); // get users entered password
		userName = userName.replaceAll(" ", "");
		// query to get password of user
		String query = "SELECT Password, FirstName, UserType, ID FROM Users WHERE UserName = ?";
		PreparedStatement stmt = db.connection.prepareStatement(query);
		stmt.setString(1, userName);
		ResultSet result = stmt.executeQuery();

		if (result.isClosed()) {
			errorAlert("Login Error", "Username does not exist.");
			usernameField.clear();
			passwordField.clear();
			db.disconnect();
		} else {
			// get database query result
			String pass = result.getString("Password");
			user = result.getString("FirstName");
			userType = result.getString("UserType");
			userId = result.getInt("ID");
			db.disconnect();

			// if user password equals database password
			if (pass.equals(userPass)) {
				Parent menuParent = FXMLLoader.load(getClass().getResource("Home_Final.fxml"));
				Scene scene = loginBtn.getScene(); // use button to get current scene
				Scene scene2 = new Scene(menuParent, scene.getWidth(), scene.getHeight()); // create new scene with last
																							// scenes dimensions
				Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
				window.setScene(scene2);
				window.setTitle("Home");
				window.show();
			}
		}
	}

	// method to alert user what the error is
	private void errorAlert(String header, String content) {
		Alert errorAlert = new Alert(AlertType.ERROR);
		errorAlert.setHeaderText(header);
		errorAlert.setContentText(content);
		errorAlert.showAndWait();
	}

	@FXML
	private void guestLogin(ActionEvent event) throws IOException {
		Parent menuParent = FXMLLoader.load(getClass().getResource("Home_Final.fxml"));
		Scene scene = guestBtn.getScene(); // use button to get current scene
		Scene scene2 = new Scene(menuParent, scene.getWidth(), scene.getHeight()); // create new scene with last scenes
																					// dimensions
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(scene2);
		window.setTitle("Home");
		window.show();
		user = null;
	}
}