package scene3d;

import application.Menu;
import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import features.FeatureCollection;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import model.Model;
import utils.Observation;

import javax.tools.DocumentationTool;
import javax.tools.JavaFileManager;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Earth
{
	private static final float ZONE_SIZE = 360.f / 256.f;
	private static final float TEXTURE_LAT_OFFSET = -0.2f;
	private static final float TEXTURE_LON_OFFSET = 2.8f;
	private static CameraManager camera_manager;
	private static SubScene sub_scene;
	private static Group earth;
	private static Pane pane;
	private static Group squares;
	private static AnchorPane anchor_pane;
	private static ListView list_view;
	private static ListView secondary_list_view;
	private static ArrayList<Observation> observations;
	private static HBox hbox;
	private static boolean first_click = true;

	/**
	 * Initialise la vue 3D du globe
	 * @param pane3D Pane où est affiché la vue
	 * @param anchor_pane3D Pane où est affiché la vue et la légende
	 * @param true_root Racine de la scène
	 */
	public static void init(Pane pane3D, AnchorPane anchor_pane3D, HBox true_root)
	{
		hbox = true_root;
		anchor_pane = anchor_pane3D;
		pane = pane3D;
		Group root = new Group();

		// Load geometry
		ObjModelImporter obj_importer = new ObjModelImporter();

		try
		{
			URL modelURL = new URL(new URL("file:"), "resources/earth/earth.obj");
			obj_importer.read(modelURL);
		}

		catch (ImportException | MalformedURLException e)
		{
			System.out.println(e.getMessage());
			System.exit(1);
		}

		MeshView[] meshViews = obj_importer.getImport();
		earth = new Group(meshViews);
		root.getChildren().add(earth);

		squares = new Group();
		squares.setDisable(true);
		squares.setFocusTraversable(true);
		earth.getChildren().add(squares);

		// Add a camera group
		PerspectiveCamera camera = new PerspectiveCamera(true);
		camera_manager = new CameraManager(camera, pane, root);

		// Add point light
		PointLight light = new PointLight(Color.WHITE);
		light.setTranslateX(-180);
		light.setTranslateY(-90);
		light.setTranslateZ(-120);
		light.getScope().addAll(root);
		root.getChildren().add(light);

		// Add ambient light
		AmbientLight ambientLight = new AmbientLight(Color.WHITE);
		ambientLight.getScope().addAll(root);
		root.getChildren().add(ambientLight);

		// Add sub scene
		sub_scene = new SubScene(root, 600, 600, true, SceneAntialiasing.BALANCED);
		sub_scene.setCamera(camera);
		sub_scene.setFill(new Color(0.15, 0.155, 0.17, 1));
		pane.getChildren().addAll(sub_scene);
		pane.getChildren().add(root);

		// Initial update
		update(-1);
		handle_click();
	}

	/**
	 * Gère les clics droits sur la vue 3D
	 */
	public static void handle_click()
	{
		earth.addEventHandler(MouseEvent.ANY, event ->
		{
			if (event.getEventType() == MouseEvent.MOUSE_PRESSED && (event.isAltDown() || event.getButton() == MouseButton.SECONDARY) && !Menu.playing)
			{
				first_click = true;
				anchor_pane.getChildren().remove(list_view);
				anchor_pane.getChildren().remove(secondary_list_view);

				PickResult pick_result = event.getPickResult();
				Point3D space_coord = pick_result.getIntersectedPoint();

				Point2D position = coord_3d_to_geo_coord(space_coord);

				if (Model.get_local_occurrence((float)position.getX(), (float)position.getY()) > 0)
					click_on_data(event, (float)position.getX(), (float)position.getY());

				else
					click_on_void(event, (float)position.getX(), (float)position.getY());
			}
		});

		hbox.addEventHandler(MouseEvent.ANY, event ->
		{
			if (event.getEventType() == MouseEvent.MOUSE_PRESSED)
			{
				if (!first_click)
				{
					anchor_pane.getChildren().remove(list_view);
					anchor_pane.getChildren().remove(secondary_list_view);
				}

				else
					first_click = false;
			}
		});
	}

	/**
	 * Affiche des listes lorsque le l'utilisateur clique sur une donnée
	 * @param event Evénement de la souris
	 * @param lat Latitude
	 * @param lon Longitude
	 */
	public static void click_on_data(MouseEvent event, float lat, float lon)
	{
		observations = Model.get_observation(Model.gps_to_geohash(lat, lon, 3));
		ObservableList<String> list = FXCollections.observableArrayList();

		if (observations.size() > 0)
		{
			int number = 1;

			for (int i = 0; i < observations.size(); i++)
			{
				number = 1;

				for (int j = 0; j < i; j++)
					if (observations.get(i).get_scientific_name().equals(observations.get(j).get_scientific_name()))
						number += 1;

				list.add(observations.get(i).get_scientific_name() + (number > 1 ? " (" + number + ")" : ""));
			}

			list_view = new ListView(list);
			list_view.setFixedCellSize(30);
			list_view.setStyle("-fx-font-size : 11pt");
			list_view.setPrefHeight(Math.min(306, list.size() * 30 + 6));
			list_view.setPrefWidth(250);
			list_view.setTranslateX(event.getSceneX());
			list_view.setTranslateY(event.getSceneY());
			anchor_pane.getChildren().add(list_view);
			list_view.toFront();

			// Lors du clic sur une observation de la liste
			list_view.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>()
			{
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
				{
					anchor_pane.getChildren().remove(secondary_list_view);

					ObservableList<String> secondary_list = FXCollections.observableArrayList();
					secondary_list.add(observations.get(list_view.getSelectionModel().getSelectedIndex()).get_scientific_name());
					secondary_list.add(observations.get(list_view.getSelectionModel().getSelectedIndex()).get_order());
					secondary_list.add(observations.get(list_view.getSelectionModel().getSelectedIndex()).get_super_class());
					secondary_list.add(observations.get(list_view.getSelectionModel().getSelectedIndex()).get_recorded_by());
					secondary_list.add(observations.get(list_view.getSelectionModel().getSelectedIndex()).get_specie());

					secondary_list_view = new ListView(secondary_list);
					secondary_list_view.setFixedCellSize(30);
					secondary_list_view.setStyle("-fx-font-size : 11pt");
					secondary_list_view.setPrefHeight(Math.min(306, secondary_list.size() * 30 + 6));
					secondary_list_view.setPrefWidth(250);
					secondary_list_view.setTranslateX(list_view.getTranslateX() + list_view.getPrefWidth());
					secondary_list_view.setTranslateY(list_view.getTranslateY() + list_view.getSelectionModel().getSelectedIndex() * 30);
					anchor_pane.getChildren().add(secondary_list_view);
					secondary_list_view.toFront();
				}
			});
		}
	}

	/**
	 * Affiche une liste lorsque le l'utilisateur clique sur le globe
	 * @param event Evénement de la souris
	 * @param lat Latitude
	 * @param lon Longitude
	 */
	public static void click_on_void(MouseEvent event, float lat, float lon)
	{
		ArrayList<String> names = Model.get_species_from_geohash(Model.gps_to_geohash(lat, lon, 3));
		ObservableList<String> list = FXCollections.observableArrayList();

		if (names.size() > 0)
		{
			for (String name : names)
				list.add(name);

			list_view = new ListView(list);
			list_view.setFixedCellSize(30);
			list_view.setStyle("-fx-font-size : 11pt");
			list_view.setPrefHeight(Math.min(306, list.size() * 30 + 6));
			list_view.setPrefWidth(250);
			list_view.setTranslateX(event.getSceneX());
			list_view.setTranslateY(event.getSceneY());
			anchor_pane.getChildren().add(list_view);
			list_view.toFront();

			list_view.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>()
			{
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
				{
					Menu.combobox.setValue(newValue);
					Menu.combobox.fireEvent(new ActionEvent());
				}
			});
		}
	}

	/**
	 * Met à jour la vue 3D
	 * @param year Année montrée
	 */
	public static void update(int year)
	{
		anchor_pane.getChildren().remove(list_view);
		anchor_pane.getChildren().remove(secondary_list_view);

		squares.getChildren().clear();
		show_data_squares(squares, year);

		Legend.update(Model.get_max_occurrence());

		camera_manager.force_update();
	}

	/**
	 * Convertit les coordonnées terrestres en coordonnées 3D
	 * @param lat Latitude
	 * @param lon Longitude
	 * @param height Hauteur
	 * @return Les coordonnées 3D
	 */
	public static Point3D geo_coord_to_3d_coord(float lat, float lon, float height)
	{
		float lat_cor = lat + TEXTURE_LAT_OFFSET;
		float lon_cor = lon + TEXTURE_LON_OFFSET;

		return new Point3D(
			-java.lang.Math.sin(java.lang.Math.toRadians(lon_cor)) * java.lang.Math.cos(java.lang.Math.toRadians(lat_cor)),
			-java.lang.Math.sin(java.lang.Math.toRadians(lat_cor)),
			java.lang.Math.cos(java.lang.Math.toRadians(lon_cor)) * java.lang.Math.cos(java.lang.Math.toRadians(lat_cor)));
	}

	/**
	 * Convertit les coordonnées 3D en coordonnées terrestres
	 * @param point Point 3D
	 * @return Les coordonnées terrestres
	 */
	public static Point2D coord_3d_to_geo_coord(Point3D point)
	{
		double latitude = (float)(Math.asin(-point.getY()) * (180 / Math.PI) - TEXTURE_LAT_OFFSET);
		double longitude = -(Math.atan2(-point.getZ(), -point.getX())) - Math.PI / 2;

		if (longitude < -Math.PI)
			longitude += Math.PI * 2;

		longitude *= -180 / Math.PI;
		longitude -= TEXTURE_LON_OFFSET;

		return new Point2D(latitude, longitude);
	}

	/**
	 * Donne la couleur de la barre de donnée
	 * @param lat Latitude
	 * @param lon Longitude
	 * @param number Nombre d'occurrences
	 * @return La couleur correspondante
	 */
	private static PhongMaterial get_color(float lat, float lon, int number)
	{
		if (number == 0)
			return null;

		final int color_nb = (int)(((float)(number - 1) / (float)(Model.get_max_occurrence())) * 7);

		return new PhongMaterial(new Color(1, Math.max(0f, Math.min(1f, (float)color_nb / 7)), 0, 1));
	}

	/**
	 * Donne la taille de la barre de donnée
	 * @param lat Latitude
	 * @param lon Longitude
	 * @param number Nombre d'occurrences
	 * @return La taille correspondante
	 */
	private static float get_size(float lat, float lon, int number)
	{
		if (Model.get_max_occurrence() == 0)
			return 0f;

		return (float)number / (float)Model.get_max_occurrence();
	}

	/**
	 * Ajoute une barre de donnée sur le globe
	 * @param parent Noeud JavaFX parent
	 * @param lat Latitude
	 * @param lon Longitude
	 * @param year Année montrée
	 */
	public static void add_square(Group parent, float lat, float lon, int year)
	{
		final int number;

		if (year == -1)
			number = Model.get_local_occurrence(lat, lon);

		else
			number = Model.get_evolution_occurrence(lat, lon, year);

		PhongMaterial material = get_color(lat, lon, number);

		if (material == null)
			return;

		Point3D from = geo_coord_to_3d_coord(lat, lon, 1.0f);
		Box box = new Box(0.01f, 0.01f, get_size(lat, lon, number));
		box.setMaterial(material);

		Point3D to = Point3D.ZERO;
		Point3D y_dir = new Point3D(0, 1, 0);

		Group group = new Group();
		Affine affine = new Affine();
		affine.append(Math3D.look_at(from, to, y_dir));
		group.getTransforms().setAll(affine);
		group.getChildren().addAll(box);
		parent.getChildren().addAll(group);
	}

	/**
	 * Affiche les barres de données sur le globe
	 * @param parent Noeud JavaFX parent
	 * @param year Année montrée
	 */
	private static void show_data_squares(Group parent, int year)
	{
		for (float lat = -180; lat < 180; lat += ZONE_SIZE)
			for (float lon = -180; lon < 180; lon += ZONE_SIZE)
				add_square(parent, lat + ZONE_SIZE / 2, lon + ZONE_SIZE / 2, year);
	}

	/**
	 * Change la taille de la vue 3D en fonction de la taille de la fenêtre
	 * @param stage
	 */
	public static void handle_events(Stage stage)
	{
		sub_scene.setWidth(stage.getWidth() - 315);
		sub_scene.setHeight(stage.getHeight() - 30);

		pane.setPrefWidth(stage.getWidth() - 315);
		pane.setPrefWidth(stage.getHeight() - 30);

		stage.widthProperty().addListener((obs, oldVal, newVal) ->
		{
			sub_scene.setWidth(newVal.doubleValue() - 315);
			pane.setPrefWidth(newVal.doubleValue() - 315);
		});

		stage.heightProperty().addListener((obs, oldVal, newVal) ->
		{
			sub_scene.setHeight(newVal.doubleValue() - 30);
			pane.setPrefHeight(newVal.doubleValue() - 30);
		});
	}
}
