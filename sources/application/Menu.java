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

import java.util.ArrayList;

public class Menu {
	public static ComboBox combobox;
	public static Slider slider;
	private static String combobox_value = "";

	public static void selection(Scene scene)
	{
		combobox.setOnKeyReleased(event -> {
			if (event.getCode().equals(KeyCode.BACK_SPACE) && !combobox_value.equals(""))
				combobox_value = combobox_value.substring(0, combobox_value.length() - 1);

			else
				combobox_value += event.getText();

			combobox.setItems(FXCollections.observableList(Model.get_species(combobox_value)));
		});

		combobox.setOnAction(event -> {
			try
			{
				if(combobox.getValue() != null)
				{
					Model.set_collection(combobox.getValue().toString());
				}
				else
				{
					Model.set_collection(combobox_value);
				}
			}
			catch(JSONException e)
			{

			}
			System.out.println(Model.get_local_occurrence(-22, 114));

		});

	}
}
