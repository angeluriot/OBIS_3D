package application;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import model.Model;
import scene3d.Earth;
import scene3d.Legend;

import java.net.URL;
import java.util.ResourceBundle;

public class App implements Initializable
{
	@FXML
	private ComboBox combobox;

	@FXML
	private DatePicker start_date_picker;

	@FXML
	private DatePicker end_date_picker;

	@FXML
	private Slider slider;

	@FXML
	private Button play;

	@FXML
	private Button pause;

	@FXML
	private Button stop;

	@FXML
	private Pane pane3D;

	@FXML
	public HBox hbox;

	@FXML
	private Label max;

	@FXML
	private Label min;

	@FXML
	private AnchorPane anchor_pane;

	@FXML
	public void initialize(URL location, ResourceBundle resources)
	{
		slider.setShowTickLabels(true);
		slider.setShowTickMarks(true);
		slider.setMajorTickUnit(20);
		slider.setMinorTickCount(1);

		Model.init_collection();

		Legend.min = min;
		Legend.max = max;

		Earth.init(pane3D, anchor_pane, hbox);

		Menu.combobox = combobox;
		Menu.start_date_picker = start_date_picker;
		Menu.end_date_picker = end_date_picker;
		Menu.slider = slider;
		Menu.play = play;
		Menu.pause = pause;
		Menu.stop = stop;
		Menu.init();
	}
}
