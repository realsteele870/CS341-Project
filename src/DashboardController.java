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
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class DashboardController implements Initializable {
	@FXML
	private Button homeBtn;
	@FXML
	private ListView<String> eventNameList;

	private ObservableList<Event> events;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

		events = FXCollections.observableArrayList();

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
			String[] dateInfo = e.date.split("/");
			if (Integer.parseInt(dateInfo[0]) >= ldt.getMonthValue()
					&& Integer.parseInt(dateInfo[1]) >= ldt.getDayOfMonth()
					&& Integer.parseInt(dateInfo[2]) >= ldt.getYear()) {
				System.out.println(e.date);
				events.add(e);
				eventNameList.getItems().add(e.getName());
			}
		}
		Start.db.disconnect();
		// show event details upon mouse click of event row in table
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

}
