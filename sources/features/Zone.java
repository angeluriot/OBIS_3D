package features;

import javafx.geometry.Point2D;

public class Zone
{
	private Point2D[] coords;
	
	public Zone(Point2D point_1, Point2D point_2, Point2D point_3, Point2D point_4)
	{
		coords = new Point2D[]{ point_1, point_2, point_3, point_4 };
	}
	
	public final Point2D[] get_coords()
	{
		return coords;
	}
}
