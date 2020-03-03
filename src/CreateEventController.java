import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

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

	
}
