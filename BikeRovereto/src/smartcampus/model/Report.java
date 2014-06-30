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
	public static final String STATION = "stations"; //they are useful for the URL
	public static final String BIKE = "bikes";
	

	private Type type;
	private String details;
	private Bitmap photo;
	private String reportOfType;
	private String id;

	public Report(Type type, String details, Bitmap photo, String typeOf, String id)
	{
		this.type = type;
		this.details = details;
		this.photo = photo;
		this.reportOfType = typeOf;
		this.id = id;
	}
	
	public Report()
	{
		
	}
	public Report(Type type, String details, String typeOf, String id)
	{
		this(type, details, null, typeOf, id);
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
	
	public String getReportOfType()
	{
		return reportOfType;
	}
	
	public String getID()
	{
		return id;
	}

	public void setPhoto(Bitmap photo)
	{
		this.photo = photo;
	}
	
	public void setType(Type type)
	{
		this.type = type;
	}
	
	public void setDetails(String details)
	{
		this.details = details;
	}
	// parcelable stuff
	public Report(Parcel source)
	{
		details = source.readString();
		type = Type.values()[source.readInt()];
		photo = source.readParcelable(null);
		reportOfType = source.readString();
		id = source.readString();
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
		dest.writeString(reportOfType);
		dest.writeString(id);
	}
	
	public String toString()
	{
		return type.toString() + " " + details + " " + (photo != null? "photo" : "no photo");
	}

}
