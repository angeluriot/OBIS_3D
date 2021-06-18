package scene3d;

import javafx.geometry.Point3D;
import javafx.scene.transform.Affine;

public class Math3D
{
	public static Affine lookAt(Point3D from, Point3D to, Point3D ydir)
	{
		Point3D zVec = to.subtract(from).normalize();
		Point3D xVec = ydir.normalize().crossProduct(zVec).normalize();
		Point3D yVec = zVec.crossProduct(xVec).normalize();

		return new Affine(xVec.getX(), yVec.getX(), zVec.getX(), from.getX(),
			xVec.getY(), yVec.getY(), zVec.getY(), from.getY(),
			xVec.getZ(), yVec.getZ(), zVec.getZ(), from.getZ());
	}
}
