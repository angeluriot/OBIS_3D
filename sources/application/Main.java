package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.stage.Stage;

import model.Model;
import scene3d.Earth;

public class Main extends Application
{
	@Override
	public void start(Stage stage) throws Exception
	{
		Parent root = FXMLLoader.load(getClass().getResource("application.fxml"));
		
		Scene scene = new Scene(root, 1200, 700);
		Earth.init();
		
		stage.setTitle("OBIS 3D");
		stage.setScene(scene);
		stage.setMinWidth(400);
		stage.setMinHeight(400);
		stage.show();
	}
	
	public static void main(String[] args)
	{
		launch(args);
	}
}
