package eu.trentorise.smartcampus.bikesharing.feedback;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.trentorise.smartcampus.bikesharing.managers.FeedbackRepository;

@Component
public class FeedbackManager
{

	@Autowired
	private FeedbackFileManager feedBackFileManager;
	
	//@Autowired
	//private FeedbackRepository feedbackRepository;
	
	public void addNewFeedback(Feedback feedback, byte[] file)
	{
		feedback.setFileId(feedBackFileManager.storeNewFile(file));
		//feedbackRepository.save(feedback);
	}

	public List<Feedback> getStationFeedback(String cityID, String stationID)
	{
		ArrayList<Feedback> fb =  new ArrayList<Feedback>();
		fb.add(new Feedback(0L, "Station", "1144", "Malfunction", "Colonnina 3 malfunzionante", null));
		return fb;
		//return feedbackRepository.findByObjectId()
	}
	
	public List<Feedback> getBikeFeedback(String cityID, String bikeID)
	{
		ArrayList<Feedback> fb =  new ArrayList<Feedback>();
		fb.add(new Feedback(0L, "Bike", "0001", "Malfunction", "Gomma forata", null));
		return fb;
	}
}
