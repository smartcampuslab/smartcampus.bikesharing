package eu.trentorise.smartcampus.bikesharing.feedback;

import java.util.Arrays;

import org.springframework.data.annotation.Id;

public class Feedback
{
	@Id
	private String id;
	
	private long date;
	private String objectType;
	private String objectId;
	private String reportType;
	private String report;
	private String fileId;
	private String[] warnings;
	
	
	
	public Feedback(long date, String objectType, String objectId, String reportType, String report, String fileId, String[] warnings)
	{
		super();
		this.date = date;
		this.objectType = objectType;
		this.objectId = objectId;
		this.reportType = reportType;
		this.report = report;
		this.fileId = fileId;
	}

	public Feedback()
	{
		super();
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public long getDate()
	{
		return date;
	}

	public void setDate(long date)
	{
		this.date = date;
	}

	public String getObjectType()
	{
		return objectType;
	}

	public void setObjectType(String objectType)
	{
		this.objectType = objectType;
	}

	public String getObjectId()
	{
		return objectId;
	}

	public void setObjectId(String objectId)
	{
		this.objectId = objectId;
	}

	public String getReportType()
	{
		return reportType;
	}

	public void setReportType(String reportType)
	{
		this.reportType = reportType;
	}

	public String getReport()
	{
		return report;
	}

	public void setReport(String report)
	{
		this.report = report;
	}

	public String getFileId()
	{
		return fileId;
	}

	public void setFileId(String fileId)
	{
		this.fileId = fileId;
	}

	public String[] getWarnings() {
		return warnings;
	}

	public void setWarnings(String[] warnings) {
		this.warnings = warnings;
	}

	@Override
	public String toString()
	{
		return "Feedback [id=" + id + ", date=" + date + ", objectType="
				+ objectType + ", objectId=" + objectId + ", reportType="
				+ reportType + ", report=" + report + ", fileId=" + fileId
				+ ", warnings=" + Arrays.toString(warnings) + "]";
	}
}
