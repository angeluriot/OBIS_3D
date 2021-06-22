package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import scene3d.Earth;

public class Main extends Application
{
	@Override
	public void start(Stage stage) throws Exception
	{
		Parent root = FXMLLoader.load(getClass().getResource("application.fxml"));

		Earth.handle_events(stage);

		stage.setTitle("OBIS 3D");
		stage.setScene(new Scene(root, 1200, 700));
		stage.setMinWidth(500);
		stage.setMinHeight(400);
		stage.show();
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
