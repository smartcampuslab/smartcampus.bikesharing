package eu.trentorise.smartcampus.bikesharing.managers;

public class Container<T>
{
	private int httpStatus;
	private String errorString;
	private T data;
	
	public Container() {
		super();
	}

	public Container(int httpStatus, String error, T data)
	{
		this.httpStatus = httpStatus;
		this.errorString = error;
		this.data = data;
	}

	@Override
	public String toString() {
		return "Container [httpStatus=" + httpStatus + ", error=" + errorString + ", data=" + data + "]";
	}

	public int getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(int httpStatus) {
		this.httpStatus = httpStatus;
	}

	public String getErrorString() {
		return errorString;
	}

	public void setErrorString(String errorString) {
		this.errorString = errorString;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}