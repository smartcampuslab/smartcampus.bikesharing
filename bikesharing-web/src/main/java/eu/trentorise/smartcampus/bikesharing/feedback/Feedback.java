package eu.trentorise.smartcampus.bikesharing.feedback;

import java.util.Arrays;

import org.springframework.data.annotation.Id;

public class Feedback
{
	@Id
	private String id;
	
	private long date;
	private String cityId;
	private String objectType;
	private String objectId;
	private String reportType;
	private String report;
	private String[] warnings;
	private String fileId;
	
	
	
	public Feedback() {
		super();
	}

	
	
	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public long getDate() {
		return date;
	}



	public void setDate(long date) {
		this.date = date;
	}



	public String getCityId() {
		return cityId;
	}



	public void setCityId(String cityId) {
		this.cityId = cityId;
	}



	public String getObjectType() {
		return objectType;
	}



	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}



	public String getObjectId() {
		return objectId;
	}



	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}



	public String getReportType() {
		return reportType;
	}



	public void setReportType(String reportType) {
		this.reportType = reportType;
	}



	public String getReport() {
		return report;
	}



	public void setReport(String report) {
		this.report = report;
	}



	public String[] getWarnings() {
		return warnings;
	}



	public void setWarnings(String[] warnings) {
		this.warnings = warnings;
	}



	public String getFileId() {
		return fileId;
	}



	public void setFileId(String fileId) {
		this.fileId = fileId;
	}



	@Override
	public String toString() {
		return "Feedback [id=" + id + ", date=" + date + ", cityId=" + cityId
				+ ", objectType=" + objectType + ", objectId=" + objectId
				+ ", reportType=" + reportType + ", report=" + report
				+ ", warnings=" + Arrays.toString(warnings) + ", fileId="
				+ fileId + "]";
	}
}
