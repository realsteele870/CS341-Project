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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class VolunteerController implements Initializable {
	@FXML
	private Button volunteer;
	private ObservableList<Event> events;
	int index;
	@FXML private ListView<String> eventNameList;
	@FXML private Label nameLbl;
	@FXML private Label descLbl;
	@FXML private Label locLbl;
	@FXML private Label dateLbl;
	@FXML private Label timeLbl;
	@FXML private Label volLbl;
	@FXML private GridPane eventDetailsGrid;
	@FXML private Button homeBtn;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		events = FXCollections.observableArrayList();
		eventDetailsGrid.setVisible(false);
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
		
		while(results.next()) {
			int id = results.getInt("EventId");
			String name = results.getString("Name");
			String desc = results.getString("Description");
			String loc = results.getString("Location");
			String date = results.getString("Date");
			int startTime = results.getInt("TimeStart");
			int endTime = results.getInt("TimeEnd");
			int volNeeded = results.getInt("VolNeeded");
			int volFilled = results.getInt("VolFilled");
			
			Event e = new Event(id,name,desc,date,loc,startTime,endTime,volNeeded,volFilled);
			events.add(e);
			eventNameList.getItems().add(e.getName());
		}
		Start.db.disconnect();
		
		eventNameList.setOnMousePressed((MouseEvent e) ->{
			if(e.getButton().equals(MouseButton.PRIMARY)) {
				index = eventNameList.getSelectionModel().getSelectedIndex();
				showEventDetails();
			}
		});
	}

	private void showEventDetails() {
		if(index < 0) index = 0;
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
	}

	private String convertTimes() {
		String time = "";
		Event e = events.get(index);
		int start = e.getTimeStart();
		int end = e.getTimeEnd();
		if(start > 12) {
			start = start - 12;
			time += start + ":00 pm - ";
		}
		else if(start == 12){
			time += start + ":00 pm - ";
		}
		else if(start == 0) {
			time += "12:00 am - ";
		}
		else {
			time += start + ":00 am - ";
		}
		if(end > 12) {
			end = end - 12;
			time += end + ":00 pm";
		}
		else if(end == 12) {
			time += end + ":00 pm";
		}
		else {
			time += end + ":00 am";
		}
		return time;
	}
	
	private String convertVols(int volNeed, int volReq) {
		return volNeed + " / " + volReq;
	}
	
	public void volunteer(ActionEvent event) throws SQLException {
		Start.db.connect();
		
		String query = "UPDATE Event SET VolFilled = VolFilled + 1 "
				+ "WHERE EventId = ?";
		PreparedStatement stmt = Start.db.connection.prepareStatement(query);
		stmt.setInt(1, events.get(index).getId());
		stmt.executeUpdate();
		Start.db.disconnect();
		
		// repopulate events
		events.clear();
		eventNameList.getItems().clear();
		initEvents();
		showEventDetails();
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
