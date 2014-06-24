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

	public String getName()
	{
		return name;
	}

	public String getStreet()
	{
		return street;
	}

	public String getId()
	{
		return id;
	}

	public int getnBikes()
	{
		return nBikes;
	}

	public int getMaxSlots()
	{
		return maxSlots;
	}

	public double getLatitude()
	{
		return latitude;
	}

	public double getLongitude()
	{
		return longitude;
	}

	public int getnBrokenBikes()
	{
		return nBrokenBikes;
	}

	public void setnBrokenBikes(int nBrokenBikes)
	{
		this.nBrokenBikes = nBrokenBikes;
	}

	@Override
	public String toString()
	{
		return "\nStation [name=" + name + ", street=" + street + ", id=" + id + ", nBikes=" + nBikes + ", maxSlots=" + maxSlots + ", nBrokenBikes=" + nBrokenBikes + ", latitude=" + latitude + ", longitude=" + longitude + "]";
	}

	
	
}
