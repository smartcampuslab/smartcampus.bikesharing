package smartcampus.model;

import java.util.ArrayList;

public interface Reportable
{
	String getName();
	void addReport(Report report);
	Report getReport(int index);
	int getNReports();
	ArrayList<Report> getReports();
	String getType();
	String getId();
}
