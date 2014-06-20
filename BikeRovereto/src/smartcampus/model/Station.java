package smartcampus.model;

import java.util.ArrayList;

import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;

import android.os.Parcel;
import android.os.Parcelable;

public class Station implements Parcelable
{
	private GeoPoint position;
	private String name, street;

	private String id;

	
	private int nBikes;
	private int maxSlots;
	private boolean favourite;

	private ArrayList<String> reports;
	public static final int DISTANCE_NOT_VALID = -1;
	private int distance = DISTANCE_NOT_VALID; // >=0 only when distance is
												// initialized

	//TODO: 
	
	public Station(GeoPoint position, String name, String street, int maxSlots, int nBikes, int brokenSlots, String id)
	{
		this.position = position;
		this.name = name;
		this.street = street;
		this.maxSlots = maxSlots;
		this.nBikes = 0;
		this.id = id;
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
		dest.writeStringList(reports);
	}

	// getters and setters
	public GeoPoint getPosition()
	{
		return new GeoPoint(position.getLatitudeE6(), position.getLongitudeE6());
	}

	public double getLatitudeDegree()
	{
		return position.getLatitudeE6() / 1E6;
	}

	public double getLongitudeDegree()
	{
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
		return nBikes; // Bici presenti nella stazione
	}

	public int getNSlotsEmpty()
	{
		return maxSlots - nBikes; // Bici mancanti nella stazione
	}

	public void addReport(String report)
	{
		reports.add(report);
	}

	public String getReport(int position)
	{
		return reports.get(position);
	}

	public int getNReports()
	{
		return reports.size();
	}

	public ArrayList<String> getReports()
	{
		return reports;
	}

	public int getDistance()
	{
		return distance;
	}

	public void setDistance(int distance)
	{
		this.distance = distance;
	}
	
	public void setFavourite(boolean fav)
	{
		this.favourite=fav;
	}
	
	public boolean getFavourite()
	{
		return favourite;
	}
	public String getId()
	{
		return id;
	}

}
