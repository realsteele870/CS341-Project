import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class DashboardController implements Initializable {

	private ObservableList<Event> events;
	@FXML
	private Button homeBtn;
	@FXML
	private ListView<String> eventNameList;
	int index;
	@FXML
	private Label nameLbl;
	@FXML
	private Label descLbl;
	@FXML
	private Label locLbl;
	@FXML
	private Label dateLbl;
	@FXML
	private Label donationLbl;
	private ObservableList<EventUsers> eventsUsers;
	private ObservableList<EventUsers> donations;

	@Override
	/* Initializes the labels and the events to be shown */
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		nameLbl.setVisible(false);
		descLbl.setVisible(false);
		locLbl.setVisible(false);
		dateLbl.setVisible(false);
		eventsUsers = FXCollections.observableArrayList();
		events = FXCollections.observableArrayList();
		donations = FXCollections.observableArrayList();
		try {
			initEvents();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// returns to home
	@FXML
	private void home(ActionEvent event) throws IOException {
		Parent menuParent = FXMLLoader.load(getClass().getResource("Home_Final.fxml"));
		Scene scene = descLbl.getScene(); // use button to get current scene
		Scene scene2 = new Scene(menuParent, scene.getWidth(), scene.getHeight()); // create new scene with last scenes
																					// dimensions
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(scene2);
		window.setTitle("Home");
		window.show();
	}

	// connects to database to put the event information into the listview
	public void initEvents() throws SQLException {
		Start.db.connect();
		LocalDateTime ldt = LocalDateTime.now();
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
			// String [] dataInfo = e.date.split("/");
			/*
			 * String[] dateInfo = e.date.split("/"); if (Integer.parseInt(dateInfo[0]) >=
			 * ldt.getMonthValue() && Integer.parseInt(dateInfo[1]) >= ldt.getDayOfMonth()
			 * && Integer.parseInt(dateInfo[2]) >= ldt.getYear()) {
			 */

			events.add(e);

			// }
		}
		Start.db.disconnect();

		Start.db.connect();

		query = "SELECT * FROM EventUsers";
		results = Start.db.runQuery(query);

		while (results.next()) {
			int id = results.getInt("ID");
			int userID = results.getInt("UserId");
			int eventID = results.getInt("EventId");

			EventUsers eu = new EventUsers(id, userID, eventID);
			// String [] dataInfo = e.date.split("/");

			eventsUsers.add(eu);
			if (eu.userID == Start.userId) {
				Event e = events.get(eu.EventID);
				eventNameList.getItems().add(e.getName());
			}
		}
		Start.db.disconnect();
		// show event details upon mouse click of event row in table

		Start.db.connect();
		query = "SELECT * FROM EventDonations";
		results = Start.db.runQuery(query);
		int doAmount = 0;
		while (results.next()) {
			int donationId = results.getInt("DonationId");
			int eventId = results.getInt("EventId");
			int userId = results.getInt("UserId");
			int amount = results.getInt("Amount");
			Donations d = new Donations(donationId, eventId, userId, amount);
			if (userId == Start.userId) {
				doAmount = doAmount + amount;
			}
		}
		donationLbl.setText("$" + doAmount);

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

	// displays the event details when clicked on in the listview
	private void showEventDetails() throws SQLException {

		if (index < 0)
			index = 0;
		EventUsers ev = eventsUsers.get(index);
		Event e = events.get(ev.EventID);
		nameLbl.setText(e.getName());
		descLbl.setText(e.getDescription());

		locLbl.setText(e.location);
		dateLbl.setText(e.date);

		nameLbl.setVisible(true);
		descLbl.setVisible(true);
		locLbl.setVisible(true);
		dateLbl.setVisible(true);
	}

}
