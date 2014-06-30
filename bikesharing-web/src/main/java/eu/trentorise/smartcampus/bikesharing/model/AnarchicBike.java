package eu.trentorise.smartcampus.bikesharing.model;

import eu.trentorise.smartcampus.bikesharing.exceptions.WebServiceErrorException;

public class AnarchicBike
{
	private String id;
	private double latitude;
	private double longitude;
	private static double latitude1 = 45.88226500641319;
	private static double longitude1 = 11.040281016146878;
	
	public AnarchicBike(String details) throws WebServiceErrorException
	{
		id = details;
		latitude = latitude1 + Math.random() * 0.1 - 0.05;
		longitude = longitude1 + Math.random() * 0.1 - 0.05;
	}

	public String getId()
	{
		return id;
	}

	public double getLatitude()
	{
		return latitude;
	}

	public double getLongitude()
	{
		return longitude;
	}

	@Override
	public String toString()
	{
		return "\nAnarchicBike [id=" + id + ", latitude=" + latitude + ", longitude=" + longitude + "]";
	}
	
	
}
