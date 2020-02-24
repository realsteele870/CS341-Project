import java.io.IOException;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Start extends Application{
	private Stage loginPage;

	public static void main(String[] args) {
		launch(args);

	}
	
	public void start(Stage loginStage) throws IOException, SQLException {
		
		Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
		Scene scene = new Scene(root);
		loginPage = loginStage;
		loginPage.setTitle("Login");
		//loginPage.setMaximized(true);
		loginPage.setScene(scene);
		loginPage.show();	
		
	}
}
