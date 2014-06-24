package eu.trentorise.smartcampus.bikesharing.exceptions;

public class InvalidStationIDException extends Exception
{
	public InvalidStationIDException(String msg)
	{
		super("InvalidBikeIDException: " + msg);
	}
}
