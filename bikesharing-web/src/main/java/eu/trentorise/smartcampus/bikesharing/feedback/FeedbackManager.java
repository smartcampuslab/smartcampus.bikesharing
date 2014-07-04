package eu.trentorise.smartcampus.bikesharing.feedback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Component;

import eu.trentorise.smartcampus.bikesharing.db.FeedbackRepository;

@Component
public class FeedbackManager
{

	@Autowired
	private FeedbackFileManager feedBackFileManager;
	
	@Autowired
	private FeedbackRepository feedbackRepository;
	
	//dataBase cache max time
	@Value("${dbCacheTime}")
	private String dbCacheTime;

	private ObjectMapper mapper = new ObjectMapper();
	
	public void addNewFeedback(String body, byte[] file) throws JsonParseException, JsonMappingException, IOException, DataAccessResourceFailureException
	{
		//convert "body" from UTF-8 charset to java default charset and create "Feedback" object
		Feedback feedback = mapper.readValue(body.getBytes("ISO-8859-1"), Feedback.class);
		
		//store image and set the fileId inside "feedback"
		feedback.setFileId(feedBackFileManager.storeNewFile(file));
		
		//store "feedback" inside Mongo
		feedbackRepository.save(feedback);
	}

	public List<Feedback> getStationFeedbacks(String cityId, String stationId)
	{
		ArrayList<Feedback> feedbacks = new ArrayList<Feedback>(feedbackRepository.findByObjectIdAndObjectTypeAndCityId(stationId, "station", cityId));
		long currentDate = GregorianCalendar.getInstance().getTimeInMillis();
		
		for(int i = 0; i < feedbacks.size(); i++)
		{
			//if it is more than "dbCacheTime" days older, do not display it
			if(currentDate - feedbacks.get(i).getDate() > Double.parseDouble(dbCacheTime) * 86400000)//convert from days to milliseconds
			{
				feedbacks.remove(i--);
			}
		}
		return sortFeedbacksByDate(feedbacks);
	}
	
	public List<Feedback> getBikeFeedbacks(String cityId, String bikeId)
	{
		ArrayList<Feedback> feedbacks = new ArrayList<Feedback>( feedbackRepository.findByObjectIdAndObjectTypeAndCityId(bikeId, "bike", cityId));
		long currentDate = GregorianCalendar.getInstance().getTimeInMillis();
		
		for(int i = 0; i < feedbacks.size(); i++)
		{
			//if it is more than "dbCacheTime" days older, do not display it
			if(currentDate - feedbacks.get(i).getDate() > Double.parseDouble(dbCacheTime) * 86400000)//convert from days to milliseconds
			{
				feedbacks.remove(i--);
			}
		}
		return sortFeedbacksByDate(feedbacks);
	}
	
	private List<Feedback> sortFeedbacksByDate(List<Feedback> feedbacks)
	{
		Collections.sort(feedbacks, new Comparator<Feedback>()
				{
					@Override
				    public int compare(Feedback f1, Feedback f2)
					{
						long toRtn = f2.getDate() - f1.getDate();
						
						//safe cast from long to int
						if(toRtn > Integer.MAX_VALUE)
						{
							toRtn = Integer.MAX_VALUE;
						}
						else if(toRtn < Integer.MIN_VALUE)
						{
							toRtn = Integer.MIN_VALUE;
						}
						
						return (int)toRtn;
					}
				});
		return feedbacks;
	}
}
