package scene3d;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

import java.net.MalformedURLException;
import java.net.URL;

public class Earth
{
	private static final float TEXTURE_LAT_OFFSET = -0.2f;
	private static final float TEXTURE_LON_OFFSET = 2.8f;
	private static SubScene sub_scene;
	private static Pane pane;
	
	public static void init(Pane pane3D)
	{
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
		Group earth = new Group(meshViews);
		root.getChildren().add(earth);
		
		meshViews[0].setTranslateX(-1);
		meshViews[0].setScaleX(0.99);
		meshViews[0].setScaleY(0.99);
		meshViews[0].setScaleZ(0.99);
		
		// Draw city on the earth
		display_town(earth, "earth", 43.435555f, 5.213611f);
		
		// Add a camera group
		PerspectiveCamera camera = new PerspectiveCamera(true);
		new CameraManager(camera, pane, root);
		
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
		
		sub_scene = new SubScene(root, 600, 600, true, SceneAntialiasing.BALANCED);
		sub_scene.setCamera(camera);
		sub_scene.setFill(Color.GRAY);
		pane.getChildren().addAll(sub_scene);
		pane.getChildren().add(root);
		
		// Add a quadrilateral
		add_quadrilateral_map(earth);
	}
	
	public static Cylinder create_line(Point3D origin, Point3D target)
	{
		Point3D y_axis = new Point3D(0, 1, 0);
		Point3D diff = target.subtract(origin);
		double height = diff.magnitude();
		
		Point3D mid = target.midpoint(origin);
		Translate move_to_midpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());
		
		Point3D axis_of_rotation = diff.crossProduct(y_axis);
		double angle = Math.acos(diff.normalize().dotProduct(y_axis));
		Rotate rotate_around_center = new Rotate(-Math.toDegrees(angle), axis_of_rotation);
		
		Cylinder line = new Cylinder(0.01f, height);
		
		line.getTransforms().addAll(move_to_midpoint, rotate_around_center);
		
		return line;
	}
	
	public static Point3D geo_coord_to_3d_coord(float lat, float lon, float height)
	{
		float lat_cor = lat + TEXTURE_LAT_OFFSET;
		float lon_cor = lon + TEXTURE_LON_OFFSET;
		
		return new Point3D(
			-java.lang.Math.sin(java.lang.Math.toRadians(lon_cor)) * java.lang.Math.cos(java.lang.Math.toRadians(lat_cor)),
			-java.lang.Math.sin(java.lang.Math.toRadians(lat_cor)) - height * java.lang.Math.sin(java.lang.Math.toRadians(lat_cor)) / 50,
			java.lang.Math.cos(java.lang.Math.toRadians(lon_cor)) * java.lang.Math.cos(java.lang.Math.toRadians(lat_cor)));
	}
	
	public static void display_town(Group parent, String name, float lat, float lon)
	{
		Sphere sphere = new Sphere(0.01);
		Point3D coords3D = geo_coord_to_3d_coord(lat, lon, 0.f);
		
		sphere.setTranslateX(coords3D.getX() - 1);
		sphere.setTranslateY(coords3D.getY());
		sphere.setTranslateZ(coords3D.getZ());
		parent.getChildren().add(sphere);
	}
	
	private static void add_quadrilateral(Group parent, Point3D top_right, Point3D bottom_right, Point3D bottom_left, Point3D top_left, PhongMaterial material)
	{
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
	
	private static void add_quadrilateral_map(Group earth)
	{
		PhongMaterial phong_red = new PhongMaterial(Color.RED);
		PhongMaterial phong_green = new PhongMaterial(Color.GREEN);
		
		for (float i = 0; i < 360; i += 4)
			for (float j = 0; j < 360; j += 4)
			{
				add_quadrilateral(earth, geo_coord_to_3d_coord(i + 2 - 180, j, 1),
						geo_coord_to_3d_coord(i + 2 - 180, j + 2, 1),
						geo_coord_to_3d_coord(i - 180, j + 2, 1),
						geo_coord_to_3d_coord(i - 180, j, 1),
						phong_red);
				
				add_quadrilateral(earth, geo_coord_to_3d_coord(i + 2 + 2 - 180, j, 1),
						geo_coord_to_3d_coord(i + 2 + 2 - 180, j + 2, 1),
						geo_coord_to_3d_coord(i + 2 - 180, j + 2, 1),
						geo_coord_to_3d_coord(i + 2 - 180, j, 1),
						phong_green);
				
				add_quadrilateral(earth, geo_coord_to_3d_coord(i + 2 - 180, j + 2, 1),
						geo_coord_to_3d_coord(i + 2 - 180, j + 2 + 2, 1),
						geo_coord_to_3d_coord(i - 180, j + 2 + 2, 1),
						geo_coord_to_3d_coord(i - 180, j + 2, 1),
						phong_green);
				
				add_quadrilateral(earth, geo_coord_to_3d_coord(i + 2 + 2 - 180, j + 2, 1),
						geo_coord_to_3d_coord(i + 2 + 2 - 180, j + 2 + 2, 1),
						geo_coord_to_3d_coord(i + 2 - 180, j + 2 + 2, 1),
						geo_coord_to_3d_coord(i + 2 - 180, j + 2, 1),
						phong_red);
			}
	}
	
	public static void handle_events(Stage stage)
	{
		sub_scene.setWidth(stage.getWidth() - 315);
		sub_scene.setHeight(stage.getHeight());
		
		pane.setPrefWidth(stage.getWidth() - 315);
		pane.setPrefWidth(stage.getHeight());
		
		stage.widthProperty().addListener((obs, oldVal, newVal) ->
		{
			sub_scene.setWidth(newVal.doubleValue() - 315);
			pane.setPrefWidth(newVal.doubleValue() - 315);
		});
		
		stage.heightProperty().addListener((obs, oldVal, newVal) ->
		{
			sub_scene.setHeight(newVal.doubleValue());
			pane.setPrefHeight(newVal.doubleValue());
		});
	}
}