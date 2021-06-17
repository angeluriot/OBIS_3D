package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.stage.Stage;

import model.Model;
import scene3d.Earth;

import java.util.ArrayList;

public class Main extends Application
{
	@Override
	public void start(Stage stage) throws Exception
	{
		Parent root = FXMLLoader.load(getClass().getResource("application.fxml"));
		
		Earth.handle_events(stage);
		
		stage.setTitle("OBIS 3D");
		stage.setScene(new Scene(root, 1200, 700));
		stage.setMinWidth(400);
		stage.setMinHeight(400);
		stage.show();
	}
	
	public static void main(String[] args)
	{
		Model model = new Model();

		int test = model.get_local_occurrence(33., -79.);
		System.out.println(test);

		int test2 = model.get_occurrence(33., -79., 3, "Delphinidae");
		System.out.println(test2);

		int test3 = model.get_occurrence(33., -79., 3, "Delphinidae", "2015-06-17",
				"2021-06-17");
		System.out.println(test3);

		ArrayList<Integer> test4 = model.get_occurrence(33., -79., 3, "Delphinidae",
				"2015-07-17", "0000-00-05", 7);
		System.out.println(test4.get(0));

		launch(args);
	}
}
