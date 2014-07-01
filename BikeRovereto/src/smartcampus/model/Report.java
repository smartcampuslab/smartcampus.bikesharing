package smartcampus.model;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

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
		public static Type stringToType(String typeString)
		{
			if (typeString == "advice")
				return ADVICE;
			else if (typeString == "complaint")
				return COMPLAINT;
			else if (typeString == "warning")
				return WARNING;
			else
				throw new IllegalArgumentException();			
		}
	}
	public static final String STATION = "station";
	public static final String BIKE = "bike";
	

	private Type type;
	private String details;
	private Bitmap photo;
	private String reportOfType;
	private String id;
	private long date;
	private ArrayList<String> warnings;
	public static final String CHAIN = "chain", GEARS = "gears", BRAKES = "brakes", TIRE = "tire";
	public static final String[] WARNINGS = new String[] {CHAIN, GEARS, BRAKES, TIRE};

	public Report(Type type, String details, Bitmap photo, String typeOf, String id, long date)
	{
		this.type = type;
		this.details = details;
		this.photo = photo;
		this.reportOfType = typeOf;
		this.id = id;
		this.date = date;
	}
	
	public Report()
	{
		
	}
	public Report(String typeOf, String id, long date)
	{
		
		this.reportOfType = typeOf;
		this.id = id;
		this.date = date;
	}
	public Report(Type type, String details, String typeOf, String id, long date)
	{
		this(type, details, null, typeOf, id, date);
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
	
	public long getDate() {
		return date;
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
		photo = Bitmap.CREATOR.createFromParcel(source); //TODO: bitmap parcel!
		reportOfType = source.readString();
		id = source.readString();
		date = source.readLong();
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
	    photo.writeToParcel(dest, 0);
	    dest.setDataPosition(0);
		dest.writeString(reportOfType);
		dest.writeString(id);
		dest.writeLong(date);
	}
	
	public String toString()
	{
		return type.toString() + " " + details + " " + (photo != null? "photo" : "no photo");
	}
	
	public ArrayList<String> getWarnings()
	{
		return warnings;
	}
	public void addWarning(String war)
	{
		if (warnings == null) warnings = new ArrayList<String>();
		warnings.add(war);
	}

}
