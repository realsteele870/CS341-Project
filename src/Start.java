import java.io.IOException;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Start extends Application {
	private Stage loginPage;

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

	public void handleLogin() throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("Home_Final.fxml"));
		Scene scene = new Scene(root);
		Stage homePage;
		homePage = new Stage();
		homePage.setTitle("Home");
		homePage.setMaximized(true);
		homePage.setScene(scene);
		homePage.showAndWait();
		// loginPage.close();

	}

}
