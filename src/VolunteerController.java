  
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class VolunteerController implements Initializable {
	@FXML
	private Button volunteer;
	private ObservableList<Event> events;
	int index;
	@FXML
	private ListView<String> eventNameList;
	@FXML
	private Label nameLbl;
	@FXML
	private Label descLbl;
	@FXML
	private Label locLbl;
	@FXML
	private Label dateLbl;
	@FXML
	private Label timeLbl;
	@FXML
	private Label volLbl;
	@FXML
	private GridPane eventDetailsGrid;
	@FXML
	private Button homeBtn;
	@FXML
	private Button volunteerBtn;
	@FXML
	private Button cancelBtn;
	@FXML
	private Button donateBtn;
	@FXML
	private TextField donateField;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		events = FXCollections.observableArrayList();
		eventDetailsGrid.setVisible(false);
		cancelBtn.setDisable(true);

		if (Start.userType.equals("Guest")) {
			volunteerBtn.setDisable(true);
			volunteerBtn.setVisible(false);
			cancelBtn.setVisible(false);
			donateField.setVisible(false);
			donateBtn.setVisible(false);
		}
		else if(Start.userType.equals("Volunteer")) {
			donateField.setVisible(false);
			donateBtn.setVisible(false);
		}

		try {
			initEvents();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void initEvents() throws SQLException {
		Start.db.connect();

		String query = "SELECT * FROM Event";
		ResultSet results = Start.db.runQuery(query);

		while (results.next()) {
			int id = results.getInt("EventId");
			String name = results.getString("Name");
			String desc = results.getString("Description");
			String loc = results.getString("Location");
			String date = results.getString("Date");
			int startTime = results.getInt("TimeStart");
			int endTime = results.getInt("TimeEnd");
			int volNeeded = results.getInt("VolNeeded");
			int volFilled = results.getInt("VolFilled");

			Event e = new Event(id, name, desc, date, loc, startTime, endTime, volNeeded, volFilled);
			events.add(e);
			eventNameList.getItems().add(e.getName());
		}
		Start.db.disconnect();

		eventNameList.setOnMousePressed((MouseEvent e) -> {
			if (e.getButton().equals(MouseButton.PRIMARY)) {
				index = eventNameList.getSelectionModel().getSelectedIndex();
				try {
					showEventDetails();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	private void showEventDetails() throws SQLException {
		if (index < 0)
			index = 0;
		Event e = events.get(index);
		nameLbl.setText(e.getName());
		descLbl.setText(e.getDescription());
		locLbl.setText(e.getAddress());
		dateLbl.setText(e.getDate());
		String time = convertTimes();
		timeLbl.setText(time);
		String vols = convertVols(e.getVolFilled(), e.getVolNeeded());
		volLbl.setText(vols);
		eventDetailsGrid.setVisible(true);
		
		/*
		 * Set Volunteer enabled or disabled depending on if someone has already volunteered 
		 */
		Start.db.connect();
		String query = "SELECT Users.ID " + 
				"	FROM Event NATURAL JOIN EventUsers JOIN Users ON EventUsers.UserId = Users.ID " + 
				"	WHERE Users.ID = ? AND EventId = ?";
		PreparedStatement stmt = Start.db.connection.prepareStatement(query);
		stmt.setInt(1, Start.userId);
		stmt.setInt(2, e.getId());
		ResultSet results = stmt.executeQuery();
		Start.db.disconnect();
		if(results.next()) {
			volunteerBtn.setDisable(true);
			cancelBtn.setDisable(false);
		}
		else {
			volunteerBtn.setDisable(false);
			cancelBtn.setDisable(true);
		}
	}

	private String convertTimes() {
		String time = "";
		Event e = events.get(index);
		int start = e.getTimeStart();
		int end = e.getTimeEnd();
		if (start > 12) {
			start = start - 12;
			time += start + ":00 pm - ";
		} else if (start == 12) {
			time += start + ":00 pm - ";
		} else if (start == 0) {
			time += "12:00 am - ";
		} else {
			time += start + ":00 am - ";
		}
		if (end > 12) {
			end = end - 12;
			time += end + ":00 pm";
		} else if (end == 12) {
			time += end + ":00 pm";
		} else {
			time += end + ":00 am";
		}
		return time;
	}

	private String convertVols(int volNeed, int volReq) {
		return volNeed + " / " + volReq;
	}

	public void volunteer(ActionEvent event) throws SQLException {

		boolean successfulVol = true;

		
		if (events.get(index).getVolFilled() + 1 > events.get(index).getVolNeeded()) {
			successfulVol = false;
			denySubmission("Error", "Event is already full of volunteers");
		}
		
		/*
		 * *****************************************************************
		 * Check for conflicting times on the same date
		 */
		String eventDate = events.get(index).getDate();
		
		Start.db.connect();
		String conflictQuery = "SELECT TimeStart, TimeEnd" + 
				"	FROM Event NATURAL JOIN EventUsers JOIN Users ON EventUsers.UserId = Users.ID " + 
				"	WHERE Users.ID = ? AND Event.Date = ?";
		PreparedStatement stmt = Start.db.connection.prepareStatement(conflictQuery);
		stmt.setInt(1, Start.userId);
		stmt.setString(2, eventDate);
		ResultSet results = stmt.executeQuery();
		
		
		int prevStart = 0;
		int prevEnd = 0;
		int currStart = events.get(index).getTimeStart();
		int currEnd = events.get(index).getTimeEnd();
		
		while(results.next()) {
			prevStart = results.getInt("TimeStart");
			prevEnd = results.getInt("TimeEnd");
			
			// events start at same time
			if(currStart == prevStart) {
				successfulVol = false;
				denySubmission("Error", "Already volunteered for event with overlapping times!");
				break;
			}
			// event starts before existing event, but ends after existing event starts
			else if(currStart < prevStart && currEnd > prevStart) {
				successfulVol = false;
				denySubmission("Error","Already volunteered for event with overlapping times!");
				break;
			}
			// event starts after existing event starts, but ends before existing event ends
			// (time is inside of event)
			else if(currStart > prevStart && currEnd < prevEnd) {
				successfulVol = false;
				denySubmission("Error","Already volunteered for event with overlapping times!");
				break;
			}
			//event starts inside existing event, ends after exisiting event ends
			else if(currStart < prevEnd && currEnd > prevEnd) {
				successfulVol = false;
				denySubmission("Error","Already volunteered for event with overlapping times!");
				break;
			}
		}
		Start.db.disconnect();
		
		/*
		 *
		 ********************************************************************/

		if (successfulVol) {
			/*
			 * Update EventUsers Table First
			 */
			Start.db.connect();
			String query = "INSERT INTO EventUsers VALUES (?, ?, ?)";
			stmt = Start.db.connection.prepareStatement(query);
			int id = getNextId();
			int uId = Start.userId;
			int eventId = events.get(index).getId();
			stmt.setInt(1, id);
			stmt.setInt(2, uId);
			stmt.setInt(3, eventId);
			stmt.executeUpdate();
			Start.db.disconnect();
			/*
			 * Update Volunteer in Event Table
			 */
			Start.db.connect();
			query = "UPDATE Event SET VolFilled = VolFilled + 1 " + "WHERE EventId = ?";
			stmt = Start.db.connection.prepareStatement(query);
			stmt.setInt(1, events.get(index).getId());
			stmt.executeUpdate();
			Start.db.disconnect();
		}
		// repopulate events
		events.clear();
		eventNameList.getItems().clear();
		initEvents();
		showEventDetails();
		if (successfulVol) {
			confirmSubmission("Success!", "You are signed up for the event, " + Start.user +"!");
		}
	}
	
	private int getNextId() throws SQLException {
		Start.db.connect();
		String query = "SELECT Max(ID) " + "FROM EventUsers";
		ResultSet results = Start.db.runQuery(query);
		int max = results.getInt(1);
		Start.db.disconnect();
		return max + 1;
	}
	
	private int getNextDonationId() throws SQLException {
		Start.db.connect();
		String query = "SELECT Max(DonationId) " + "FROM EventDonations";
		ResultSet results = Start.db.runQuery(query);
		int max = results.getInt(1);
		Start.db.disconnect();
		return max + 1;
	}
	
	public void cancel(ActionEvent event) throws SQLException {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Cancel Confirmation");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to cancel your volunteer slot?");
		
		Optional<ButtonType> result = alert.showAndWait();
		/*
		 * Remove Volunteer Slot from database and decrement volunteer count for event
		 */
		if(result.get() == ButtonType.OK) {
			Start.db.connect();
			String query = "DELETE FROM EventUsers WHERE UserId = ? "
					+ "AND EventId = ?";
			PreparedStatement stmt = Start.db.connection.prepareStatement(query);
			stmt.setInt(1, Start.userId);
			stmt.setInt(2, events.get(index).getId());
			stmt.executeUpdate();
			Start.db.disconnect();
			
			Start.db.connect();
			query = "UPDATE Event SET VolFilled = VolFilled - 1 " + "WHERE EventId = ?";
			stmt = Start.db.connection.prepareStatement(query);
			stmt.setInt(1, events.get(index).getId());
			stmt.executeUpdate();
			Start.db.disconnect();
			
			confirmSubmission("Cancellation", "You have cancelled your volunteer slot for the " +
									events.get(index).getName() + " event");
			// repopulate events
			events.clear();
			eventNameList.getItems().clear();
			initEvents();
			showEventDetails();
		}
	}
	
	public void donate(ActionEvent event) throws SQLException {
		int donationAmnt = 0;
		boolean checkDonation = true;
		try {
			donationAmnt = Integer.parseInt(donateField.getText());
		} catch (NumberFormatException ex) {
			checkDonation = false;
			denySubmission("Error", "Please enter a valid dollar amount");
		}
		if(checkDonation) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Donation Confirmation");
			alert.setHeaderText(null);
			alert.setContentText("Are you sure you want to donate $" + donationAmnt + " to the event: "
									+ events.get(index).getName() + "?");
			
			Optional<ButtonType> result = alert.showAndWait();
			
			if(result.get() == ButtonType.OK) {
				Start.db.connect();
				String query = "INSERT INTO EventDonations VALUES (?,?,?,?)";
				PreparedStatement stmt = Start.db.connection.prepareStatement(query);
				int dId = getNextDonationId();
				int eId = events.get(index).getId();
				int uId = Start.userId;
				stmt.setInt(1, dId);
				stmt.setInt(2, eId);
				stmt.setInt(3, uId);
				stmt.setInt(4, donationAmnt);
				stmt.executeUpdate();
				Start.db.disconnect();
				
				confirmSubmission("Donation Confirmation", "You have donated $" + donationAmnt + " to the "
						+ "event: " + events.get(index).getName());
			}
			
			donateField.clear();
		}
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

	private void denySubmission(String header, String content) {
		Alert confAlert = new Alert(AlertType.WARNING);
		confAlert.setHeaderText(header);
		confAlert.setContentText(content);
		confAlert.showAndWait();
	}
}

