package eu.trentorise.smartcampus.bikesharing.feedback;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Component;

import eu.trentorise.smartcampus.bikesharing.db.FeedbackRepository;

@EnableMongoRepositories
@Component
public class FeedbackManager
{

	@Autowired
	private FeedbackFileManager feedBackFileManager;
	
	@Autowired
	private FeedbackRepository feedbackRepository;
	
	public void addNewFeedback(Feedback feedback, byte[] file)
	{
		feedback.setFileId(feedBackFileManager.storeNewFile(file));
		System.out.println(feedback.toString());
		feedbackRepository.save(feedback);
	}

	public List<Feedback> getStationFeedback(String cityID, String stationID)
	{
		ArrayList<Feedback> fb =  new ArrayList<Feedback>();
		fb.add(new Feedback(0L, "Station", "1144", "Malfunction", "Colonnina 3 malfunzionante", null, null));
		return fb;
		//return feedbackRepository.findByObjectId()
	}
	
	public List<Feedback> getBikeFeedback(String cityID, String bikeID)
	{
		ArrayList<Feedback> fb =  new ArrayList<Feedback>();
		fb.add(new Feedback(0L, "Bike", "0001", "Malfunction", "Gomma forata", null, null));
		return fb;
	}
}
