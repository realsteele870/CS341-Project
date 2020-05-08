import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class AdminController implements Initializable {
	@FXML
	private Label nameLabel;
	@FXML
	private Label descLabel;
	@FXML
	private Label userLabel;
	@FXML
	private Label statusLabel;
	@FXML
	private Label dateLabel;
	@FXML
	private Label nameResult;
	@FXML
	private Label descResult;
	@FXML
	private Label userResult;
	@FXML
	private Label statusResult;
	@FXML
	private Label dateResult;
	@FXML
	private Label ttlHoursLabel;
	@FXML
	private Label ttlDnrEventsLabel;
	@FXML
	private Label ttlDnrOrgLabel;
	@FXML
	private Button deleteBtn;
	@FXML
	private Button empSumBtn;
	@FXML
	private Button homeBtn;
	@FXML
	private ListView<String> volsList;
	@FXML
	private ListView<String> eventList;
	@FXML
	private GridPane infoPane;
	@FXML
	private GridPane summPane;
	@FXML
	private Tab volTab;
	@FXML
	private Tab evtTab;
	
	private ObservableList<Event> events;
	private ObservableList<User> users;
	int userIndex;
	int eventIndex;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		events = FXCollections.observableArrayList();
		users = FXCollections.observableArrayList();
		summPane.setVisible(false);
		infoPane.setVisible(false);
		
		try {
			init();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public void init() throws SQLException {
		Start.db.connect();
	
			String userQuery = "SELECT * FROM Users";
			ResultSet results = Start.db.runQuery(userQuery);
		
			while(results.next()) {
				int id = results.getInt("ID");
				String first = results.getString("FirstName");
				String last = results.getString("LastName");
				String type = results.getString("UserType");
				String userName = results.getString("UserName");
				String dDate = results.getString("DeactiveDate");
			
				User u = new User(id, first, last, type, userName, "",dDate);
				users.add(u);
				String fullName = u.getFirstName() + " " + u.getLastName();
				volsList.getItems().add(fullName);
			}
		
		//show event details upon mouse click of user row in table
			volsList.setOnMousePressed((MouseEvent e) -> {
				if (e.getButton().equals(MouseButton.PRIMARY)) {
					userIndex = volsList.getSelectionModel().getSelectedIndex();
					showUserDetails();
				}
			});
			
			String eventQuery = "SELECT * FROM Event";
			ResultSet eventResults = Start.db.runQuery(eventQuery);
			
			while(eventResults.next()) {
				int id = eventResults.getInt("EventId");
				String name = eventResults.getString("Name");
				String desc = eventResults.getString("Description");
				String loc = eventResults.getString("Location");
				String date = eventResults.getString("Date");
				String cancDate = eventResults.getString("CancelDate");
				
				Event e = new Event(id, name, desc, date, loc, 0, 0, 0, 0, cancDate);
				events.add(e);
				eventList.getItems().add(e.getName());
			}
			
			Start.db.disconnect();
			
			eventList.setOnMousePressed((MouseEvent e) -> {
				if (e.getButton().equals(MouseButton.PRIMARY)) {
					eventIndex = eventList.getSelectionModel().getSelectedIndex();
					showEventDetails();
				}
			});
		
	
	}
	
	public void showUserDetails() {
		infoPane.setVisible(true);
		summPane.setVisible(false);
		if(userIndex < 0) userIndex = 0;
		
		User u = users.get(userIndex);
		nameLabel.setText("Name:");
		String fullName = u.getFirstName() + " " + u.getLastName();
		nameResult.setText(fullName);
		descLabel.setText("User Type:");
		descResult.setText(u.getUserType());
		userLabel.setText("Username:");
		userResult.setText(u.getUserName());
		statusLabel.setText("Status");
		
		deleteBtn.setText("Delete");
		empSumBtn.setText("Run Employee Summary Report");
		statusLabel.setText("Status:");
		if(u.getDeacDate() != null) {
			statusResult.setText("Deactive");
			deleteBtn.setDisable(true);
		}
		else {
			statusResult.setText("Active");
			deleteBtn.setDisable(false);
		}
		dateLabel.setVisible(false);
		dateResult.setVisible(false);
	}
	
	public void showEventDetails() {
		infoPane.setVisible(true);
		summPane.setVisible(false);
		if(eventIndex < 0) eventIndex = 0;
		
		Event e = events.get(eventIndex);
		nameLabel.setText("Name");
		nameResult.setText(e.getName());
		descLabel.setText("Description");
		descResult.setText(e.getDescription());
		userLabel.setText("Location");
		userResult.setText(e.getAddress());
		dateLabel.setVisible(true);
		dateResult.setVisible(true);
		dateResult.setText(e.getDate());
		
		deleteBtn.setText("Cancel Event");
		empSumBtn.setText("Event Summary");
		
		statusLabel.setText("Event Status:");
		if(e.getCancelDate() != null) {
			statusResult.setText("Cancelled");
			deleteBtn.setDisable(true);
		}
		else {
			statusResult.setText("Active");
			deleteBtn.setDisable(false);
		}
		
	}
	
	@FXML
	private void showUserSummary(ActionEvent e) throws SQLException {
		Start.db.connect();
		ResultSet results;
		PreparedStatement stmt;
		String query;
		
		if(volTab.isSelected()) {
		
		/*
		 * Get total time volunteered with company
		*/
			 query = "SELECT TimeStart, TimeEnd " + 
				"	FROM Event NATURAL JOIN EventUsers JOIN Users ON EventUsers.UserId = Users.ID" + 
				"		WHERE Users.ID = ? ";
		
			stmt = Start.db.connection.prepareStatement(query);
			stmt.setInt(1, users.get(userIndex).getId());
			results = stmt.executeQuery();
		}
		else {
			 query = "SELECT TimeStart, TimeEnd, VolFilled "
							+ " FROM Event WHERE CancelDate IS NULL";
			results = Start.db.runQuery(query);
		}
		
		
		int endTime = 0;
		int startTime = 0;
		int totalTime = 0;
		int vols = 0;
		while(results.next()) {
			
			startTime = results.getInt("TimeStart");
			endTime = results.getInt("TimeEnd");
			
			if(evtTab.isSelected()) {
				vols = results.getInt("VolFilled");
				totalTime = totalTime + (vols * (endTime - startTime));
			}
			else {
				totalTime = totalTime + (endTime - startTime);
			}
		}
		Start.db.disconnect();
		
		ttlHoursLabel.setText(totalTime + " hours!");
		
		
		/********************/
		
		Start.db.connect();
		ResultSet donResults;
		
		if(volTab.isSelected()) {
		/*
		 * Get total donated to Events
		 */
			query = "SELECT Sum(Amount) " + 
				"	FROM Event NATURAL JOIN EventDonations JOIN Users ON EventDonations.UserId = Users.ID " + 
				"		WHERE Users.ID = ? AND EventID != -1 ";
			stmt = Start.db.connection.prepareStatement(query);
			stmt.setInt(1, users.get(userIndex).getId());
			donResults = stmt.executeQuery();
		}
		else {
			query = "SELECT Sum(Amount) "
					+ " FROM EventDonations"
					+ "	WHERE EventID = ?";
			PreparedStatement stmt2 = Start.db.connection.prepareStatement(query);
			stmt2.setInt(1, events.get(eventIndex).getId());
			donResults = stmt2.executeQuery();
		}
		
		int donTotal;
		if(donResults.next()) {
			donTotal = donResults.getInt(1);
		}
		else {
			donTotal = 0;
		}
		ttlDnrEventsLabel.setText("$ " + donTotal);
		
		Start.db.disconnect();
		
		/********************/
		
		Start.db.connect();
		ResultSet orgResults;
		
		/*
		 *  Get total donated to organization
		 */
		if(volTab.isSelected()) {
			query = "SELECT Sum(Amount)" + 
				"	FROM Event NATURAL JOIN EventDonations JOIN Users ON EventDonations.UserId = Users.ID " + 
				"	WHERE Users.ID = ? AND EventID = -1";
			stmt = Start.db.connection.prepareStatement(query);
			stmt.setInt(1, users.get(userIndex).getId());
			orgResults = stmt.executeQuery();
		}
		else {
			query = "SELECT Sum(Amount)"
					+ "  FROM EventDonations "
					+ "	 WHERE EventID = -1";
			orgResults = Start.db.runQuery(query);
		}
		
		
		if(orgResults.next()) {
			ttlDnrOrgLabel.setText("$ " + orgResults.getInt(1));
		}
		else {
			ttlDnrOrgLabel.setText("$ 0");
		}
		Start.db.disconnect();
		
		summPane.setVisible(true);	
	}
	
	@FXML
	private void cancel(ActionEvent event) throws SQLException {
		Start.db.connect();
		String query;
		PreparedStatement stmt;
		
		if(volTab.isSelected()) {	
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Delete User Confirmation");
			alert.setHeaderText(null);
			alert.setContentText("Are you sure you want to delete " + users.get(userIndex).getFirstName()
					+ " " + users.get(userIndex).getLastName() + "?");
			
			Optional<ButtonType> result = alert.showAndWait();
			
			if(result.get() == ButtonType.OK) {
				query = "UPDATE Users SET DeactiveDate = ? WHERE ID = ?";
				stmt = Start.db.connection.prepareStatement(query);
			
				DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
				Date date = new Date();
				String deacDate = dateFormat.format(date);
			
				stmt.setString(1, deacDate);
				stmt.setInt(2, users.get(userIndex).getId());
				stmt.executeUpdate();
				
				
				confirmSubmission("User Deleted", "You have deleted " 
														+ users.get(userIndex).getFirstName()
															+ " " + users.get(userIndex).getLastName() + ".");
				users.clear();
				events.clear();
				volsList.getItems().clear();
				eventList.getItems().clear();
				init();
				showUserDetails();
			}
		}
		else {
			
			LocalDateTime ldt = LocalDateTime.now();
			String [] dateInfo = events.get(eventIndex).getDate().split("/");
			if(Integer.parseInt(dateInfo[0]) >= ldt.getMonthValue() &&
			   Integer.parseInt(dateInfo[1]) >= ldt.getDayOfMonth() &&
			   Integer.parseInt(dateInfo[2]) >= ldt.getYear()) {
				
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Cancel Event Confirmation");
				alert.setHeaderText(null);
				alert.setContentText("Are you sure you want to cancel " + events.get(eventIndex).getName() + "?");
				
				Optional<ButtonType> result = alert.showAndWait();
				
				if(result.get() == ButtonType.OK) {
					query = "UPDATE Event SET CancelDate = ? WHERE EventId = ?";
					stmt = Start.db.connection.prepareStatement(query);
					
					DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
					Date date = new Date();
					String cancDate = dateFormat.format(date);
					
					stmt.setString(1, cancDate);
					stmt.setInt(2, events.get(eventIndex).getId());
					stmt.executeUpdate();
					
					confirmSubmission("Event Cancelled",
										"You have cancelled the event " + events.get(eventIndex).getName() + ".");
					
					users.clear();
					events.clear();
					volsList.getItems().clear();
					eventList.getItems().clear();
					init();
					showEventDetails();
					
				}
				
			}
			else {
				denySubmission("Cancellation Error", "Cannot cancel event that has already happened");
			}		
		}
		Start.db.disconnect();
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
