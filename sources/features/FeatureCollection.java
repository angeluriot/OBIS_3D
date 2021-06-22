package features;

public class FeatureCollection
{
	private final String name;
	private final Feature[] features;
	private int max_occurrence;

	public FeatureCollection(String name, Feature[] features)
	{
		this.name = name;
		this.features = features;

		max_occurrence = 0;

		for (Feature feature : features)
		{
			int number = feature.get_number();

			if (number > max_occurrence)
				max_occurrence = number;
		}
	}

	public FeatureCollection(FeatureCollection other)
	{
		name = other.get_name();
		features = other.get_features();

		int max = 0;

		for (Feature feature : features)
		{
			int number = feature.get_number();

			if (number > max)
				max = number;
		}

		max_occurrence = max;
	}

	public FeatureCollection(FeatureCollection other, int max_occurrence)
	{
		name = other.get_name();
		features = other.get_features();
		this.max_occurrence = max_occurrence;
	}

	/**
	 * Récupère le nom de l'espèce
	 * @return le nom de l'espèce
	 */
	public final String get_name()
	{
		return name;
	}

	/**
	 * Récupère les observations de l'espèce
	 * @return les observations de l'espèce
	 */
	public final Feature[] get_features()
	{
		return features;
	}

	/**
	 * Récupère le nombre maximal d'occurrences sur un GeoHash d'une espèce
	 * @return le nombre maximal d'occurrences
	 */
	public final int get_max_occurrence()
	{
		return max_occurrence;
	}
}
