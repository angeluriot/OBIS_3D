package scene3d;

import javafx.geometry.Point3D;
import javafx.scene.transform.Affine;

public class Math3D
{
	/**
	 * Donne l'affine vers un point
	 * @param from Le point d'origine
	 * @param to Le point d'arrivée
	 * @param y_dir La direction Y
	 * @return L'affine vers le point
	 */
	public static Affine look_at(Point3D from, Point3D to, Point3D y_dir)
	{
		Point3D z_vec = to.subtract(from).normalize();
		Point3D x_vec = y_dir.normalize().crossProduct(z_vec).normalize();
		Point3D y_vec = z_vec.crossProduct(x_vec).normalize();

		return new Affine(x_vec.getX(), y_vec.getX(), z_vec.getX(), from.getX(),
			x_vec.getY(), y_vec.getY(), z_vec.getY(), from.getY(),
			x_vec.getZ(), y_vec.getZ(), z_vec.getZ(), from.getZ());
	}
}
