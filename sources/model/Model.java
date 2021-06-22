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
	private static ArrayList<FeatureCollection> evolution_collection;

	private static String start_date = "";
	private static String end_date = "";

	/**
	 * Initialise le json local
	 */
	public static void init_collection()
	{
		species_feature_collection = new FeatureCollection(Read.parseCollectionJson(
				"resources/data/Delphinidae.json", "Delphinidae"));
		start_date = "";
		end_date = "";
	}

	/**
	 * Initialise l'évolution du json local
	 */
	public static void init_evolution()
	{
		evolution_collection = new ArrayList<>();
		String url;

		for (int i = 0; i < 24; i++)
		{
			url = "https://api.obis.org/v3/occurrence/grid/3?scientificname=Delphinidae&startdate=" + (1900 + 5 * i) + "-01-01" +
					"&enddate=" + (1900 + 5 * i + 5) + "-01-01";
			evolution_collection.add(new FeatureCollection(Read.parseCollectionJson(Read.readJsonFromUrl(url), "Delphinidae")));
		}
	}

	/**
	 * Récupère l'espèce chargée
	 * @return l'espèce chargée
	 */
	public static FeatureCollection get_feature_collection()
	{
		return species_feature_collection;
	}

	/**
	 * Fixe une nouvelle espèce
	 * @param specie une espèce
	 */
	public static void set_collection(String specie)
	{
		String specie_space = "";

		for (char c : specie.toCharArray())
		{
			if (c == ' ')
				specie_space += "%20";

			else
				specie_space += c;
		}

		String url = "https://api.obis.org/v3/occurrence/grid/3?scientificname=" + specie_space;
		species_feature_collection = new FeatureCollection(Read.parseCollectionJson(Read.readJsonFromUrl(url), specie_space));
		start_date = "";
		end_date = "";
	}

	/**
	 * Fixe une nouvelle espèce en fonction de la précision du GeoHash
	 * Nous n'utilisons pas cette fonction sauf pour les tests
	 * @param specie une espèce
	 * @param geohash_precision la précision du GeoHash
	 */
	public static void set_collection(String specie, int geohash_precision)
	{
		String specie_space = "";

		for (char c : specie.toCharArray())
		{
			if (c == ' ')
				specie_space += "%20";

			else
				specie_space += c;
		}

		String url = "https://api.obis.org/v3/occurrence/grid/" + geohash_precision + "?scientificname=" + specie_space;
		species_feature_collection = new FeatureCollection(Read.parseCollectionJson(Read.readJsonFromUrl(url), specie_space));
		start_date = "";
		end_date = "";
	}

	/**
	 * Fixe un intervalle de temps pour l'espèce chargée
	 * @param start_date le début de l'intervalle
	 * @param end_date la fin de l'intervalle
	 */
	public static void set_date(String start_date, String end_date)
	{
		Model.start_date = start_date;
		Model.end_date = end_date;
		String url = "https://api.obis.org/v3/occurrence/grid/3?scientificname=" + species_feature_collection.get_name()
				+ "&startdate=" + start_date + "&enddate=" + end_date;

		species_feature_collection = new FeatureCollection(Read.parseCollectionJson(Read.readJsonFromUrl(url),
				species_feature_collection.get_name()), species_feature_collection.get_max_occurrence());
	}

	/**
	 * Fixe un intervalle de temps pour l'espèce chargée en fonction de la précision du GeoHash
	 * Nous n'utilisons pas cette fonction sauf pour les tests
	 * @param start_date le début de l'intervalle
	 * @param end_date la fin de l'intervalle
	 * @param geohash_precision la précision du GeoHash
	 */
	public static void set_date(String start_date, String end_date, int geohash_precision)
	{
		Model.start_date = start_date;
		Model.end_date = end_date;
		String url = "https://api.obis.org/v3/occurrence/grid/" + geohash_precision + "?scientificname=" + species_feature_collection.get_name()
				+ "&startdate=" + start_date + "&enddate=" + end_date;

		species_feature_collection = new FeatureCollection(Read.parseCollectionJson(Read.readJsonFromUrl(url),
				species_feature_collection.get_name()), species_feature_collection.get_max_occurrence());
	}

	/**
	 * Fixe l'évolution de l'espèce chargée
	 * @param specie une espèce
	 */
	public static void set_evolution(String specie)
	{
		String specie_space = "";
		evolution_collection = new ArrayList<FeatureCollection>();
		String url;

		for (char c : specie.toCharArray())
		{
			if (c == ' ')
				specie_space += "%20";

			else
				specie_space += c;
		}

		for (int i = 0; i < 24; i++)
		{
			url = "https://api.obis.org/v3/occurrence/grid/3?scientificname=" + specie_space + "&startdate=" + (1900 + 5 * i) + "-01-01" +
					"&enddate=" + (1900 + 5 * i + 5) + "-01-01";
			evolution_collection.add(new FeatureCollection(Read.parseCollectionJson(Read.readJsonFromUrl(url), specie_space)));
		}
	}

	/**
	 * Fixe l'évolution de l'espèce chargée en fonction de la précision du GeoHash
	 * Nous n'utilisons pas cette fonction
	 * @param specie une espèce
	 * @param geohash_precision la précision du GeoHash
	 */
	public static void set_evolution(String specie, int geohash_precision)
	{
		String specie_space = "";
		evolution_collection = new ArrayList<FeatureCollection>();
		String url;

		for (char c : specie.toCharArray())
		{
			if (c == ' ')
				specie_space += "%20";

			else
				specie_space += c;
		}

		for (int i = 0; i < 24; i++)
		{
			url = "https://api.obis.org/v3/occurrence/grid/" + geohash_precision + "?scientificname=" + specie_space +
					"&startdate=" + (1900 + 5 * i) + "-01-01" + "&enddate=" + (1900 + 5 * i + 5) + "-01-01";
			evolution_collection.add(new FeatureCollection(Read.parseCollectionJson(Read.readJsonFromUrl(url), specie_space)));
		}
	}

	/**
	 * Récupère le nombre d'occurrences de l'espèce chargée pour des coordonnées gps données
	 * @param lat la latitude
	 * @param lon la longitude
	 * @return le nombre d'occurrences
	 */
	public static int get_local_occurrence(double lat, double lon)
	{
		int res = 0;

		for (Feature f : species_feature_collection.get_features())
		{
			Point2D point_min = f.get_zone().get_coords()[0];
			Point2D point_max = f.get_zone().get_coords()[2];

			if (lat >= point_min.getX() && lat <= point_max.getX() && lon >= point_min.getY() && lon <= point_max.getY())
				res += f.get_number();
		}

		return res;
	}

	/**
	 * Récupère le nombre d'occurrences de l'espèce chargée lors de l'évolution à partir d'une année donnée
	 * (pendant 5 ans), et pour des coordonnées gps données
	 * @param lat la latitude
	 * @param lon la longitude
	 * @param year l'année de début
	 * @return le nombre d'occurrences
	 */
	public static int get_evolution_occurrence(double lat, double lon, int year)
	{
		int res = 0;

		for (Feature f : evolution_collection.get((year - 1900) / 5).get_features())
		{
			Point2D point_min = f.get_zone().get_coords()[0];
			Point2D point_max = f.get_zone().get_coords()[2];

			if (lat > point_min.getX() && lat < point_max.getX() && lon > point_min.getY() && lon < point_max.getY())
				res += f.get_number();
		}

		return res;
	}

	/**
	 * Récupère le nombre d'occurrences d'une espèce à partir d'une requête http
	 * Nous n'utilisons pas cette fonction, voir get_local_occurrence et set_collection
	 * @param lat la latitude
	 * @param lon la longitude
	 * @param geohash_precision la précision du GeoHash
	 * @param specie l'espèce
	 * @return le nombre d'occurrences
	 */
	public static int get_occurrence(double lat, double lon, int geohash_precision, String specie)
	{
		int res = 0;
		String geohash = gps_to_geohash((float) lat, (float) lon, geohash_precision);
		String url = "https://api.obis.org/v3/occurrence/grid/3?scientificname=" + specie + "&geometry=" + geohash;

		FeatureCollection collection = Read.parseCollectionJson(Read.readJsonFromUrl(url), specie);

		for (Feature f : collection.get_features())
			res += f.get_number();

		return res;
	}


	/**
	 * Récupère le nombre d'occurrences d'une espèce à partir d'une requête http et pour un intervalle de temps
	 * Nous n'utilisons pas cette fonction, voir get_local_occurrence, set_collection et set_date
	 * @param lat la latitude
	 * @param lon la longitude
	 * @param geohash_precision la précision du GeoHash
	 * @param specie l'espèce
	 * @param start_date le début de l'intervalle
	 * @param end_date la fin de l'intervalle
	 * @return le nombre d'occurrences
	 */
	public static int get_occurrence(double lat, double lon, int geohash_precision, String specie, String start_date, String end_date)
	{
		int res = 0;
		String geohash = gps_to_geohash((float) lat, (float) lon, geohash_precision);
		String url = "https://api.obis.org/v3/occurrence/grid/3?scientificname=" + specie + "&startdate=" + start_date +
				"&enddate=" + end_date + "&geometry=" + geohash;

		FeatureCollection collection = Read.parseCollectionJson(Read.readJsonFromUrl(url), specie);

		for (Feature f : collection.get_features())
			res += f.get_number();

		return res;
	}

	/**
	 * Récupère les nombres d'occurrences d'une espèce à partir d'une requête http et pour plusieurs intervalles de temps
	 * Nous n'utilisons pas cette fonction, voir get_evolution_occurrence et set_evolution
	 * @param lat la latitude
	 * @param lon la longitude
	 * @param geohash_precision la précision du GeoHash
	 * @param specie l'espèce
	 * @param start_date le début de l'intervalle
	 * @param interval taille de l'intervalle
	 * @param interval_nb nombre d'intervalles
	 * @return tableau contenant le nombre d'occurrences pour chaque intervalle
	 */
	public static ArrayList<Integer> get_occurrence(double lat, double lon, int geohash_precision, String specie,
		String start_date, String interval, int interval_nb)
	{
		ArrayList<Integer> res = new ArrayList<>();
		String geohash = gps_to_geohash((float) lat, (float) lon, geohash_precision);
		String url;
		Time start_time = new Time(start_date);
		Time interval_time = new Time(interval);
		Time end_time = new Time(start_time);
		end_time.add_interval(interval_time);

		for (int i = 0; i < interval_nb; i++)
		{
			res.add(0);
			int compt = 0;
			url = "https://api.obis.org/v3/occurrence/grid/3?scientificname=" + specie + "&startdate=" +
					start_time.get_date() + "&enddate=" + end_time.get_date()+ "&geometry=" + geohash;
			FeatureCollection collection = Read.parseCollectionJson(Read.readJsonFromUrl(url), specie);

			for (Feature f : collection.get_features())
				compt += f.get_number();

			res.set(i, compt);
			start_time.add_interval(interval_time);
			end_time.add_interval(interval_time);
		}

		return res;
	}

	/**
	 * Récupère la liste des espèces repérées à un GeoHash donné
	 * @param geohash le GeoHash
	 * @return la liste des espèces
	 */
	public static ArrayList<String> get_species_from_geohash(String geohash)
	{
		String url = "https://api.obis.org/v3/occurrence?";

		if (!start_date.equals("") && !end_date.equals(""))
			url += ("&startdate=" + start_date + "&enddate=" + end_date);

		url += ("&geometry=" + geohash);

		return Read.parseNamesJson(Read.readJsonFromUrl(url));
	}

	/**
	 * Récupère la liste des détails d'observations de chaque espèce observée à un GeoHash donné
	 * @param geohash le GeoHash
	 * @return la liste des détails d'observations
	 */
	public static ArrayList<Observation> get_observation(String geohash)
	{
		String url = "https://api.obis.org/v3/occurrence?";

		if (species_feature_collection.get_name().length() > 0)
			url += ("scientificname=" + species_feature_collection.get_name());

		if (!start_date.equals("") && !end_date.equals(""))
			url += ("&startdate=" + start_date + "&enddate=" + end_date);

		url += ("&geometry=" + geohash);

		return Read.parseObservationJson(Read.readJsonFromUrl(url));
	}

	/**
	 * Récupère la liste des espèces dont le nom commence par une chaîne de caractères donnée
	 * @param begin la chaîne de caractères
	 * @return la liste des espèces
	 */
	public static ArrayList<String> get_species(String begin)
	{
		String url = "https://api.obis.org/v3/taxon/complete/verbose/" + begin;
		return Read.parseVerboseJson(Read.readJsonArrayFromUrl(url));
	}

	/**
	 * Récupère le nombre maximal d'occurrences sur un GeoHash d'une espèce
	 * @return le nombre maximal d'occurrences
	 */
	public static int get_max_occurrence()
	{
		return species_feature_collection.get_max_occurrence();
	}

	/**
	 * Convertit une coordonnées GPS en GeoHash
	 * @param lat la latitude
	 * @param lon la longitude
	 * @param precision la précision du GeoHash
	 * @return le GeoHash
	 */
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
					idx = idx * 2 + 1;
					lon_min = lonMid;
				}

				else
				{
					idx = idx * 2;
					lon_max = lonMid;
				}
			}

			else
			{
				float latMid = (lat_min + lat_max) / 2;

				if (lat > latMid)
				{
					idx = idx * 2 + 1;
					lat_min = latMid;
				}

				else
				{
					idx = idx * 2;
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
