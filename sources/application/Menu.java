package application;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import model.Model;
import org.json.JSONException;
import scene3d.Earth;

import java.util.ArrayList;

public class Menu
{
	public static ComboBox combobox;
	public static Slider slider;

	public static void selection(Scene scene)
	{
		combobox.setOnAction(event ->
		{
			try
			{
				if (combobox.getValue() != null)
				{
					Model.set_collection(combobox.getValue().toString());
					Earth.update();
				}
			}

			catch (JSONException e)
			{
				if (combobox.getValue().toString().equals(""))
				{
					Model.init_collection();
					Earth.update();
				}

				else
				{
					combobox.setItems(FXCollections.observableList(Model.get_species(combobox.getValue().toString())));
					combobox.show();
				}
			}
		});

	}
}
