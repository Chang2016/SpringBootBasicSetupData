package org.strupp.springboot.topic;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TopicService {
	
	@Autowired
	private TopicRepository topicRepository;
	
	private final RestTemplate restTemplate;
	
	public TopicService(RestTemplateBuilder restTemplateBuilder) {
		restTemplate = restTemplateBuilder.build();
	}
	
	public List<Topic> retrieveTopics() {
		return (List<Topic>) topicRepository.findAll();
	}
	
	public Optional<Topic> retrieveTopic(long id) {
		return topicRepository.findById(id);
	}
	
	@Async
    public CompletableFuture<User> findUser(String user) throws InterruptedException {
        String url = String.format("https://api.github.com/users/%s", user);
        User results = restTemplate.getForObject(url, User.class);
        // Artificial delay of 10s for demonstration purposes
        Thread.sleep(10000L);
        return CompletableFuture.completedFuture(results);
    }
	
	public Topic createTopic(@Valid Topic topic) {
		return topicRepository.save(topic);
	}
	
	public Topic updateTopic(Topic topic) {
		try {
			return topicRepository.save(topic);
		} catch (DataIntegrityViolationException e) {
//			Set set = Stream.of(Stream.empty());
//			ConstraintViolation f;
//			throw new ConstraintViolationException("", e);
			throw e;
		}
	}
	
	public void deleteTopic(long id) {
//		try {
			topicRepository.deleteById(id);
//		} catch (Exception e) {
//			if(e instanceof ConstraintViolationException)
//				throw e;
//			else
//				throw new ResourceNotFoundException(e.getMessage());
//		}
	}
}
