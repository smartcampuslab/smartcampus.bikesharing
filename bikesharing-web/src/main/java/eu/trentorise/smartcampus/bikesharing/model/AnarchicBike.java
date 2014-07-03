package eu.trentorise.smartcampus.bikesharing.model;

import eu.trentorise.smartcampus.bikesharing.exceptions.WebServiceErrorException;

public class AnarchicBike
{
	private String id;
	private double latitude;
	private double longitude;
	private static double latitude1 = 45.88226500641319;
	private static double longitude1 = 11.040281016146878;
	private int reportsNumber;
	
	public AnarchicBike(String details) throws WebServiceErrorException
	{
		id = details;
		latitude = latitude1 + Math.random() * 0.1 - 0.05;
		longitude = longitude1 + Math.random() * 0.1 - 0.05;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public static double getLatitude1() {
		return latitude1;
	}

	public static void setLatitude1(double latitude1) {
		AnarchicBike.latitude1 = latitude1;
	}

	public static double getLongitude1() {
		return longitude1;
	}

	public static void setLongitude1(double longitude1) {
		AnarchicBike.longitude1 = longitude1;
	}

	public int getReportsNumber() {
		return reportsNumber;
	}

	public void setReportsNumber(int reportsNumber) {
		this.reportsNumber = reportsNumber;
	}

	@Override
	public String toString() {
		return "AnarchicBike [id=" + id + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", reportsNumber="
				+ reportsNumber + "]";
	}
}
