package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import model.Model;

public class Main extends Application
{
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		/*
		Parent root = FXMLLoader.load(getClass().getResource(""));
		primaryStage.setTitle("Hello World");
		primaryStage.setScene(new Scene(root, 300, 275));
		primaryStage.show();
		 */
	}
	
	public static void main(String[] args)
	{
		Model model = new Model();

		int test = model.get_local_occurrence(-79., 33.);
		System.out.println(test);

		int test2 = model.get_occurrence(-79., 33., 3, "Delphinidae");
		System.out.println(test2);

		launch(args);
	}
}
