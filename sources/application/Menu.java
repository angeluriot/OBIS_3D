package application;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
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
	public static DatePicker start_date_picker;
	public static DatePicker end_date_picker;
	public static Slider slider;

	public static void init()
	{
		selection();
		dates();
	}

	public static void selection()
	{
		combobox.setOnAction(event ->
		{
			try
			{
				if (combobox.getValue() != null)
				{
					Model.set_collection(combobox.getValue().toString());
					start_date_picker.setValue(null);
					end_date_picker.setValue(null);
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

	public static void dates()
	{
		start_date_picker.setOnAction(event ->
		{
			if (start_date_picker.getValue() != null && end_date_picker.getValue() != null)
			{
				Model.set_date(start_date_picker.getValue().toString(), end_date_picker.getValue().toString());
				Earth.update();
			}
		});

		end_date_picker.setOnAction(event ->
		{
			if (start_date_picker.getValue() != null && end_date_picker.getValue() != null)
			{
				Model.set_date(start_date_picker.getValue().toString(), end_date_picker.getValue().toString());
				Earth.update();
			}
		});
	}
}
