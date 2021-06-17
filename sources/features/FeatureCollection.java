package features;

public class FeatureCollection
{
	private String name;
	private Feature[] features;

	public FeatureCollection(String name, Feature[] features)
	{
		this.name = name;
		this.features = features;
	}

	public FeatureCollection(FeatureCollection other)
	{
		name = other.get_name();
		features = other.get_features();
	}
	
	public final String get_name()
	{
		return name;
	}
	
	public final Feature[] get_features()
	{
		return features;
	}
}
