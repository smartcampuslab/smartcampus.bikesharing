package smartcampus.model;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
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
	}
	public static final String STATION = "station";
	public static final String BIKE = "bike";
	

	private Type type;
	private String details;
	private Bitmap photo;
	private String reportOfType;
	private String id;
	private long date;
	private ArrayList<String> warnings = new ArrayList<String>();
	public static final String CHAIN = "chain", GEARS = "gears", BRAKES = "brakes", TIRE = "tire";

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
	
	public byte[] getPhotoAsByteArray()
	{
		return bitmapAsByteArray(photo);
	}
	
	// parcelable stuff
	public Report(Parcel source)
	{
		details = source.readString();
		type = Type.values()[source.readInt()];
		reportOfType = source.readString();
		id = source.readString();
		date = source.readLong();
		byte[] imageByte = new byte[source.readInt()];
		source.readByteArray(imageByte);
		photo = byteArrayToBitmap(imageByte);
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
		dest.writeString(reportOfType);
		dest.writeString(id);
		dest.writeLong(date);
		byte[] byteArray = bitmapAsByteArray(photo);
		dest.writeInt(byteArray.length);
		dest.writeByteArray(byteArray);
	}
	
	public String toString()
	{
		return type.toString() + " " + details + " " + (photo != null? "photo" : "no photo");
	}
	
	private static byte[] bitmapAsByteArray(Bitmap bitmap) {
		if (bitmap != null) {
			ByteArrayOutputStream bais = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.PNG, 50, bais);
			return bais.toByteArray();
		}
		return null;
	}
	
	private static Bitmap byteArrayToBitmap(byte[] byteArray)
	{
		return BitmapFactory.decodeByteArray(byteArray, 0,byteArray.length);
	}

	public ArrayList<String> getWarnings() {
		return warnings;
	}

	public void addWarning(String war){
		warnings.add(war);
	}

}
