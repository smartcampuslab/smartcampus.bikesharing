package eu.trentorise.smartcampus.bikesharing.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.bikesharing.exceptions.InvalidCityIDException;
import eu.trentorise.smartcampus.bikesharing.exceptions.InvalidStationIDException;
import eu.trentorise.smartcampus.bikesharing.exceptions.WebServiceErrorException;
import eu.trentorise.smartcampus.bikesharing.managers.Container;
import eu.trentorise.smartcampus.bikesharing.managers.DataManager;
import eu.trentorise.smartcampus.bikesharing.model.AnarchicBike;
import eu.trentorise.smartcampus.bikesharing.model.Station;
 
@Controller
public class WebServiceController
{
	@Autowired
	private DataManager dataManager;
	
	@RequestMapping("/stations/{cityID:.*}/{stationID:.*}")
    public @ResponseBody Container<Station> stationService(@PathVariable String cityID, @PathVariable String stationID)
    {
		int httpStatus = HttpStatus.OK.value();
		String errorString =  HttpStatus.OK.getReasonPhrase();
		Station station = null;
		
		try
		{
			station = dataManager.getStation(cityID, stationID);
		}
		catch (InvalidCityIDException e)
		{
			e.printStackTrace();
			httpStatus = HttpStatus.BAD_REQUEST.value();
			errorString =   HttpStatus.BAD_REQUEST.getReasonPhrase() + ": " + e.getMessage();
		}
		catch (InvalidStationIDException e)
		{
			e.printStackTrace();
			httpStatus = HttpStatus.BAD_REQUEST.value();
			errorString =  HttpStatus.BAD_REQUEST.getReasonPhrase() + ": " + e.getMessage();
		}
		catch (WebServiceErrorException e)
		{
			e.printStackTrace();
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR.value();
			errorString =  HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase() + ": " + e.getMessage();
		}
		
        return new Container<Station>(httpStatus, errorString, station);
    }
	@RequestMapping("/stations/{cityID:.*}")
    public @ResponseBody Container<Object[]> stationsService(@PathVariable String cityID)
    {
		int httpStatus = HttpStatus.OK.value();
		String errorString =  HttpStatus.OK.getReasonPhrase();
		Map<String, Station> stations = null;
		
		try
		{
			stations = dataManager.getStations(cityID);
		}
		catch (InvalidCityIDException e)
		{
			e.printStackTrace();
			httpStatus = HttpStatus.BAD_REQUEST.value();
			errorString =  HttpStatus.BAD_REQUEST.getReasonPhrase() + ": " + e.getMessage();
		}
		catch (WebServiceErrorException e)
		{
			e.printStackTrace();
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR.value();
			errorString =  HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase() + ": " + e.getMessage();
		}
		
        return new Container<Object[]>(httpStatus, errorString, stations.values().toArray());
    }
	
	@RequestMapping("/bikes/{cityID:.*}")
    public @ResponseBody Container<Object[]> anarchicBikesService(@PathVariable String cityID)
    {
		int httpStatus = HttpStatus.OK.value();
		String errorString =  HttpStatus.OK.getReasonPhrase();
		Map<String, AnarchicBike> bikes = null;
		
		try
		{
			bikes = dataManager.getAnarchicBikes(cityID);
		}
		catch (InvalidCityIDException e)
		{
			e.printStackTrace();
			httpStatus = HttpStatus.BAD_REQUEST.value();
			errorString =   HttpStatus.BAD_REQUEST.getReasonPhrase() + ": " + e.getMessage();
		}
		catch (WebServiceErrorException e)
		{
			e.printStackTrace();
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR.value();
			errorString =  HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase() + ": " + e.getMessage();
		}
		
		return new Container<Object[]>(httpStatus, errorString, bikes.values().toArray());
    }
}