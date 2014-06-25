package eu.trentorise.smartcampus.bikesharing.exceptions;

public class InvalidCityIDException extends Exception
{
	public InvalidCityIDException(String msg)
	{
		super("InvalidCityIDException: " + msg);
	}
}
