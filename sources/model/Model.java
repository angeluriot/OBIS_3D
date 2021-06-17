package model;

import features.Feature;
import features.FeatureCollection;
import data.Read;
import javafx.geometry.Point2D;
import utils.Observation;
import utils.Time;

import java.util.ArrayList;

public class Model
{
	private static FeatureCollection species_feature_collection;
	
	public Model()
	{
		species_feature_collection = new FeatureCollection(Read.parseCollectionJson(
				"resources/data/Delphinidae.json", "Delphinidae"));
	}
	
	public static final FeatureCollection get_feature_collection(String specie)
	{
		return species_feature_collection;
	}

	public static int get_local_occurrence(double lat, double lon)
	{
		int res = 0;

		for(Feature f : species_feature_collection.get_features())
		{
			Point2D point_min = f.get_zone().get_coords()[0];
			Point2D point_max = f.get_zone().get_coords()[2];
			if(lat > point_min.getX() && lat < point_max.getX()
			&& lon > point_min.getY() && lon < point_max.getY())
				res += f.get_number();
		}

		return res;
	}
	
	public static int get_occurrence(double lat, double lon, int geohash_precision, String specie)
	{
		int res = 0;
		String geohash = gps_to_geohash((float) lat, (float) lon, geohash_precision);
		String url = "https://api.obis.org/v3/occurrence/grid/3?scientificname=" + specie + "&geometry=" + geohash;

		FeatureCollection collection = Read.parseCollectionJson(Read.readJsonFromUrl(url), specie);

		for(Feature f : collection.get_features())
			res += f.get_number();

		return res;
	}

	// A changer au besoin : start_date et end_date doivent être entrées de la manière suivante : YYYY-MM-DD
	public static int get_occurrence(double lat, double lon, int geohash_precision, String specie, String start_date,
							  String end_date)
	{
		int res = 0;
		String geohash = gps_to_geohash((float) lat, (float) lon, geohash_precision);
		String url = "https://api.obis.org/v3/occurrence/grid/3?scientificname=" + specie + "&startdate=" + start_date
				+ "&enddate=" + end_date + "&geometry=" + geohash;

		FeatureCollection collection = Read.parseCollectionJson(Read.readJsonFromUrl(url), specie);

		for(Feature f : collection.get_features())
			res += f.get_number();

		return res;
	}

	public static ArrayList<Integer> get_occurrence(double lat, double lon, int geohash_precision, String specie, String start_date,
							  String interval, int interval_nb)
	{
		ArrayList<Integer> res = new ArrayList<Integer>();
		String geohash = gps_to_geohash((float) lat, (float) lon, geohash_precision);
		String url;
		Time start_time = new Time(start_date);
		Time interval_time = new Time(interval);
		Time end_time = new Time(start_time);
		end_time.add_interval(interval_time);

		for(int i = 0; i < interval_nb; i++)
		{
			res.add(0);
			int compt = 0;
			url = "https://api.obis.org/v3/occurrence/grid/3?scientificname=" + specie + "&startdate=" +
					start_time.get_date() + "&enddate=" + end_time.get_date()+ "&geometry=" + geohash;
			FeatureCollection collection = Read.parseCollectionJson(Read.readJsonFromUrl(url), specie);

			for(Feature f : collection.get_features())
				compt += f.get_number();
			res.set(i, compt);
			start_time.add_interval(interval_time);
			end_time.add_interval(interval_time);
		}

		return res;
	}

	public static ArrayList<Observation> get_observation(String geohash, String scientific_name)
	{
		String url = "https://api.obis.org/v3/occurrence?";
		if(scientific_name.length() > 0)
			url += ("scientificname=" + scientific_name + "&amp;");
		url += ("geometry=" + geohash);

		return Read.parseObservationJson(Read.readJsonFromUrl(url));
	}

	/*
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

	 */

	public static final int get_min_occurrence()
	{
		int min = Integer.MAX_VALUE;

		for (Feature feature : species_feature_collection.get_features())
		{
			int number = feature.get_number();

			if (number < min)
				min = number;
		}

		return min;
	}

	public static final int get_max_occurrence()
	{
		int max = 0;

		for (Feature feature : species_feature_collection.get_features())
		{
			int number = feature.get_number();

			if (number > max)
				max = number;

		}

		return max;
	}
	
	public static String gps_to_geohash(float lat, float lon, float precision)
	{
		String base32 = "0123456789bcdefghjkmnpqrstuvwxyz";
		int idx = 0;
		int bit = 0;
		boolean even_bit = true;
		String geohash = "";
		
		float lat_min =  -90;
		float lat_max =   90;
		float lon_min = -180;
		float lon_max =  180;
		
		while (geohash.length() < precision)
		{
			if (even_bit)
			{
				float lonMid = (lon_min + lon_max) / 2;
				
				if (lon > lonMid)
				{
					idx = idx*2 + 1;
					lon_min = lonMid;
				}
				
				else
				{
					idx = idx*2;
					lon_max = lonMid;
				}
				
			}
			
			else
			{
				float latMid = (lat_min + lat_max) / 2;
				
				if (lat > latMid)
				{
					idx = idx*2 + 1;
					lat_min = latMid;
				}
				
				else
				{
					idx = idx*2;
					lat_max = latMid;
				}
			}
			
			even_bit = !even_bit;
			
			if (++bit == 5)
			{
				geohash += base32.charAt(idx);
				bit = 0;
				idx = 0;
			}
		}
		
		return geohash;
	}
}
