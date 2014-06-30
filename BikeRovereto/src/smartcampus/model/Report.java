package smartcampus.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Report implements Parcelable
{

	public enum Type
	{
		ADVICE, COMPLAINT, WARNING;

		@Override
		public String toString()
		{
			switch (this)
			{
			case ADVICE:
				return "advice";
			case COMPLAINT:
				return "complaint";
			case WARNING:
				return "warning";
			default:
				throw new IllegalArgumentException();
			}
		}
	}

	Type type;
	String details;
	Bitmap photo;

	public Report(Type type, String details, Bitmap photo)
	{
		this.type = type;
		this.details = details;
		this.photo = photo;
	}

	public Type getType()
	{
		return type;
	}

	public String getDetails()
	{
		return details;
	}

	public Bitmap getPhoto()
	{
		return photo;
	}

	// parcelable stuff
	public Report(Parcel source)
	{
		details = source.readString();
		type = Type.values()[source.readInt()];
		photo = source.readParcelable(null);
	}

	public static final Parcelable.Creator<Report> CREATOR = new Creator<Report>()
	{

		@Override
		public Report[] newArray(int size)
		{
			return new Report[size];
		}

		@Override
		public Report createFromParcel(Parcel source)
		{
			return new Report(source);
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
		dest.writeString(details);
	    dest.writeInt(type.ordinal());
		dest.writeParcelable(photo, CONTENTS_FILE_DESCRIPTOR);
	}

}
