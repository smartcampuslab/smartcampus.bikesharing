package eu.trentorise.smartcampus.bikesharing.exceptions;

public class WebServiceErrorException extends Exception
{
	public WebServiceErrorException(String msg)
	{
		super("WebServiceErrorException: " + msg);
	}
}
