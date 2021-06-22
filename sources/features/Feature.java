package features;

public class Feature
{
	private int number;
	private Zone zone;

	public Feature(int number, Zone zone)
	{
		this.number = number;
		this.zone = zone;
	}

	/**
	 * Récupère le nombre d'occurrences de l'espèce dans l'observation
	 * @return le nombre d'occurrences
	 */
	public final int get_number()
	{
		return number;
	}

	/**
	 * Récupère la zone de l'observation
	 * @return la zone
	 */
	public final Zone get_zone()
	{
		return zone;
	}

	@Override
	public String toString()
	{
		return  "n = " + number +
				"\npoint 1 : " + zone.get_coords()[0].toString() +
				"\npoint 2 : " + zone.get_coords()[1].toString() +
				"\npoint 3 : " + zone.get_coords()[2].toString() +
				"\npoint 4 : " + zone.get_coords()[3].toString();
	}
}
