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
	
	public final String get_name()
	{
		return name;
	}
	
	public final Feature[] get_features()
	{
		return features;
	}
	
	public final Feature get_min_occurrence()
	{
		int min = Integer.MAX_VALUE;
		Feature min_feature = features[0];
		
		for (Feature feature : features)
		{
			int number = feature.get_number();
			
			if (number < min)
			{
				min = number;
				min_feature = feature;
			}
		}
		
		return min_feature;
	}
	
	public final Feature get_max_occurrence()
	{
		int max = 0;
		Feature max_feature = features[0];
		
		for (Feature feature : features)
		{
			int number = feature.get_number();
			
			if (number > max)
			{
				max = number;
				max_feature = feature;
			}
		}
		
		return max_feature;
	}
}
