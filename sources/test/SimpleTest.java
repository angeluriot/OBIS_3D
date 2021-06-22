package test;

import static org.junit.Assert.*;

import features.FeatureCollection;
import org.junit.Before;
import org.junit.Test;

import model.Model;
import utils.Observation;

import java.util.ArrayList;


public class SimpleTest
{
	@Before
	public void setUp() throws Exception
	{
		Model.init_collection();
        FeatureCollection feature_collection = Model.get_feature_collection();

		assertNotEquals(null, feature_collection);

		if (feature_collection == null)
			throw new Exception();

        try
        {
            if (!feature_collection.get_name().equals("Delphinidae"))
            	throw new Exception();
        }

        catch (Exception e)
		{
        	e.printStackTrace();
        }
	}

	@Test
	public void LoadTest()
	{
		Model.init_collection();
		assertEquals("Delphinidae", Model.get_feature_collection().get_name());
	    Model.set_collection("Selachii");
	    assertEquals("Selachii", Model.get_feature_collection().get_name());
	    Model.set_collection("Cetacea");
	    assertEquals("Cetacea", Model.get_feature_collection().get_name());
	}

	@Test
	public void OccurrenceTest()
	{
		Model.init_collection();
		assertEquals(8147, Model.get_local_occurrence(33, -79));
		Model.set_collection("Selachii");
		assertEquals(163261, Model.get_local_occurrence(-19, 147));
		Model.set_collection("Cetacea", 5);
		assertEquals(1685, Model.get_local_occurrence(27.45, -82.68));
	}

	@Test
	public void DateOccurrenceTest()
	{
		Model.init_collection();
		Model.set_date("2000-01-01", "2005-01-01");
		assertEquals(126, Model.get_local_occurrence(40, -71));
		Model.set_collection("Selachii", 4);
		Model.set_date("2015-06-17", "2021-06-17", 4);
		assertEquals(19078, Model.get_local_occurrence(40.5, -71.5));
	}

	@Test
	public void IntervalOccurrenceTest()
	{
		Model.set_collection("Selachii");
		Model.set_evolution("Selachii");
		int test1 = Model.get_evolution_occurrence(40, -71, 2000);
		int test2 = Model.get_evolution_occurrence(40, -71, 2005);
		int test3 = Model.get_evolution_occurrence(40, -71, 2010);

		assertEquals(922, test1);
		assertEquals(306, test2);
		assertEquals(28, test3);
	}

	@Test
	public void ObservationTest()
	{
		Model.init_collection();
		Observation test = Model.get_observation("spd").get(0);

		assertEquals("Stenella coeruleoalba", test.get_scientific_name());
		assertEquals("Cetartiodactyla", test.get_order());
		assertEquals("Tetrapoda", test.get_super_class());
		// Il n'y a pas de "recordedBy"
		assertEquals("Stenella coeruleoalba", test.get_specie());

		Model.set_collection("Selachii");
		test = Model.get_observation("spd").get(0);

		assertEquals("Oxynotus centrina", test.get_scientific_name());
		assertEquals("Squaliformes", test.get_order());
		assertEquals("Pisces", test.get_super_class());
		assertEquals("bauchot", test.get_recorded_by());
		assertEquals("Oxynotus centrina", test.get_specie());
	}

	@Test
	public void SpecieNamesTest()
	{
		ArrayList<String> test = Model.get_species("ac");
		boolean test_Actinobacteria = false;
		boolean test_Acanthocephala = false;
		boolean test_Aculifera = false;
		boolean test_Acritarcha = false;
		boolean test_Acidobacteria = false;

		for (String specie : test)
			switch (specie)
			{
				case "Actinobacteria" -> test_Actinobacteria = true;
				case "Acanthocephala" -> test_Acanthocephala = true;
				case "Aculifera" -> test_Aculifera = true;
				case "Acritarcha" -> test_Acritarcha = true;
				case "Acidobacteria" -> test_Acidobacteria = true;
			}

		assertTrue("Actinobacteria not found", test_Actinobacteria);
		assertTrue("Acanthocephala not found", test_Acanthocephala);
		assertTrue("Aculifera not found", test_Aculifera);
		assertTrue("Acritarcha not found", test_Acritarcha);
		assertTrue("Acidobacteria not found", test_Acidobacteria);
	}

	@Test
	public void GeohashConversionTest()
	{
		assertEquals("spd", Model.gps_to_geohash(42.89f, 3.52f, 3));
		assertEquals("7zz", Model.gps_to_geohash(0.f, 0.f, 3));
		assertEquals("czbzur", Model.gps_to_geohash(100.f, -100.f, 6));
	}
}



