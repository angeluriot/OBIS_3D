package model;

import features.FeatureCollection;

import java.util.ArrayList;

public class Model
{
	private FeatureCollection[] species_feature_collection;
	
	public final FeatureCollection get_feature_collection(String specie)
	{
		for (FeatureCollection collection : species_feature_collection)
			if (collection.get_name().equals(specie))
				return collection;
		
		return null;
	}
	
	public final ArrayList<String> get_species(String begin)
	{
		ArrayList<String> species_list = new ArrayList<>();
		
		for (FeatureCollection collection : species_feature_collection)
		{
			char[] name = collection.get_name().toCharArray();
			String begin_name = "";
			
			for (int i = 0; i < begin.length() && i < name.length; i++)
				begin_name += name[i];
			
			if (begin_name.equals(begin))
				species_list.add(collection.get_name());
		}
		
		return species_list;
	}
}
