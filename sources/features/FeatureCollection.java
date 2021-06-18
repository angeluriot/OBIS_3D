package features;

public class FeatureCollection
{
	private String name;
	private Feature[] features;
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

	public final String get_name()
	{
		return name;
	}

	public final Feature[] get_features()
	{
		return features;
	}

	public final int get_max_occurrence()
	{
		return max_occurrence;
	}
}
