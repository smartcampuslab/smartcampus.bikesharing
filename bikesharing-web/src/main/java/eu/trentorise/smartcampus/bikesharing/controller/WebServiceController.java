package eu.trentorise.smartcampus.bikesharing.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import eu.trentorise.smartcampus.bikesharing.exceptions.InvalidCityIDException;
import eu.trentorise.smartcampus.bikesharing.exceptions.InvalidStationIDException;
import eu.trentorise.smartcampus.bikesharing.exceptions.WebServiceErrorException;
import eu.trentorise.smartcampus.bikesharing.feedback.FeedBack;
import eu.trentorise.smartcampus.bikesharing.feedback.FeedBackManager;
import eu.trentorise.smartcampus.bikesharing.managers.Container;
import eu.trentorise.smartcampus.bikesharing.managers.DataManager;
import eu.trentorise.smartcampus.bikesharing.model.AnarchicBike;
import eu.trentorise.smartcampus.bikesharing.model.Station;
 
@Controller
public class WebServiceController
{
	@Autowired
	private DataManager dataManager;
	
	@Autowired
	private FeedBackManager feedBackManager;
	
	@RequestMapping(value = "/stations/{cityID:.*}/{stationID:.*}")
    public @ResponseBody Container<Station> stationService(@PathVariable String cityID, @PathVariable String stationID)
    {
		int httpStatus = HttpStatus.OK.value();
		String errorString =  "";
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
	
	@RequestMapping(value = "/stations/{cityID:.*}")
    public @ResponseBody Container<Object[]> stationsService(@PathVariable String cityID)
    {
		int httpStatus = HttpStatus.OK.value();
		String errorString = "";
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
	
	@RequestMapping(value = "/bikes/{cityID:.*}")
    public @ResponseBody Container<Object[]> anarchicBikesService(@PathVariable String cityID)
    {
		int httpStatus = HttpStatus.OK.value();
		String errorString =  "";
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
    
    @RequestMapping(value = "/report", method = RequestMethod.POST)
    public @ResponseBody Container<Integer> reportService(@RequestBody FeedBack feedBack, @RequestParam(required=false,value="file") MultipartFile file)
    {
		int httpStatus = HttpStatus.OK.value();
		String errorString = "";
		
		feedBackManager.addNewFeedBack(feedBack, file);
		
		return new Container<Integer>(httpStatus, errorString, 0);
    }
    
    @RequestMapping(value = "/stations/{cityID:.*}/{stationID:.*}/report")
    public @ResponseBody Container<FeedBack> stationsReportService(@PathVariable String cityID, @PathVariable String stationID)
    {
		int httpStatus = HttpStatus.OK.value();
		String errorString = "";
		
		FeedBack res = new FeedBack(null, 0L, "Station", "1144", "Malfunction", "Colonnina 3 malfunzionante", null);//feedBackManager.getFeedBacks(cityID, stationID)
		
        return new Container<FeedBack>(httpStatus, errorString, res);
    }
    
    @RequestMapping(value = "/bikes/{cityID:.*}/{bikeID:.*}/report")
    public @ResponseBody Container<FeedBack> anarchicBikesReportService(@PathVariable String cityID, @PathVariable String bikeID)
    {
		int httpStatus = HttpStatus.OK.value();
		String errorString = "";
		
		FeedBack res = new FeedBack(null, 0L, "Bike", "0001", "Malfunction", "Gomma forata", null);//feedBackManager.getFeedBacks(cityID, bikeID)
		
        return new Container<FeedBack>(httpStatus, errorString, res);
    }
    
    /*@RequestMapping(value = "/feedback")
    public @ResponseBody FeedBack feedback()
    {
        return new FeedBack();
    }*/
}