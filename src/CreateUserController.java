import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.sqlite.SQLiteException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateUserController implements Initializable {

	@FXML
	private Button homeBtn;
	@FXML
	private TextField firstNameTextField;
	@FXML
	private TextField lastNameTextField;
	@FXML
	private TextField userNameTextField;
	@FXML
	private TextField passwordTextField;
	@FXML
	private TextField userTypeTextField;
	@FXML
	private Button createAccntBtn;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

	@FXML
	private void createUser(ActionEvent event) throws SQLException {
		boolean createdAccount = true;
		String fName = firstNameTextField.getText();
		String lName = lastNameTextField.getText();
		String uName = userNameTextField.getText();
		String pWord = passwordTextField.getText();
		String uType = userTypeTextField.getText();
		int userId = getNextUserId();

		Start.db.connect();
		String insertStatement = "INSERT INTO Users VALUES (?, ?, ?, ?, ?, ?)";
		PreparedStatement stmt = Start.db.connection.prepareStatement(insertStatement);
		stmt.setInt(1, userId);
		stmt.setString(2, fName);
		stmt.setString(3, lName);
		stmt.setString(4, uType);
		stmt.setString(5, uName);
		stmt.setString(6, pWord);
		try {
			stmt.executeUpdate();
		} catch (SQLiteException e) {
			// add in error pane to make visible if unique constrain (same username)
			// violation
			userAlreadyExists("Previous Account", "An account is already made for this user");
			createdAccount = false;
		}
		Start.db.disconnect();

		// clear fields
		firstNameTextField.clear();
		lastNameTextField.clear();
		userNameTextField.clear();
		passwordTextField.clear();
		userTypeTextField.clear();
		if (createdAccount) {
			confirmSubmission("Success!", "Your account was successfully created!");
		}
	}

	private int getNextUserId() throws SQLException {
		Start.db.connect();
		String query = "SELECT Max(ID) " + "FROM Users";
		ResultSet results = Start.db.runQuery(query);
		int max = results.getInt(1);
		Start.db.disconnect();
		return max + 1;
	}

	@FXML
	private void home(ActionEvent event) throws IOException {
		Parent menuParent = FXMLLoader.load(getClass().getResource("Home_Final.fxml"));
		Scene scene = homeBtn.getScene(); // use button to get current scene
		Scene scene2 = new Scene(menuParent, scene.getWidth(), scene.getHeight()); // create new scene with last scenes
																					// dimensions
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(scene2);
		window.setTitle("Home");
		window.show();
	}

	private void confirmSubmission(String header, String content) {
		Alert confAlert = new Alert(AlertType.INFORMATION);
		confAlert.setHeaderText(header);
		confAlert.setContentText(content);
		confAlert.showAndWait();
	}

	private void userAlreadyExists(String header, String content) {
		Alert confAlert = new Alert(AlertType.ERROR);
		confAlert.setHeaderText(header);
		confAlert.setContentText(content);
		confAlert.showAndWait();
	}

}
