package scene3d;

import javafx.scene.control.Label;

public class Legend
{
	public static Label min;
	public static Label max;

	/**
	 * Met à jour les valeurs de la légende
	 * @param new_max La nouvelle valeur maximum
	 */
	public static void update(int new_max)
	{
		max.setText(Integer.toString(new_max));
		min.setText(Integer.toString(Math.min(1, new_max)));
	}
}
