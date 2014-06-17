package smartcampus.model;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import eu.trentorise.smartcampus.osm.android.util.BoundingBoxE6;
import eu.trentorise.smartcampus.osm.android.util.GeoPoint;

public class Station implements Parcelable
{
	private GeoPoint position;
	private String name, street;

	private int nBikes;
	private int maxSlots;
	
	private int nReports;
	private ArrayList<String> reports;
	public static final int DISTANCE_NOT_VALID = -1;
	private int distance=DISTANCE_NOT_VALID; //>=0 only when distance is initialized
	
	public Station(GeoPoint position, String nameAndStreet, int maxSlots)
	{
		this.position = position;
		this.name = nameAndStreet.split("-")[0].trim();
		this.street = nameAndStreet.split("-")[1].trim();
		this.maxSlots = maxSlots;
		this.nBikes = 0;
		this.nReports = 0;
		reports = new ArrayList<String>();
	}

	public static BoundingBoxE6 getBoundingBox(ArrayList<Station> stations)
	{
		// the four edges of the bounding box

		int north = Integer.MIN_VALUE;
		int south = Integer.MAX_VALUE;
		int west = Integer.MAX_VALUE;
		int east = Integer.MIN_VALUE;

		for (Station s : stations)
		{
			if (s.getPosition().getLatitudeE6() > north)
			{
				north = s.getPosition().getLatitudeE6();
			}
			else if (s.getPosition().getLatitudeE6() < south)
			{
				south = s.getPosition().getLatitudeE6();
			}

			if (s.getPosition().getLongitudeE6() > east)
			{
				east = s.getPosition().getLongitudeE6();
			}
			else if (s.getPosition().getLongitudeE6() < west)
			{
				west = s.getPosition().getLongitudeE6();
			}
		}
		return new BoundingBoxE6(north, east, south, west);
	}

	// parcelable stuff
	public Station(Parcel source)
	{
		position = new GeoPoint(source.readInt(), source.readInt());
		distance = source.readInt();
		name = source.readString();
		street = source.readString();
		nBikes = source.readInt();
		maxSlots = source.readInt();
		nReports = source.readInt();
		reports = source.createStringArrayList();
	}

	public static final Parcelable.Creator<Station> CREATOR = new Creator<Station>()
	{

		@Override
		public Station[] newArray(int size)
		{
			return new Station[size];
		}

		@Override
		public Station createFromParcel(Parcel source)
		{
			return new Station(source);
		}
	};

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		// Log.d("parcelable", Double.toString(position.getLongitudeE6()));
		// Log.d("parcelable", Double.toString(position.getLongitudeE6()));
		// Log.d("parcelable", "-----");

		dest.writeInt(position.getLatitudeE6());
		dest.writeInt(position.getLongitudeE6());
		dest.writeInt(distance);
		dest.writeString(name);
		dest.writeString(street);
		dest.writeInt(nBikes);
		dest.writeInt(maxSlots);
		dest.writeInt(nReports);
		dest.writeStringList(reports);
	}

	// getters and setters
	public GeoPoint getPosition()
	{
		return new GeoPoint(position.getLatitudeE6(), position.getLongitudeE6());
	}
	
	public double getLatitudeDegree(){
		return position.getLatitudeE6() / 1E6;
	}
	public double getLongitudeDegree(){
		return position.getLongitudeE6() / 1E6;
	}

	public String getName()
	{
		return name;
	}

	public String getStreet()
	{
		return street;
	}

	public int getMaxSlots()
	{
		return maxSlots;
	}

	public int getUnavailableSlots()
	{
		return 0; // TODO: add unavailable slots
	}
	
	

	public double getBikesPresentPercentage()
	{
		if (nBikes == 0)
		{
			return 0;
		}
		return (double) nBikes / maxSlots;
	}

	public void setUsedSlots(int usedSlots)
	{

		if (usedSlots > maxSlots)
		{
			throw new RuntimeException("slots out of bounds");
		}
		this.nBikes = usedSlots;
	}

	public int getNSlotsUsed()
	{
		return nBikes; //Bici presenti nella stazione
	}

	public int getNSlotsEmpty()
	{
		return maxSlots - nBikes;  //Bici mancanti nella stazione
	}
	
	public void addReport(String report){
		reports.add(report);
		nReports++;
	}
	public String getReport(int position){
		return reports.get(position);
	}
	public int getNReports(){
		return nReports;
	}
	public ArrayList<String> getReports(){
		return reports;
	}
	public int getDistance()
	{
		return distance;
	}
	public void setDistance(int distance)
	{
		this.distance=distance;
	}
	
}
