package application;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import scene3d.Earth;

public class App
{
	@FXML
	private Slider slider;
	
	@FXML
	private Pane scene3d;
	
	@FXML
	private Group scene3d_group;
	
	@FXML
	private SubScene sub_scene;
	
	@FXML
	public void initialize()
	{
		slider.setShowTickLabels(true);
		slider.setShowTickMarks(true);
		slider.setMajorTickUnit(20);
		slider.setMinorTickCount(1);
		
		Earth.pane = scene3d;
		Earth.root = scene3d_group;
		Earth.sub_scene = sub_scene;
	}
}
