package scene3d;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import features.FeatureCollection;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.ListView;
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
	private static HBox hbox;
	private static boolean first_click = true;

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

		meshViews[0].setTranslateX(-1);

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

	public static void handle_click()
	{
		earth.addEventHandler(MouseEvent.ANY, event ->
		{
			if (event.getEventType() == MouseEvent.MOUSE_PRESSED && (event.isAltDown() || event.getButton() == MouseButton.SECONDARY))
			{
				first_click = true;
				anchor_pane.getChildren().remove(list_view);

				PickResult pick_result = event.getPickResult();
				Point3D space_coord = pick_result.getIntersectedPoint();

				Point2D position = coord_3d_to_geo_coord(space_coord);

				ArrayList<Observation> observations = Model.get_observation(Model.gps_to_geohash((float)position.getX(), (float)position.getY(), 3));
				ObservableList<String> list = FXCollections.observableArrayList();

				if (observations.size() > 0)
				{
					for (Observation observation : observations)
						list.add(observation.get_scientific_name());

					list_view = new ListView(list);
					list_view.setFixedCellSize(30);
					list_view.setStyle("-fx-font-size : 11pt");
					list_view.setPrefHeight(Math.min(306, list.size() * 30 + 6));
					list_view.setPrefWidth(300);
					list_view.setTranslateX(event.getSceneX());
					list_view.setTranslateY(event.getSceneY());
					anchor_pane.getChildren().add(list_view);
				}
			}
		});

		hbox.addEventHandler(MouseEvent.ANY, event ->
		{
			if (event.getEventType() == MouseEvent.MOUSE_PRESSED)
			{
				if (!first_click)
					anchor_pane.getChildren().remove(list_view);

				else
					first_click = false;
			}
		});
	}

	public static void update(int year)
	{
		squares.getChildren().clear();
		show_data_squares(squares, year);

		Legend.update(Model.get_max_occurrence());

		camera_manager.force_update();
	}

	public static Point3D geo_coord_to_3d_coord(float lat, float lon, float height)
	{
		float lat_cor = lat + TEXTURE_LAT_OFFSET;
		float lon_cor = lon + TEXTURE_LON_OFFSET;

		return new Point3D(
			-java.lang.Math.sin(java.lang.Math.toRadians(lon_cor)) * java.lang.Math.cos(java.lang.Math.toRadians(lat_cor)),
			-java.lang.Math.sin(java.lang.Math.toRadians(lat_cor)),
			java.lang.Math.cos(java.lang.Math.toRadians(lon_cor)) * java.lang.Math.cos(java.lang.Math.toRadians(lat_cor)));
	}

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

	private static PhongMaterial get_color(float lat, float lon, int number)
	{
		if (number == 0)
			return null;

		final int color_nb = (int)(((float)(number - 1) / (float)(Model.get_max_occurrence() - 1)) * 7);

		return new PhongMaterial(new Color(1, (float)color_nb / 7, 0, 1));
	}

	private static float get_size(float lat, float lon, int number)
	{
		return (float)number / (float)Model.get_max_occurrence();
	}

	private static void add_square(Group parent, Point3D top_left, Point3D top_right, Point3D bottom_right, Point3D bottom_left, PhongMaterial material)
	{
		if (material == null)
			return;

		final TriangleMesh triangle_mesh = new TriangleMesh();

		final float[] points =
		{
			(float)top_right.getX(), (float)top_right.getY(), (float)top_right.getZ(),
			(float)top_left.getX(), (float)top_left.getY(), (float)top_left.getZ(),
			(float)bottom_left.getX(), (float)bottom_left.getY(), (float)bottom_left.getZ(),
			(float)bottom_right.getX(), (float)bottom_right.getY(), (float)bottom_right.getZ(),
		};

		final float[] tex_coords =
		{
			1, 1,
			1, 0,
			0, 1,
			0, 0
		};

		final int[] faces =
		{
			0, 1, 1, 0, 2, 2,
			0, 1, 2, 2, 3, 3
		};

		triangle_mesh.getPoints().setAll(points);
		triangle_mesh.getTexCoords().setAll(tex_coords);
		triangle_mesh.getFaces().setAll(faces);

		final MeshView mesh_view = new MeshView(triangle_mesh);
		mesh_view.setMaterial(material);
		mesh_view.setTranslateX(-1);
		parent.getChildren().addAll(mesh_view);
	}

	public static void test(Group parent, float lat, float lon, int year)
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
		Point3D yDir = new Point3D(0, 1, 0);

		Group group = new Group();
		Affine affine = new Affine();
		affine.append(Math3D.lookAt(from, to, yDir));
		group.getTransforms().setAll(affine);
		group.getChildren().addAll(box);
		group.setTranslateX(-1);

		parent.getChildren().addAll(group);
	}

	public static void displayTown(Group parent, String name, float lat, float lon)
	{
		Sphere sphere = new Sphere(0.01);
		Point3D coords3D = geo_coord_to_3d_coord(lat, lon, 0.f);
		sphere.setTranslateX(coords3D.getX() - 1);
		sphere.setTranslateY(coords3D.getY());
		sphere.setTranslateZ(coords3D.getZ());
		parent.getChildren().add(sphere);
		camera_manager.force_update();
	}

	private static void show_data_squares(Group parent, int year)
	{
		for (float lat = -180; lat < 180; lat += ZONE_SIZE)
			for (float lon = -180; lon < 180; lon += ZONE_SIZE)
				test(parent, lat + ZONE_SIZE / 2, lon + ZONE_SIZE / 2, year);
	}

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
