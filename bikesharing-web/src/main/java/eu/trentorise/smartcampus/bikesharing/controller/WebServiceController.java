package eu.trentorise.smartcampus.bikesharing.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import eu.trentorise.smartcampus.bikesharing.exceptions.InvalidCityIDException;
import eu.trentorise.smartcampus.bikesharing.exceptions.InvalidStationIDException;
import eu.trentorise.smartcampus.bikesharing.exceptions.WebServiceErrorException;
import eu.trentorise.smartcampus.bikesharing.feedback.Feedback;
import eu.trentorise.smartcampus.bikesharing.feedback.FeedbackManager;
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
	private FeedbackManager feedBackManager;
	
	private ObjectMapper mapper = new ObjectMapper();
	
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
    public @ResponseBody Container<Integer> reportService(@RequestParam("body") String body, @RequestParam(required=false,value="file") MultipartFile file)
    {
		int httpStatus = HttpStatus.OK.value();
		String errorString = "";
		

		try
		{
			byte[] byteArray = null;
			
			if(file != null)
			{
				byteArray = file.getBytes();
			}
			
			feedBackManager.addNewFeedback(mapper.readValue(body, Feedback.class), byteArray);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			httpStatus = HttpStatus.NOT_ACCEPTABLE.value();
			errorString = HttpStatus.NOT_ACCEPTABLE.getReasonPhrase() + ": " + e.getMessage();
		}
		
		return new Container<Integer>(httpStatus, errorString, 0);
    }
    
    @RequestMapping(value = "/stations/{cityID:.*}/{stationID:.*}/report")
    public @ResponseBody Container<Feedback[]> stationsReportService(@PathVariable String cityID, @PathVariable String stationID)
    {
		int httpStatus = HttpStatus.OK.value();
		String errorString = "";
		List<Feedback> res = null;
		
		try
		{
			res = feedBackManager.getStationFeedback(cityID, stationID);
		}
		catch(Exception e)
		{
			
		}
		
        return new Container<Feedback[]>(httpStatus, errorString, (Feedback[]) res.toArray());
    }
    
    @RequestMapping(value = "/bikes/{cityID:.*}/{bikeID:.*}/report")
    public @ResponseBody Container<Feedback[]> anarchicBikesReportService(@PathVariable String cityID, @PathVariable String bikeID)
    {
		int httpStatus = HttpStatus.OK.value();
		String errorString = "";
		List<Feedback> res = null;
		
		try
		{
			res = feedBackManager.getBikeFeedback(cityID, bikeID);
	    }
		catch(Exception e)
		{
			
		}
		
        return new Container<Feedback[]>(httpStatus, errorString, (Feedback[]) res.toArray());
    }
    
    @RequestMapping(value = "/feedback")
    public @ResponseBody Feedback feedback()
    {
        return new Feedback();
    }
}