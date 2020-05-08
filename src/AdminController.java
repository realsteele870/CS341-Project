import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
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
					System.out.println("Here");
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
		
		/*
		 * Get total time volunteered with company
		*/
		String query = "SELECT TimeStart, TimeEnd " + 
				"	FROM Event NATURAL JOIN EventUsers JOIN Users ON EventUsers.UserId = Users.ID" + 
				"		WHERE Users.ID = ? ";
		PreparedStatement stmt = Start.db.connection.prepareStatement(query);
		stmt.setInt(1, users.get(userIndex).getId());
		ResultSet results = stmt.executeQuery();
		
		int totalEndTime = 0;
		int totalStartTime = 0;
		while(results.next()) {
			
			totalStartTime = totalStartTime + results.getInt("TimeStart");
			totalEndTime = totalEndTime + results.getInt("TimeEnd");
		}
		Start.db.disconnect();
		
		int totalVolHours = totalEndTime - totalStartTime;
		ttlHoursLabel.setText(totalVolHours + " hours!");
		
		/********************/
		
		Start.db.connect();
		
		/*
		 * Get total donated to Events
		 */
		query = "SELECT Sum(Amount) " + 
				"	FROM Event NATURAL JOIN EventDonations JOIN Users ON EventDonations.UserId = Users.ID " + 
				"		WHERE Users.ID = ? AND EventID != -1 ";
		stmt = Start.db.connection.prepareStatement(query);
		stmt.setInt(1, users.get(userIndex).getId());
		ResultSet donResults = stmt.executeQuery();
		
		Start.db.disconnect();
		
		int donTotal = donResults.getInt(1);	
		ttlDnrEventsLabel.setText("$ " + donTotal);
		
		/********************/
		
		Start.db.connect();
		
		/*
		 *  Get total donated to organization
		 */
		
		query = "SELECT Sum(Amount)" + 
				"	FROM Event NATURAL JOIN EventDonations JOIN Users ON EventDonations.UserId = Users.ID " + 
				"	WHERE Users.ID = ? AND Event.Id = -1";
		stmt = Start.db.connection.prepareStatement(query);
		stmt.setInt(1, users.get(userIndex).getId());
		
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

}
