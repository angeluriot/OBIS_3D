package data;

import features.Zone;
import javafx.geometry.Point2D;
import org.json.JSONArray;
import org.json.JSONObject;

import features.Feature;
import features.FeatureCollection;
import utils.Observation;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Read
{
	/**
	 * Permet de lire un fichier et de renvoyer un String de son contenu
	 * @param rd le fichier
	 * @return le String
	 * @throws IOException exception à la lecture du fichier
	 */
	public static String readAll(Reader rd) throws IOException
	{
		StringBuilder sb = new StringBuilder();
		int cp;

		while ((cp = rd.read()) != -1)
			sb.append((char) cp);

		return sb.toString();
	}

	/**
	 * Lit un fichier json depuis une url et renvoie son objet
	 * @param url l'url
	 * @return l'objet json
	 */
	public static JSONObject readJsonFromUrl(String url)
	{
		String json = "";

		HttpClient client = HttpClient.newBuilder()
				.version(HttpClient.Version.HTTP_1_1)
				.followRedirects(HttpClient.Redirect.NORMAL)
				.connectTimeout(Duration.ofSeconds(20))
				.build();

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(url))
				.timeout(Duration.ofMinutes(2))
				.header("Content-type", "application/json")
				.GET()
				.build();

		try
		{
			json = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
					.thenApply(HttpResponse::body).get(10, TimeUnit.SECONDS);
		}

		catch (Exception e)
		{
			e.printStackTrace();
		}

		return new JSONObject(json);
	}

	/**
	 * Lit un fichier json depuis une url et renvoie son tableau
	 * @param url l'url
	 * @return le tableau json
	 */
	public static JSONArray readJsonArrayFromUrl(String url)
	{
		String json = "";

		HttpClient client = HttpClient.newBuilder()
				.version(HttpClient.Version.HTTP_1_1)
				.followRedirects(HttpClient.Redirect.NORMAL)
				.connectTimeout(Duration.ofSeconds(20))
				.build();

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(url))
				.timeout(Duration.ofMinutes(2))
				.header("Content-type", "application/json")
				.GET()
				.build();

		try
		{
			json = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
					.thenApply(HttpResponse::body).get(10, TimeUnit.SECONDS);
		}

		catch (Exception e)
		{
			e.printStackTrace();
		}

		return new JSONArray(json);
	}

	/**
	 * Récupère le contenu de chaque observation d'un fichier json sous forme de String
	 * @param json le fichier json sous forme de String
	 * @param name le nom de l'espèce dont le json contient les observations
	 * @return un FeatureCollection contenant les données de chaque observation
	 */
	public static FeatureCollection parseCollectionJson(String json, String name)
	{
		try (Reader reader = new FileReader(json))
		{
			ArrayList<Feature> features_list = new ArrayList<>();
			BufferedReader rd = new BufferedReader(reader);
			String jsonText = readAll(rd);
			JSONObject jsonRoot = new JSONObject(jsonText);
			JSONArray features = jsonRoot.getJSONArray("features");

			for (int i = 0; i < features.length(); i++)
			{
				JSONObject feature = features.getJSONObject(i);
				JSONObject geometry = feature.getJSONObject("geometry");
				JSONArray coordinates_base = geometry.getJSONArray("coordinates");
				JSONArray coordinates = coordinates_base.getJSONArray(0);
				JSONArray coordinates_1 = coordinates.getJSONArray(0);

				Point2D point_1 = new Point2D(coordinates_1.getDouble(1), coordinates_1.getDouble(0));
				JSONArray coordinates_2 = coordinates.getJSONArray(1);
				Point2D point_2 = new Point2D(coordinates_2.getDouble(1), coordinates_2.getDouble(0));
				JSONArray coordinates_3 = coordinates.getJSONArray(2);
				Point2D point_3 = new Point2D(coordinates_3.getDouble(1), coordinates_3.getDouble(0));
				JSONArray coordinates_4 = coordinates.getJSONArray(3);
				Point2D point_4 = new Point2D(coordinates_4.getDouble(1), coordinates_4.getDouble(0));
				JSONObject properties = feature.getJSONObject("properties");
				features_list.add(new Feature(properties.getInt("n"), new Zone(point_1, point_2, point_3, point_4)));
			}

			Feature[] feature_array = features_list.toArray(new Feature[0]);

			return new FeatureCollection(name, feature_array);
		}

		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Récupère le contenu de chaque observation d'un objet json
	 * @param json l'objet json
	 * @param name le nom de l'espèce dont le json contient les observations
	 * @return un FeatureCollection contenant les données de chaque observation
	 */
	public static FeatureCollection parseCollectionJson(JSONObject json, String name)
	{
		ArrayList<Feature> features_list = new ArrayList<>();
		JSONArray features = json.getJSONArray("features");

		for (int i = 0; i < features.length(); i++)
		{
			JSONObject feature = features.getJSONObject(i);
			JSONObject geometry = feature.getJSONObject("geometry");
			JSONArray coordinates_base = geometry.getJSONArray("coordinates");
			JSONArray coordinates = coordinates_base.getJSONArray(0);
			JSONArray coordinates_1 = coordinates.getJSONArray(0);

			Point2D point_1 = new Point2D(coordinates_1.getDouble(1), coordinates_1.getDouble(0));
			JSONArray coordinates_2 = coordinates.getJSONArray(1);
			Point2D point_2 = new Point2D(coordinates_2.getDouble(1), coordinates_2.getDouble(0));
			JSONArray coordinates_3 = coordinates.getJSONArray(2);
			Point2D point_3 = new Point2D(coordinates_3.getDouble(1), coordinates_3.getDouble(0));
			JSONArray coordinates_4 = coordinates.getJSONArray(3);
			Point2D point_4 = new Point2D(coordinates_4.getDouble(1), coordinates_4.getDouble(0));
			JSONObject properties = feature.getJSONObject("properties");
			features_list.add(new Feature(properties.getInt("n"), new Zone(point_1, point_2, point_3, point_4)));
		}

		Feature[] feature_array = features_list.toArray(new Feature[0]);

		return new FeatureCollection(name, feature_array);
	}

	/**
	 * Récupère le nom de chaque observation d'un objet json
	 * @param json l'objet json
	 * @return la liste des noms
	 */
	public static ArrayList<String> parseNamesJson(JSONObject json)
	{
		ArrayList<String> names = new ArrayList<>();
		JSONArray results = json.getJSONArray("results");

		for (int i = 0; i < results.length(); i++)
		{
			JSONObject result = results.getJSONObject(i);
			String scientific_name = result.getString("scientificName");
			names.add(scientific_name);
		}

		return names;
	}

	/**
	 * Récupère tous les détails d'observation d'un objet json
	 * @param json l'objet json
	 * @return la liste des détails d'observation
	 */
	public static ArrayList<Observation> parseObservationJson(JSONObject json)
	{
		ArrayList<Observation> observations = new ArrayList<>();
		JSONArray results = json.getJSONArray("results");

		for (int i = 0; i < results.length(); i++)
		{
			JSONObject result = results.getJSONObject(i);

			String scientific_name;

			try
			{
				scientific_name = result.getString("scientificName");
			}

			catch (Exception e)
			{
				scientific_name = "";
			}

			String order;

			try
			{
				order = result.getString("order");
			}

			catch (Exception e)
			{
				order = "";
			}

			String super_class;

			try
			{
				super_class = result.getString("superclass");
			}

			catch (Exception e)
			{
				super_class = "";
			}

			String recorded_by;

			try
			{
				recorded_by = result.getString("recordedBy");
			}

			catch (Exception e)
			{
				recorded_by = "";
			}

			String specie;

			try
			{
				specie = result.getString("species");
			}

			catch (Exception e)
			{
				specie = "";
			}

			observations.add(new Observation(scientific_name, order, super_class, recorded_by, specie));
		}

		return observations;
	}

	/**
	 * Récupère une liste de noms depuis un tableau json
	 * @param json_array le tableau json
	 * @return la liste de noms
	 */
	public static ArrayList<String> parseVerboseJson(JSONArray json_array)
	{
		ArrayList<String> scientific_names = new ArrayList<>();

		for (int i = 0; i < json_array.length(); i++)
		{
			JSONObject specie = json_array.getJSONObject(i);
			scientific_names.add(specie.getString("scientificName"));
		}

		return scientific_names;
	}
}
