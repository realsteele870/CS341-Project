import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

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
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateEventController implements Initializable {
	@FXML
	private TextField nameField;
	@FXML
	private TextArea descArea;
	@FXML
	private DatePicker dateField;
	@FXML
	private TextField timeStrtField;
	@FXML
	private TextField timeEndField;
	@FXML
	private TextField volunteersField;
	@FXML
	private Button createEventBtn;
	@FXML
	private Button viewEventBtn;
	@FXML
	private Button volunteerBtn;
	@FXML
	private Button homeBtn;
	@FXML
	private RadioButton startAmRBtn;
	@FXML
	private RadioButton startPmRBtn;
	@FXML
	private RadioButton endAmRBtn;
	@FXML
	private RadioButton endPmRBtn;
	@FXML
	private TextField locationField;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

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

	@FXML
	private void createEvent(ActionEvent event) throws SQLException {
		// get info from fields
		boolean checkVols = true;
		String name = nameField.getText();
		String desc = descArea.getText();
		String loc = locationField.getText();
		String date = dateField.getEditor().getText();
		String timeStart = timeStrtField.getText();
		String timeEnd = timeEndField.getText();
		int vols = 0;
		try {
			vols = Integer.parseInt(volunteersField.getText());
		} catch (NumberFormatException ex) {
			checkVols = false;
			denySubmission("Error", "Please enter a valid number of volunteers");
		}
		if (checkVols) {
			int startHour = toMilitaryTimeStart(timeStart);
			int endHour = toMilitaryTimeEnd(timeEnd);

			// enter event into database
			int eventId = getNextId();
			Start.db.connect();
			String query = "INSERT INTO Event VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement stmt = Start.db.connection.prepareStatement(query);
			stmt.setInt(1, eventId);
			stmt.setString(2, name);
			stmt.setString(3, desc);
			stmt.setString(4, loc);
			stmt.setString(5, date);
			stmt.setInt(6, startHour);
			stmt.setInt(7, endHour);
			stmt.setInt(8, vols);
			stmt.setInt(9, 0); // initial volunteers filled is 0
			stmt.executeUpdate();
			Start.db.disconnect();
		}
		// clear fields
		nameField.clear();
		descArea.clear();
		locationField.clear();
		dateField.getEditor().clear();
		timeStrtField.clear();
		timeEndField.clear();
		volunteersField.clear();
		startAmRBtn.setSelected(false);
		startPmRBtn.setSelected(false);
		endAmRBtn.setSelected(false);
		endPmRBtn.setSelected(false);
		if (checkVols) {
			confirmSubmission("Sucess!", "Your event was created!");
		}

	}

	private int getNextId() throws SQLException {
		Start.db.connect();
		String query = "SELECT Max(EventId) " + "FROM Event";
		ResultSet results = Start.db.runQuery(query);
		int max = results.getInt(1);
		Start.db.disconnect();
		return max + 1;
	}

	public int toMilitaryTimeStart(String time) {
		String[] times = time.split(":");
		time = times[0]; // gives hour of time entered
		int hour = Integer.parseInt(time);
		if (startAmRBtn.isSelected()) {
			// time is 12:00 am
			if (hour == 12) {
				hour = 0;
			}
			return hour;
		} else if (startPmRBtn.isSelected()) {
			// time is 12:00 pm
			if (hour == 12) {
				hour = 12;
				return hour;
			} else {
				hour = hour + 12;
			}
			return hour;
		} else {
			return 0; // should never reach, but if it does -- set hour to zero
		}
	}

	public int toMilitaryTimeEnd(String time) {
		String[] times = time.split(":");
		time = times[0]; // gives hour of time entered
		int hour = Integer.parseInt(time);
		if (endAmRBtn.isSelected()) {
			// time is 12:00 am
			if (hour == 12) {
				hour = 0;
			}
			return hour;
		} else if (endPmRBtn.isSelected()) {
			// time is 12:00 pm
			if (hour == 12) {
				hour = 12;
				return hour;
			} else {
				hour = hour + 12;
			}
			return hour;
		} else {
			return 0; // should never reach, but if it does -- set hour to zero
		}
	}

	public void switchRadioStart(ActionEvent event) {
		RadioButton rb = (RadioButton) event.getSource();
		if (rb.equals(startAmRBtn)) {
			if (startAmRBtn.isSelected() && startPmRBtn.isSelected()) {
				startPmRBtn.setSelected(false);
			}
		} else {
			if (startAmRBtn.isSelected() && startPmRBtn.isSelected()) {
				startAmRBtn.setSelected(false);
			}
		}
	}

	public void switchRadioEnd(ActionEvent event) {
		RadioButton rb = (RadioButton) event.getSource();
		if (rb.equals(endAmRBtn)) {
			if (endAmRBtn.isSelected() && endPmRBtn.isSelected()) {
				endPmRBtn.setSelected(false);
			}
		} else {
			if (endAmRBtn.isSelected() && endPmRBtn.isSelected()) {
				endAmRBtn.setSelected(false);
			}
		}
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

	private void confirmSubmission(String header, String content) {
		Alert confAlert = new Alert(AlertType.INFORMATION);
		confAlert.setHeaderText(header);
		confAlert.setContentText(content);
		confAlert.showAndWait();
	}

	private void denySubmission(String header, String content) {
		Alert confAlert = new Alert(AlertType.WARNING);
		confAlert.setHeaderText(header);
		confAlert.setContentText(content);
		confAlert.showAndWait();
	}
}
