package eu.trentorise.smartcampus.bikesharing.managers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.trentorise.smartcampus.bikesharing.exceptions.WebServiceErrorException;
import eu.trentorise.smartcampus.bikesharing.feedback.FeedbackManager;
import eu.trentorise.smartcampus.bikesharing.model.AnarchicBike;

@Component
public class AnarchicBikeServiceClient
{
	@Autowired
	private FeedbackManager feedBackManager;
	
	public Map<String, AnarchicBike> getAnarchicBikes(String cityId) throws WebServiceErrorException
	{		
		Map<String, AnarchicBike> anarchicBikes = new HashMap<String, AnarchicBike>();
		//create and add 4 random "AnarchicBikes"
		for(int i = 0; i < 8; i++)
		{
			AnarchicBike bike = new AnarchicBike("" + i);
			bike.setReportsNumber(feedBackManager.getBikeFeedbacks(cityId, bike.getId()).size());
			anarchicBikes.put(bike.getId(), bike);
		}
		return anarchicBikes;
	}
}
