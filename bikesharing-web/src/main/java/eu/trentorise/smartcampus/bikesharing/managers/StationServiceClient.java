package eu.trentorise.smartcampus.bikesharing.managers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import c.inetub.wwwroot.webservice.service.TOBikeUtente;
import c.inetub.wwwroot.webservice.service.TOBikeUtenteSoap;
import eu.trentorise.smartcampus.bikesharing.exceptions.WebServiceErrorException;
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
	
	public Map<String, Station> getElencoStazioniPerComuneJSON(String cityID) throws WebServiceErrorException
	{
		
		Map<String, Station> stations = new HashMap<String, Station>();
		
		String strStations = port.elencoStazioniPerComuneJSON(username, password, cityID);
		
		if(strStations == null) throw new WebServiceErrorException("No data for ID: " + cityID);
		if(strStations.equals("")) throw new WebServiceErrorException("No data for ID: " + cityID);
		
		String[] strArrStations = strStations.replace("\"","").split("\\|");
		
		for(int i = 0; i < strArrStations.length; i++)
		{
			Station s = new Station(strArrStations[i]);
			stations.put(s.getId(), s);
		}
		return stations;
	}
}
