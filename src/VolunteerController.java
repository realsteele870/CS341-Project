import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;


public class VolunteerController implements Initializable {
	@FXML
	private Button volunteer;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
	}

	@FXML
	private void volunteer(ActionEvent event) throws IOException {
		// get associated event from database and increment volunteers filled
		// shit
	}
}
