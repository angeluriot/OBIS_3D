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
		if(feature_collection == null)
		{
			throw new Exception();
		}

        try
        {
            if(!feature_collection.get_name().equals("Delphinidae"))
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
	    Model.set_collection("Selachii");
	    assertEquals("Selachii", Model.get_feature_collection().get_name());
	}

	@Test
	public void HttpOccurrenceTest()
	{
		Model.set_collection("Selachii");
		assertEquals(163261, Model.get_local_occurrence(-19, 147));
	}

	@Test
	public void HttpDateOccurrenceTest()
	{
		Model.set_collection("Selachii");
		Model.set_date("2015-06-17", "2021-06-17");
		assertEquals(20218, Model.get_local_occurrence(40, -71));
	}

	@Test
	public void HttpIntervalOccurrenceTest()
	{
		Model.set_collection("Selachii");
		Model.set_evolution("Selachii");
		int test1 = Model.get_evolution_occurrence(40, -71, 2000);
		int test2 = Model.get_evolution_occurrence(40, -71, 2005);
		int test3 = Model.get_evolution_occurrence(40, -71, 2010);

		assertEquals(139,		test1);
		assertEquals(150,		test2);
		assertEquals(4,	test3);
	}

	@Test
	public void ObservationTest()
	{
		Model.set_collection("Selachii");
		Observation test = Model.get_observation("spd").get(0);

		assertEquals("Etmopterus baxteri", test.get_scientific_name());
		assertEquals("Squaliformes", test.get_order());
		assertEquals("Pisces", test.get_super_class());
		assertEquals("", test.get_recorded_by());
		assertEquals("Etmopterus baxteri", test.get_specie());
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

		for(String specie : test)
		{
			switch (specie) {
				case "Actinobacteria" -> test_Actinobacteria = true;
				case "Acanthocephala" -> test_Acanthocephala = true;
				case "Aculifera" -> test_Aculifera = true;
				case "Acritarcha" -> test_Acritarcha = true;
				case "Acidobacteria" -> test_Acidobacteria = true;
			}
		}

		assertTrue("Actinobacteria not found", test_Actinobacteria);
		assertTrue("Acanthocephala not found", test_Acanthocephala);
		assertTrue("Aculifera not found", test_Aculifera);
		assertTrue("Acritarcha not found", test_Acritarcha);
		assertTrue("Acidobacteria not found", test_Acidobacteria);
	}

}



