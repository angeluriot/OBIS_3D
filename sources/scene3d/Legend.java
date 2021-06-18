package scene3d;

import javafx.scene.control.Label;

public class Legend
{
	public static Label min;
	public static Label max;

	public static void update(int new_max)
	{
		max.setText(Integer.toString(new_max));
		min.setText(Integer.toString(Math.min(1, new_max)));
	}
}
