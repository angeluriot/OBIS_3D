package scene3d;

import javafx.scene.control.Label;

public class Legend
{
	public static Label max;
	
	public static void update_max(int value)
	{
		max.setText(Integer.toString(value));
	}
}
