package eu.trentorise.smartcampus.bikesharing.managers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import eu.trentorise.smartcampus.bikesharing.exceptions.WebServiceErrorException;
import eu.trentorise.smartcampus.bikesharing.model.AnarchicBike;

@Component
public class AnarchicBikeServiceClient
{
	public Map<String, AnarchicBike> getAnarchicBikes(String cityID) throws WebServiceErrorException
	{		
		Map<String, AnarchicBike> anarchicBikes = new HashMap<String, AnarchicBike>();
		//create and add 4 random "AnarchicBikes"
		AnarchicBike bike = new AnarchicBike("0000");
		anarchicBikes.put(bike.getId(), bike);
		bike = new AnarchicBike("0001");
		anarchicBikes.put(bike.getId(), bike);
		bike = new AnarchicBike("0002");
		anarchicBikes.put(bike.getId(), bike);
		bike = new AnarchicBike("0003");
		anarchicBikes.put(bike.getId(), bike);
		return anarchicBikes;
	}
}
