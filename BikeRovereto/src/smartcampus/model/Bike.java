package smartcampus.model;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;
import eu.trentorise.smartcampus.osm.android.util.BoundingBoxE6;
import eu.trentorise.smartcampus.osm.android.util.GeoPoint;

public class Bike implements Parcelable
{

	private GeoPoint position;

	private String id;

	private int nReports;
	private ArrayList<String> reports;
	public static final int DISTANCE_NOT_VALID = -1;
	private int distance = DISTANCE_NOT_VALID; // >=0 only when distance is
												// initialized

	public Bike(GeoPoint position, String id)
	{
		this.position = position;
		this.id = id;
		this.nReports = 0;
		reports = new ArrayList<String>();
	}

	// parcelable stuff
	public Bike(Parcel source)
	{
		position = new GeoPoint(source.readInt(), source.readInt());
		id = source.readString();
		nReports = source.readInt();
		reports = source.createStringArrayList();
	}

	public static final Parcelable.Creator<Bike> CREATOR = new Creator<Bike>()
	{

		@Override
		public Bike[] newArray(int size)
		{
			return new Bike[size];
		}

		@Override
		public Bike createFromParcel(Parcel source)
		{
			return new Bike(source);
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

		dest.writeInt(position.getLatitudeE6());
		dest.writeInt(position.getLongitudeE6());
		dest.writeString(id);
		dest.writeInt(nReports);
		dest.writeStringList(reports);
	}

	// getters and setters
	public GeoPoint getPosition()
	{
		return new GeoPoint(position.getLatitudeE6(), position.getLongitudeE6());
	}

	public String getId()
	{
		return id;
	}

	public static BoundingBoxE6 getBoundingBox(ArrayList<Bike> bikes)
	{
		// the four edges of the bounding box

		int north = Integer.MIN_VALUE;
		int south = Integer.MAX_VALUE;
		int west = Integer.MAX_VALUE;
		int east = Integer.MIN_VALUE;

		for (Bike b : bikes)
		{
			if (b.getPosition().getLatitudeE6() > north)
			{
				north = b.getPosition().getLatitudeE6();
			}
			else if (b.getPosition().getLatitudeE6() < south)
			{
				south = b.getPosition().getLatitudeE6();
			}

			if (b.getPosition().getLongitudeE6() > east)
			{
				east = b.getPosition().getLongitudeE6();
			}
			else if (b.getPosition().getLongitudeE6() < west)
			{
				west = b.getPosition().getLongitudeE6();
			}
		}
		return new BoundingBoxE6(north, east, south, west);
	}

	public void addReport(String report)
	{
		reports.add(report);
		nReports++;
	}

	public String getReport(int position)
	{
		return reports.get(position);
	}

	public int getNReports()
	{
		return nReports;
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

	public double getLatitudeDegree()
	{
		return position.getLatitudeE6() / 1E6;
	}

	public double getLongitudeDegree()
	{
		return position.getLongitudeE6() / 1E6;
	}
}
