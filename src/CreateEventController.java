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
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateEventController implements Initializable {
	@FXML private TextField nameField;
	@FXML private TextArea descArea;
	@FXML private DatePicker dateField;
	@FXML private TextField timeStrtField;
	@FXML private TextField timeEndField;
	@FXML private TextField volunteersField;
	@FXML private Button createEventBtn;
	@FXML private Button viewEventBtn;
	@FXML private Button volunteerBtn;
	@FXML private Button homeBtn;
	@FXML private RadioButton startAmRBtn;
	@FXML private RadioButton startPmRBtn;
	@FXML private RadioButton endAmRBtn;
	@FXML private RadioButton endPmRBtn;
	
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
	private void createEvent(ActionEvent event) {
		String name = nameField.getText();
		String desc = descArea.getText();
		String date = dateField.getEditor().getText();
		String timeStart = timeStrtField.getText();
		String timeEnd = timeEndField.getText();
		String vols = volunteersField.getText();
		
	}
	
	public int toMilitaryTime(String time) {
		
		
		return 0;
	}
	
	public void switchRadioStart(ActionEvent event) {
		RadioButton rb = (RadioButton) event.getSource();
		if(rb.equals(startAmRBtn)) {
			if(startAmRBtn.isSelected() && startPmRBtn.isSelected()) {
				startPmRBtn.setSelected(false);
			}
		}
		else {
			if(startAmRBtn.isSelected() && startPmRBtn.isSelected()) {
				startAmRBtn.setSelected(false);
			}
		}
	}
	
	public void switchRadioEnd(ActionEvent event) {
		RadioButton rb = (RadioButton) event.getSource();
		if(rb.equals(endAmRBtn)) {
			if(endAmRBtn.isSelected() && endPmRBtn.isSelected()) {
				endPmRBtn.setSelected(false);
			}
		}
		else {
			if(endAmRBtn.isSelected() && endPmRBtn.isSelected()) {
				endAmRBtn.setSelected(false);
			}
		}
	}
}
