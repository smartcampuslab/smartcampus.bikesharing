package eu.trentorise.smartcampus.bikesharing.managers;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import eu.trentorise.smartcampus.bikesharing.exceptions.InvalidCityIDException;
import eu.trentorise.smartcampus.bikesharing.exceptions.InvalidStationIDException;
import eu.trentorise.smartcampus.bikesharing.exceptions.WebServiceErrorException;
import eu.trentorise.smartcampus.bikesharing.model.AnarchicBike;
import eu.trentorise.smartcampus.bikesharing.model.Station;

@Component
public class DataManager
{
	private LoadingCache<String, Map<String, Station>> stationsCache;
	private LoadingCache<String, Map<String, AnarchicBike>> anarchicBikesCache;
		
	@Autowired
	private StationServiceClient stationServiceClient;
	@Autowired
	private AnarchicBikeServiceClient anarchicBikeServiceClient;
	
	
	public DataManager()
	{		
		stationsCache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES).build(
			           new CacheLoader<String, Map<String, Station>>()
			           {
			             public Map<String, Station> load(String key) throws WebServiceErrorException
			             {
							 return stationServiceClient.getElencoStazioniPerComuneJSON(key);
			             }
			           });
		
		anarchicBikesCache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.SECONDS).build(
			           new CacheLoader<String,  Map<String, AnarchicBike>>()
			           {
			             public Map<String, AnarchicBike> load(String key) throws WebServiceErrorException
			             {
			            	 return anarchicBikeServiceClient.getAnarchicBikes(key);
						 }
			           });
	}
	
	public Map<String, Station> getStations(String cityID) throws InvalidCityIDException, WebServiceErrorException
	{
		if(cityID == null) throw new InvalidCityIDException("null cityID");
		if(cityID.equals("")) throw new InvalidCityIDException("empty cityID");
		
		Map<String, Station> res = null;
		
		try
		{
			res = stationsCache.get(cityID);
		}
		catch (ExecutionException e)
		{
			e.printStackTrace();
		}
		
		if (res == null)
		{
			throw new WebServiceErrorException("No data for cityID: " + cityID);
		}
		
		return res;
			
	}

	public Station getStation(String cityID, String stationID) throws InvalidCityIDException, InvalidStationIDException, WebServiceErrorException
	{
		if(stationID == null) throw new InvalidStationIDException("null stationID");
		if(stationID.equals("")) throw new InvalidStationIDException("empty stationID");
		
		Station res = getStations(cityID).get(stationID);
		
		if(res == null)
		{
			throw new InvalidStationIDException("No data for stationID: " + stationID);
		}
		
		return res;
	}
	
	public Map<String, AnarchicBike> getAnarchicBikes(String cityID) throws InvalidCityIDException, WebServiceErrorException
	{
		if(cityID == null) throw new InvalidCityIDException("null cityID");
		if(cityID.equals("")) throw new InvalidCityIDException("empty cityID");
		
		Map<String, AnarchicBike> res = null;
		
		try
		{
			res = anarchicBikesCache.get(cityID);
		}
		catch (ExecutionException e)
		{
			e.printStackTrace();
		}
		
		if (res == null)
		{
			throw new WebServiceErrorException("No data for cityID: " + cityID);
		}
		
		return res;
	}
}
