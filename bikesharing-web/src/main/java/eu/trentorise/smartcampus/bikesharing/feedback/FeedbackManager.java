package eu.trentorise.smartcampus.bikesharing.feedback;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.trentorise.smartcampus.bikesharing.db.FeedbackRepository;

@Component
public class FeedbackManager
{

	@Autowired
	private FeedbackFileManager feedBackFileManager;
	
	@Autowired
	private FeedbackRepository feedbackRepository;

	private ObjectMapper mapper = new ObjectMapper();
	
	public void addNewFeedback(String body, byte[] file) throws JsonParseException, JsonMappingException, IOException
	{
		Feedback feedback = mapper.readValue(body, Feedback.class);
		feedback.setFileId(feedBackFileManager.storeNewFile(file));
		System.out.println(feedback.toString());
		feedbackRepository.save(feedback);
	}

	public List<Feedback> getStationFeedback(String cityId, String stationId)
	{
		return feedbackRepository.findByObjectIdAndObjectTypeAndCityId(stationId, "station", cityId);
	}
	
	public List<Feedback> getBikeFeedback(String cityId, String bikeId)
	{
		return feedbackRepository.findByObjectIdAndObjectTypeAndCityId(bikeId, "bike", cityId);
	}
}
