package application;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import scene3d.Earth;

import model.Model;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class App implements Initializable
{
	@FXML
	private ComboBox combobox;

	@FXML
	private Slider slider;
	
	@FXML
	private Pane pane3D;

	@FXML
	private HBox hbox;

	@FXML
	public void initialize(URL location, ResourceBundle resources)
	{
		slider.setShowTickLabels(true);
		slider.setShowTickMarks(true);
		slider.setMajorTickUnit(20);
		slider.setMinorTickCount(1);
		
		pane3D.setPrefWidth(600);
		pane3D.setPrefHeight(600);

		Menu.combobox = combobox;
		Menu.slider = slider;

		Earth.init(pane3D);
	}
}
