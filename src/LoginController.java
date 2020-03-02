import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class LoginController {
	private Start startApp;

	LoginController() {

	}

	@FXML
	public void handleLogin() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Start.class.getResource("Home_Final.fxml"));
		BorderPane personOverview = (BorderPane) loader.load();
		Scene scene = new Scene(personOverview);
		Stage loginPage = new Stage();
		loginPage.setTitle("Login");
		loginPage.setMaximized(true);
		loginPage.setScene(scene);
		loginPage.show();

	}

}
