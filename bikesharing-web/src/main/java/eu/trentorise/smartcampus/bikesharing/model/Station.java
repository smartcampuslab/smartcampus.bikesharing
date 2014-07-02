package eu.trentorise.smartcampus.bikesharing.model;

import eu.trentorise.smartcampus.bikesharing.exceptions.WebServiceErrorException;

public class Station
{
	private String name;
	private String street;
	private String id;
	private int nBikes;
	private int maxSlots;
	private int nBrokenBikes;
	private double latitude;
	private double longitude;
	private int reportsNumber;
	
	public Station(String details) throws WebServiceErrorException
	{
		if(details == null) throw new WebServiceErrorException("null string");
		if(details.equals("")) throw new WebServiceErrorException("empty string");
		
		String[] attributes = details.split(";");
		
		if(attributes.length != 7) throw new WebServiceErrorException("invalid string: " + details);
		
		for(String att: attributes)
		{
			if(att == null || att.equals("")) throw new WebServiceErrorException("null element: " + details);
		}
		
		id = attributes[0];
		name = attributes[1];
		street = attributes[2].replace("\\/", "-");
		latitude = Double.parseDouble(attributes[4]);
		longitude = Double.parseDouble(attributes[5]);
		
		nBikes = 0;
		maxSlots = 0;
		nBrokenBikes = 0;
		
		for (char c : attributes[6].toCharArray())
		{
			if(c == '4')
			{
				nBikes++;
				maxSlots++;
			}
			else if(c == '5')
			{
				nBikes++;
				nBrokenBikes++;
				maxSlots++;
			}
			else if(c == '0')
			{
				maxSlots++;
			}
		}
	}

	public Station()
	{
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getnBikes() {
		return nBikes;
	}

	public void setnBikes(int nBikes) {
		this.nBikes = nBikes;
	}

	public int getMaxSlots() {
		return maxSlots;
	}

	public void setMaxSlots(int maxSlots) {
		this.maxSlots = maxSlots;
	}

	public int getnBrokenBikes() {
		return nBrokenBikes;
	}

	public void setnBrokenBikes(int nBrokenBikes) {
		this.nBrokenBikes = nBrokenBikes;
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

	public int getReportsNumber() {
		return reportsNumber;
	}

	public void setReportsNumber(int reportsNumber) {
		this.reportsNumber = reportsNumber;
	}

	@Override
	public String toString() {
		return "Station [name=" + name + ", street=" + street + ", id=" + id
				+ ", nBikes=" + nBikes + ", maxSlots=" + maxSlots
				+ ", nBrokenBikes=" + nBrokenBikes + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", reportsNumber="
				+ reportsNumber + "]";
	}

}
