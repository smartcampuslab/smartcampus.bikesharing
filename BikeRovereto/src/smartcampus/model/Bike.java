package smartcampus.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
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
		dest.writeString(id);
	}

	// getters and setters
	public GeoPoint getPosition()
	{
		return new GeoPoint(position.getLatitudeE6(), position.getLongitudeE6());
	}


}
