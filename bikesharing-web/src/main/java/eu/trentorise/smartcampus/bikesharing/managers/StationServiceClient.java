package eu.trentorise.smartcampus.bikesharing.managers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import c.inetub.wwwroot.webservice.service.TOBikeUtente;
import c.inetub.wwwroot.webservice.service.TOBikeUtenteSoap;
import eu.trentorise.smartcampus.bikesharing.exceptions.WebServiceErrorException;
import eu.trentorise.smartcampus.bikesharing.feedback.FeedbackManager;
import eu.trentorise.smartcampus.bikesharing.model.Station;

@Component
public class StationServiceClient
{
	private TOBikeUtente service;
	private TOBikeUtenteSoap port;
	
	@Value("${tobike.username}")
	private String username;
	@Value("${tobike.password}")
	private String password;
	
	@Autowired
	private FeedbackManager feedBackManager;
	
	public StationServiceClient()
	{
		service = new TOBikeUtente();
		port = service.getTOBikeUtenteSoap();
	}

	public TOBikeUtente getService()
	{
		return service;
	}
	
	public TOBikeUtenteSoap getPort()
	{
		return port;
	}
	
	public Map<String, Station> getElencoStazioniPerComuneJSON(String cityId) throws WebServiceErrorException
	{
		
		Map<String, Station> stations = new HashMap<String, Station>();
		
		String strStations = port.elencoStazioniPerComuneJSON(username, password, cityId);
		
		if(strStations == null) throw new WebServiceErrorException("No data for ID: " + cityId);
		if(strStations.equals("")) throw new WebServiceErrorException("No data for ID: " + cityId);
		
		String[] strArrStations = strStations.replace("\"","").split("\\|");
		
		for(int i = 0; i < strArrStations.length; i++)
		{
			//create the station from the string
			Station s = new Station(strArrStations[i]);
			
			//Android application needs to know how many reports have been made for every station
			s.setReportsNumber(feedBackManager.getStationFeedbacks(cityId, s.getId()).size());
			
			//put "station" inside "stations" map
			stations.put(s.getId(), s);
		}
		return stations;
	}
}
