package eu.trentorise.smartcampus.bikesharing.db;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import eu.trentorise.smartcampus.bikesharing.feedback.Feedback;

@Component
public interface FeedbackRepository extends MongoRepository<Feedback, String>
{
	public List<Feedback> findByObjectId(String objectId);
	public List<Feedback> findByObjectIdAndCityId(String objectId, String cityId);
	public List<Feedback> findByObjectIdAndObjectTypeAndCityId(String objectId, String objectType, String cityId);
}
