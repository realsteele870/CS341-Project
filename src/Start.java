import java.io.IOException;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Start extends Application {
	private Stage loginPage;
	@FXML private Button loginBtn;

	public static void main(String[] args) {
		Database db = new Database();
		launch(args);

	}

	public void start(Stage loginStage) throws IOException, SQLException {

		Parent root = FXMLLoader.load(getClass().getResource("Login_FINAL.fxml"));
		Scene scene = new Scene(root);
		loginPage = loginStage;
		loginPage.setTitle("Login");
		loginPage.setMaximized(true);
		loginPage.setScene(scene);
		loginPage.show();

	}

	/*public void handleLogin() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("Home_Final.fxml"));
		Scene scene = new Scene(root);
		Stage homePage;
		homePage = new Stage();
		homePage.setTitle("Home");
		homePage.setMaximized(true);
		homePage.setScene(scene);
		homePage.showAndWait();
		// loginPage.close();

	}*/
	@FXML
	public void login(ActionEvent event) throws IOException, SQLException {
	
		Parent menuParent = FXMLLoader.load(getClass().getResource("Home_Final.fxml"));
		Scene scene = loginBtn.getScene(); // use button to get current scene
		Scene scene2 = new Scene(menuParent,scene.getWidth(),scene.getHeight()); // create new scene with last scenes dimensions
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(scene2);
		window.setTitle("Home");
		window.show();
	}

}
