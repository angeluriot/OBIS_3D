package scene3d;

import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Rotate;

public class CameraManager
{
	private static final double CAMERA_MIN_DISTANCE = -1.2;
	private static final double CAMERA_INITIAL_DISTANCE = -5;
	private static final double CAMERA_INITIAL_X_ANGLE = 0.0;
	private static final double CAMERA_INITIAL_Y_ANGLE = 0.0;
	private static final double CAMERA_NEAR_CLIP = 0.1;
	private static final double CAMERA_FAR_CLIP = 10000.0;
	private static final double CONTROL_MULTIPLIER = 0.1;
	private static final double SHIFT_MULTIPLIER = 10.0;
	private static final double MOUSE_SPEED = 0.01;
	private static final double ROTATION_SPEED = 0.2;
	private static final double TRACK_SPEED = 0.6;

	private final Group cameraXform = new Group();
	private final Group cameraXform2 = new Group();
	private Rotate rx = new Rotate();
	private Rotate ry = new Rotate();
	private double mousePosX;
	private double mousePosY;
	private double mouseOldX;
	private double mouseOldY;
	private double mouseDeltaX;
	private double mouseDeltaY;

	private Camera camera;

	public CameraManager(Camera cam, Node mainRoot, Group root)
	{
		camera = cam;

		root.getChildren().add(cameraXform);
		cameraXform.getChildren().add(cameraXform2);
		cameraXform2.getChildren().add(camera);

		rx.setAxis(Rotate.X_AXIS);
		ry.setAxis(Rotate.Y_AXIS);
		cameraXform.getTransforms().addAll(ry, rx);

		camera.setNearClip(CAMERA_NEAR_CLIP);
		camera.setFarClip(CAMERA_FAR_CLIP);
		camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
		ry.setAngle(CAMERA_INITIAL_Y_ANGLE);
		rx.setAngle(CAMERA_INITIAL_X_ANGLE);

		// Add mouse handler
		handleMouse(mainRoot, root);
	}

	public void force_update()
	{
		camera.setTranslateZ(camera.getTranslateZ() - 0.001);
		camera.setTranslateZ(camera.getTranslateZ() + 0.001);
	}

	private void handleMouse(Node mainRoot, final Node root)
	{
		mainRoot.setOnMousePressed(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent me)
			{
				mousePosX = me.getSceneX();
				mousePosY = me.getSceneY();
				mouseOldX = me.getSceneX();
				mouseOldY = me.getSceneY();

				// Set focus on the mainRoot to be able to detect key press
				mainRoot.requestFocus();
			}
		});

		mainRoot.setOnMouseDragged(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent me)
			{
				mouseOldX = mousePosX;
				mouseOldY = mousePosY;
				mousePosX = me.getSceneX();
				mousePosY = me.getSceneY();
				mouseDeltaX = (mousePosX - mouseOldX);
				mouseDeltaY = (mousePosY - mouseOldY);

				double modifier = 1.0;

				if (me.isControlDown())
					modifier = CONTROL_MULTIPLIER;

				if (me.isShiftDown())
					modifier = SHIFT_MULTIPLIER;

				if (me.isPrimaryButtonDown())
				{
					ry.setAngle(ry.getAngle() + mouseDeltaX * modifier * ROTATION_SPEED);
					rx.setAngle(rx.getAngle() - mouseDeltaY * modifier * ROTATION_SPEED);
				}

				else if (me.isSecondaryButtonDown())
				{
					cameraXform2.setTranslateX(cameraXform2.getTranslateX() - mouseDeltaX * MOUSE_SPEED * modifier * TRACK_SPEED);
					cameraXform2.setTranslateY(cameraXform2.getTranslateY() - mouseDeltaY * MOUSE_SPEED * modifier * TRACK_SPEED);
				}
			}
		});

		mainRoot.setOnScroll(new EventHandler<ScrollEvent>()
		{
			@Override
			public void handle(ScrollEvent event)
			{
				double modifier = 1.0;

				if (event.isControlDown())
					modifier = CONTROL_MULTIPLIER;

				if (event.isShiftDown())
					modifier = SHIFT_MULTIPLIER;

				double z = camera.getTranslateZ();
				double newZ = z + event.getDeltaY() * MOUSE_SPEED * modifier;

				if (newZ > CAMERA_MIN_DISTANCE)
				    newZ = CAMERA_MIN_DISTANCE;

				camera.setTranslateZ(newZ);
			}
		});
	}
}
