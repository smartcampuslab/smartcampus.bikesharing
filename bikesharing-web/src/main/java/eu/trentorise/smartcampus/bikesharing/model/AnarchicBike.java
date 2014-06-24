package eu.trentorise.smartcampus.bikesharing.model;

import eu.trentorise.smartcampus.bikesharing.exceptions.WebServiceErrorException;

public class AnarchicBike
{
	private String id;
	private double latitude;
	private double longitude;
	
	public AnarchicBike(String details) throws WebServiceErrorException
	{
		id = details;
		latitude = 0.0;
		longitude = 0.0;
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
