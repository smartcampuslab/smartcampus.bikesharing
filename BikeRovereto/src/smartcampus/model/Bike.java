package smartcampus.model;

import android.os.Parcel;
import android.os.Parcelable;
import eu.trentorise.smartcampus.osm.android.util.GeoPoint;

public class Bike implements Parcelable
{

	private GeoPoint position;

	private String id;

	public Bike(GeoPoint position, String id)
	{
		this.position = position;
		this.id = id;
	}

	// parcelable stuff
	public Bike(Parcel source)
	{
		position = new GeoPoint(source.readInt(), source.readInt());
		id = source.readString();
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
}
