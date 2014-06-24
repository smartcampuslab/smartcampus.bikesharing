package eu.trentorise.smartcampus.bikesharing.exceptions;

import java.util.Arrays;

public class InvalidCityIDException extends Exception
{
	public InvalidCityIDException(String msg)
	{
		super("InvalidCityIDException: " + msg);
	}
}
