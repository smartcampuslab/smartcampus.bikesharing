package eu.trentorise.smartcampus.bikesharing.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
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
//	@Autowired
//	private Log requestsLog;
	
	@Autowired
	private DataManager dataManager;
	
	@Autowired
	private FeedbackManager feedBackManager;
	
	@RequestMapping(value = "/stations/{cityId:.*}/{stationId:.*}", method = RequestMethod.GET)
    public @ResponseBody Container<Station> stationService(HttpServletRequest request, @PathVariable String cityId, @PathVariable String stationId)
    {
		int httpStatus = HttpStatus.OK.value();
		String errorString =  "";
		Station station = null;
		
		try
		{
			station = dataManager.getStation(cityId, stationId);
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
		
//		requestsLog.printRequest(request, errorString);

        return new Container<Station>(httpStatus, errorString, station);
    }
	
	@RequestMapping(value = "/stations/{cityId:.*}", method = RequestMethod.GET)
    public @ResponseBody Container<List<Station>> stationsService(HttpServletRequest request, @PathVariable String cityId)
    {
		int httpStatus = HttpStatus.OK.value();
		String errorString = "";
		Map<String, Station> stations = null;
		
		try
		{
			stations = dataManager.getStations(cityId);
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
		
//		requestsLog.printRequest(request, errorString);

        return new Container<List<Station>>(httpStatus, errorString, new ArrayList<Station>(stations.values()));
    }
	
	@RequestMapping(value = "/bikes/{cityId:.*}", method = RequestMethod.GET)
    public @ResponseBody Container<List<AnarchicBike>> anarchicBikesService(HttpServletRequest request, @PathVariable String cityId)
    {
		int httpStatus = HttpStatus.OK.value();
		String errorString =  "";
		Map<String, AnarchicBike> bikes = null;
		
		try
		{
			bikes = dataManager.getAnarchicBikes(cityId);
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
		
//		requestsLog.printRequest(request, errorString);
		
		return new Container<List<AnarchicBike>>(httpStatus, errorString, new ArrayList<AnarchicBike>(bikes.values()));
    }
    
    @RequestMapping(value = "/report", method = RequestMethod.POST)
    public @ResponseBody Container<Void> reportService(HttpServletRequest request, @RequestParam(value = "body") String feedback, @RequestParam(required=false,value="file") MultipartFile file)
    {
		int httpStatus = HttpStatus.OK.value();
		String errorString = "";
		
		byte[] byteArray = null;
		
		if(file != null)
		{
			try
			{
				byteArray = file.getBytes();
			}
			catch (IOException e)
			{
				e.printStackTrace();
				httpStatus = HttpStatus.NOT_ACCEPTABLE.value();
				errorString = HttpStatus.NOT_ACCEPTABLE.getReasonPhrase() + ": Cannot read image, " + e.getMessage();
			}
		}
		
		try
		{
			feedBackManager.addNewFeedback(feedback, byteArray);
		}
		catch (DataAccessResourceFailureException e)
		{
			e.printStackTrace();
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR.value();
			errorString = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase() + ": Disconnected database, " + e.getMessage();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			httpStatus = HttpStatus.NOT_ACCEPTABLE.value();
			errorString = HttpStatus.NOT_ACCEPTABLE.getReasonPhrase() + ": Wrong json format, " + e.getMessage();
		}
		
//		requestsLog.printRequest(request, errorString);

		return new Container<Void>(httpStatus, errorString, null);
    }
    
    @RequestMapping(value = "/stations/{cityId:.*}/{stationId:.*}/reports")
    public @ResponseBody Container<List<Feedback>> stationsReportService(HttpServletRequest request, @PathVariable String cityId, @PathVariable String stationId)
    {
		int httpStatus = HttpStatus.OK.value();
		String errorString = "";
		
		List<Feedback> res = feedBackManager.getStationFeedbacks(cityId, stationId);
		
//		requestsLog.printRequest(request, errorString);

        return new Container<List<Feedback>>(httpStatus, errorString, res);
    }
    
    @RequestMapping(value = "/bikes/{cityId:.*}/{bikeId:.*}/reports")
    public @ResponseBody Container<List<Feedback>> anarchicBikesReportService(HttpServletRequest request, @PathVariable String cityId, @PathVariable String bikeId)
    {
		int httpStatus = HttpStatus.OK.value();
		String errorString = "";
		
		List<Feedback> res = feedBackManager.getBikeFeedbacks(cityId, bikeId);

//		requestsLog.printRequest(request, errorString);
		
        return new Container<List<Feedback>>(httpStatus, errorString, res);
    }
}