package data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class Read
{
	public static String readAll(Reader rd) throws IOException
	{
		StringBuilder sb = new StringBuilder();
		int cp;
		
		while ((cp = rd.read()) != -1)
			sb.append((char) cp);

		return sb.toString();
	}
	
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

    /*
	public static SpecieFeature readLocalFile(String file) {
		try (Reader reader = new java.io.FileReader(file)) {
			BufferedReader rd = new BufferedReader(reader);
			String jsonText = readAll(rd);

			return ObisParser.parseOccurrencesJsonObject(new JSONObject(jsonText), "Delphinidae", 3);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	*/
	
	public static void readerException(String fileName)
	{
		try (Reader reader = new FileReader(fileName))
		{
			BufferedReader rd = new BufferedReader(reader);
			String jsonText = readAll(rd);
			JSONObject jsonRoot = new JSONObject(jsonText);
			JSONArray resultatRecherche = jsonRoot.getJSONObject("query").getJSONArray("search");
			JSONObject article = resultatRecherche.getJSONObject(0);
			System.out.println(article.getString("title"));
			System.out.println(article.getString("snippet"));
			
		}
		
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
