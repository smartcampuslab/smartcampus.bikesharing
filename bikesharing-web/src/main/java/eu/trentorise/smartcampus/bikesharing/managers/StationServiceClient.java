package eu.trentorise.smartcampus.bikesharing.managers;

import it.sayservice.platform.smartplanner.data.message.otpbeans.Parking;

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
import eu.trentorise.smartcampus.mobilityservice.MobilityServiceException;

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
		List<Parking> bikeSharings = null;
		try {
			bikeSharings = mobilityDataService.getBikeSharings(agencyId, null);
		} catch (MobilityServiceException e) {
			throw new WebServiceErrorException(e.getMessage());
		}
		
		Map<String, Station> stations = new HashMap<String, Station>();
		
		if(bikeSharings == null || bikeSharings.isEmpty()) throw new WebServiceErrorException("No data for ID: " + cityId);
		
		for(Parking parking : bikeSharings)
		{
			//create the station from the string
			Station s = new Station();
			s.setName(parking.getName());
			s.setId(parking.getName());
			s.setLatitude(parking.getPosition()[0]);
			s.setLongitude(parking.getPosition()[1]);
			if (parking.isMonitored()) {
				s.setMaxSlots(parking.getSlotsTotal());
				s.setnBikes((Integer)parking.getExtra().get("bikes"));
				s.setnBrokenBikes(Math.max(s.getMaxSlots() - s.getnBikes()-parking.getSlotsAvailable(), 0));
			} else {
				s.setMaxSlots(0);
				s.setnBikes(0);
				s.setnBrokenBikes(0);
			}
			
			s.setStreet(parking.getDescription());
			s.setReportsNumber(feedBackManager.getStationFeedbacks(cityId, s.getId()).size());
			
			stations.put(s.getId(), s);
		}
		return stations;
	}
}
