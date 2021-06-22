package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Slider;
import javafx.util.Duration;
import model.Model;
import org.json.JSONException;
import scene3d.Earth;

public class Menu
{
	public static ComboBox combobox;
	public static DatePicker start_date_picker;
	public static DatePicker end_date_picker;
	public static Slider slider;
	public static Button play;
	public static Button pause;
	public static Button stop;
	private static int start;
	public static boolean playing = false;
	private static boolean http_mode = false;
	private static boolean is_evolution_loaded = false;

	/**
	 * Initialise le menu en appelant toutes les autres méthodes de la classe
	 */
	public static void init()
	{
		selection();
		dates();
		evolution();
	}

	/**
	 * Gère les évènements liés à la ComboBox (entrée du nom de l'espèce)
	 */
	public static void selection()
	{
		combobox.setOnAction(event ->
		{
			try
			{
				if (combobox.getValue() != null)
				{
					http_mode = true;
					is_evolution_loaded = false;
					Model.set_collection(combobox.getValue().toString());
					start_date_picker.setValue(null);
					end_date_picker.setValue(null);
					Earth.update(-1);
				}
			}

			catch (JSONException e)
			{
				if (combobox.getValue().toString().equals(""))
				{
					http_mode = false;
					Model.init_collection();
					Earth.update(-1);
				}

				else
				{
					combobox.setItems(FXCollections.observableList(Model.get_species(combobox.getValue().toString())));
					combobox.show();
				}
			}
		});
	}

	/**
	 * Gère les évènements liés aux DatePickers (entrée des intervalles de temps)
	 */
	public static void dates()
	{
		start_date_picker.setOnAction(event ->
		{
			if (start_date_picker.getValue() != null && end_date_picker.getValue() != null)
			{
				Model.set_date(start_date_picker.getValue().toString(), end_date_picker.getValue().toString());
				Earth.update(-1);
			}
		});

		end_date_picker.setOnAction(event ->
		{
			if (start_date_picker.getValue() != null && end_date_picker.getValue() != null)
			{
				Model.set_date(start_date_picker.getValue().toString(), end_date_picker.getValue().toString());
				Earth.update(-1);
			}

			else if (start_date_picker.getValue() == null && end_date_picker.getValue() == null)
			{
				if (combobox.getValue().toString().equals(""))
					Model.init_collection();

				else
					Model.set_collection(combobox.getValue().toString());

				Earth.update(-1);
			}
		});
	}

	/**
	 * Gère les évènements liés au Slider, et au Buttons play, pause et stop (évolution sur plusieurs intervalles de
	 * temps
	 */
	public static void evolution()
	{
		// Animation du Slider et mise à jour de Earth à chaque itération
		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event ->
		{
			if (slider.getValue() != 2015)
			{
				if (playing)
					slider.setValue(slider.getValue() + 5);

				else
					playing = true;

				Earth.update((int)slider.getValue());
			}
		}));

		// Remet à zéro l'évolution une fois terminée
		timeline.setOnFinished((finish) ->
		{
			slider.setValue(start);
			Earth.update(-1);
			playing = false;
			slider.setDisable(false);
		});

		// Bouton play
		play.setOnMouseClicked(event ->
		{
			if (!is_evolution_loaded)
			{
				if (http_mode)
					Model.set_evolution(combobox.getValue().toString());
				else
					Model.init_evolution();

				is_evolution_loaded = true;
			}

			if (!playing)
				start = (int)slider.getValue();

			slider.setDisable(true);
			slider.setOpacity(1);
			timeline.setCycleCount((2020 - (int)slider.getValue()) / 5 + 1);
			timeline.play();
		});

		// Bouton pause
		pause.setOnMouseClicked(event ->
		{
			timeline.pause();
		});

		// Bouton stop
		stop.setOnMouseClicked(event ->
		{
			timeline.stop();
			slider.setValue(start);
			Earth.update(-1);
			playing = false;
			slider.setDisable(false);
		});

		// Slider
		slider.valueProperty().addListener(new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue)
			{
				if (!playing)
				{
					if (newValue.intValue() % 5 != 0)
						slider.setValue(Math.round(newValue.floatValue() / 5f) * 5);

					if (newValue.floatValue() != newValue.intValue())
						slider.setValue(newValue.intValue());
				}
			}
		});
	}
}
