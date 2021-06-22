package utils;

public class Observation
{
	private final String scientific_name;
	private final String order;
	private final String super_class;
	private final String recorded_by;
	private final String specie;

	public Observation(String scientific_name, String order, String super_class, String recorded_by, String specie)
	{
		this.scientific_name = scientific_name;
		this.order = order;
		this.super_class = super_class;
		this.recorded_by = recorded_by;
		this.specie = specie;
	}

	/**
	 * Récupère le nom scientifique de l'espèce
	 * @return le nom scientifique
	 */
	public final String get_scientific_name()
	{
		return scientific_name;
	}

	/**
	 * Récupère l'ordre de l'espèce
	 * @return l'ordre
	 */
	public final String get_order()
	{
		return order;
	}

	/**
	 * Récupère la super classe de l'espèce
	 * @return la super classe
	 */
	public final String get_super_class()
	{
		return super_class;
	}

	/**
	 * Récupère le nom de l'organisme qui a observé l'espèce
	 * @return le nom de l'organisme
	 */
	public final String get_recorded_by()
	{
		return recorded_by;
	}

	/**
	 * Récupère le nom de l'espèce
	 * @return le nom de l'espèce
	 */
	public final String get_specie()
	{
		return specie;
	}

	@Override
	public String toString()
	{
		return  "Nom scientifique : " + scientific_name +
				"\nOrdre : " + order +
				"\nSuper classe : " + super_class +
				"\nEnregistre par : " + recorded_by +
				"\nEspece : " + specie;
	}
}
