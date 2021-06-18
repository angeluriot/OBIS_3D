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
import test.SimpleTest;
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
		stage.setMinWidth(500);
		stage.setMinHeight(400);
		Menu.selection(stage.getScene());
		stage.show();
	}

	public static void main(String[] args)
	{
		Model.init_collection();
		/*
		int test = Model.get_local_occurrence(33., -79.);
		System.out.println(test);

		Model.set_collection("Selachii");

		int test2_bis = Model.get_local_occurrence(-22., 114.);
		System.out.println(test2_bis);
		System.out.println(Model.get_max_occurrence());

		int test2 = Model.get_occurrence(-22., 114., 3, "Selachii");
		System.out.println(test2);

		Model.set_date("2015-06-17", "2021-06-17");
		int test3_bis = Model.get_local_occurrence(33., -79.);
		System.out.println(test3_bis);
		System.out.println("Max : " + Model.get_max_occurrence());

		int test3 = Model.get_occurrence(33., -79., 3, "Selachii", "2015-06-17",
				"2021-06-17");
		System.out.println(test3);

		ArrayList<Integer> test4 = Model.get_occurrence(33., -79., 3, "Selachii",
				"2015-07-17", "0001-00-00", 6);
		System.out.println(test4.get(0));
		System.out.println(test4.get(1));
		System.out.println(test4.get(2));
		System.out.println(test4.get(3));
		System.out.println(test4.get(4));
		System.out.println(test4.get(5));

		ArrayList<Observation> test5 = Model.get_observation("spd");
		System.out.println(test5.get(0));

		ArrayList<String> test6 = Model.get_species("ac");
		System.out.println(test6.get(0));
		System.out.println(test6.get(1));
		System.out.println(test6.get(2));
		System.out.println(test6.get(3));
		System.out.println(test6.get(4));

		 */

		launch(args);
	}
}
