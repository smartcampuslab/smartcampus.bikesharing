package eu.trentorise.smartcampus.bikesharing.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import eu.trentorise.smartcampus.bikesharing.exceptions.WebServiceErrorException;
import eu.trentorise.smartcampus.bikesharing.feedback.FeedbackManager;
import eu.trentorise.smartcampus.bikesharing.model.Station;
import eu.trentorise.smartcampus.mobilityservice.MobilityDataService;
import eu.trentorise.smartcampus.network.JsonUtils;
import eu.trentorise.smartcampus.network.RemoteConnector;
import eu.trentorise.smartcampus.network.RemoteException;

@Component
public class StationServiceClient
{
	@Autowired
	private FeedbackManager feedBackManager;

	@Autowired
	@Value("${core.mobility.url}")
	private String mobilityUrl;
	
	@Autowired
	@Value("${agencies}")
	private String agencies;
	
	private MobilityDataService mobilityDataService;

	private Map<String,String> agencyMap = new HashMap<String, String>();
	
	@PostConstruct
	public void init() {
		mobilityDataService = new MobilityDataService(mobilityUrl);
		String[] agencyPairs = agencies.split(",");
		for (String pairStr : agencyPairs) {
			String[] pair = pairStr.split(":");
			agencyMap.put(pair[0].trim(), pair[1].trim());
		}
	}

	public Map<String, Station> getElencoStazioniPerComuneJSON(String cityId) throws WebServiceErrorException
	{
		String agencyId = agencyMap.get(cityId);
		if (agencyId == null) return Collections.emptyMap();
//		List<Parking> bikeSharings = null;
//		try {
//			bikeSharings = mobilityDataService.getBikeSharings(agencyId, null);
//		} catch (MobilityServiceException e) {
//			throw new WebServiceErrorException(e.getMessage());
//		}
//		
		Map<String, Station> stations = new HashMap<String, Station>();
		
		List<Station> bikeSharings = null;
		try {
			bikeSharings = readStations(mobilityUrl, cityId);
		} catch (Exception e) {
			throw new WebServiceErrorException(e.getMessage());
		}
 		if(bikeSharings == null || bikeSharings.isEmpty()) throw new WebServiceErrorException("No data for ID: " + cityId);
		
		for(Station s : bikeSharings)
		{
			s.setReportsNumber(feedBackManager.getStationFeedbacks(cityId, s.getId()).size());
			stations.put(s.getId(), s);
		}
		return stations;
	}
	
	@SuppressWarnings("rawtypes")
	private static List<Station> readStations(String url, String city) throws SecurityException, RemoteException {
		List<Station> result = new ArrayList<Station>();
		String json = RemoteConnector.getJSON(url, "/bikesharing/"+city, null);
		List<Map> objectList = JsonUtils.toObjectList(json, Map.class);
		for (Map map: objectList) {
			Station s = new Station();
			s.setName((String)map.get("name"));
			s.setId((String)map.get("id"));
			List<Double> position = (List<Double>) map.get("position");
			s.setLatitude(position.get(0));
			s.setLongitude(position.get(1));
			s.setMaxSlots((Integer) map.get("totalSlots"));
			s.setnBikes((Integer)map.get("bikes"));
			int slots = (Integer) map.get("slots");
			s.setnBrokenBikes(Math.max(s.getMaxSlots() - s.getnBikes()-slots, 0));
			s.setStreet((String) map.get("address"));
			result.add(s);
		}
		return result;
	}
}
