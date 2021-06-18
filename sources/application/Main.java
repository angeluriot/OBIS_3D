package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import model.Model;
import scene3d.Earth;
import utils.Observation;

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

		int test = Model.get_local_occurrence(33., -79.);
		System.out.println(test);

		System.out.println(Model.get_min_occurrence());
		System.out.println(Model.get_max_occurrence());

		int test2 = Model.get_occurrence(33., -79., 3, "Selachii");
		System.out.println(test2);

		int test3 = Model.get_occurrence(33., -79., 3, "Selachii", "2015-06-17",
				"2021-06-17");
		System.out.println(test3);

		ArrayList<Integer> test4 = Model.get_occurrence(33., -79., 3, "Selachii",
				"2015-07-17", "0000-00-05", 7);
		System.out.println(test4.get(0));

		ArrayList<Observation> test5 = Model.get_observation("spd", "Selachii");
		System.out.println(test5.get(0));
		
		launch(args);
	}
}
