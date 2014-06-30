package eu.trentorise.smartcampus.bikesharing.managers;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import eu.trentorise.smartcampus.bikesharing.feedback.Feedback;

@Component
public interface FeedbackRepository extends MongoRepository<Feedback, String>
{

}
