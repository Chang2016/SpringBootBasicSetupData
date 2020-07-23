package org.strupp.springboot.topic;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.strupp.springboot.exceptions.ResourceNotFoundException;

//Achtung Tomcat läuft auf Port 8443, siehe application.properties

//Das Package des @RestController muss in einem Unterpackage der main()-Klasse sein!
//Der RestController wird im Classpath gefunden, keine Konfiguration notwendig

/*
 * A key difference between a traditional MVC controller and the RESTful web service controller above is the way that the HTTP response 
 * body is created. Rather than relying on a view technology to perform server-side rendering of the greeting data to HTML, this RESTful 
 * web service controller simply populates and returns a Greeting object. The object data will be written directly to the HTTP response 
 * as JSON.
 *
 * To accomplish this, the @ResponseBody annotation on the greeting() method tells Spring MVC that it does not need to render the 
 * greeting object through a server-side view layer, but that instead the greeting object returned is the response body, and should 
 * be written out directly.
 *
 * The Greeting object must be converted to JSON. Thanks to Spring’s HTTP message converter support, you don’t need to do this 
 * conversion manually. Because Jackson is on the classpath, Spring’s MappingJackson2HttpMessageConverter is automatically chosen 
 * to convert the Greeting instance to JSON.
 */
@RestController
public class TopicController {
	
	@Autowired
	private TopicService topicService;
	
	@RequestMapping("/")
	public String hi() {
		return "hello";
	}
	
//	automatische Serialisierung nach JSON
	@RequestMapping("/topics")
	public List<Topic> retrieveTopics(@RequestHeader HttpHeaders headers) throws InterruptedException, ExecutionException {
//		CompletableFuture<User> future = topicService.findUser("Marco");
//		User user = future.get();
		return topicService.retrieveTopics();
	}
	
	@RequestMapping("/topics/delayed")
	public List<Topic> retrieveTopicsDelayed(@RequestHeader HttpHeaders headers) {
		try {
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			e.printStackTrace();
		}
		return topicService.retrieveTopics();
	}
	
	@RequestMapping(method=RequestMethod.OPTIONS, value="/topics/{id}")
	public void optionTopic(@PathVariable long id, @RequestHeader HttpHeaders headers) {
		headers.keySet().stream().forEach(k -> System.out.println(k + " " + headers.get(k)));
	}
	
//	@CrossOrigin(origins = "http://127.0.0.1")
	@RequestMapping(value="/topics/{id}", produces="application/json;charset=UTF-8")
	public Topic retrieveTopic(@PathVariable long id, @RequestHeader HttpHeaders headers) {
//		headers.keySet().stream().forEach(k -> System.out.println(k + " " + headers.get(k)));
		Optional<Topic> opt = topicService.retrieveTopic(id);
		return opt.orElseThrow(() -> new ResourceNotFoundException("No Topic with id " + id + " found."));
	}
		
	@RequestMapping(method=RequestMethod.POST, value="/topics/", produces="application/json;charset=UTF-8")
	public ResponseEntity<Topic> createTopic(@Valid @RequestBody Topic topic, HttpServletRequest request) {
		Topic t = topicService.createTopic(topic);
		UriComponents uri = ServletUriComponentsBuilder.fromRequest(request).pathSegment(String.valueOf(t.getId())).build();
        return ResponseEntity.created(uri.toUri()).build();
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/topics/{id}", produces="application/json;charset=UTF-8")
//	@ResponseStatus(value=HttpStatus)
	public ResponseEntity<Topic> updateTopic(@PathVariable long id, @Valid @RequestBody Topic topic, HttpServletRequest request) {
		topic.setId(id);
		Topic t = topicService.updateTopic(topic);
		return ResponseEntity.ok(t);
	}
	
	@RequestMapping(method=RequestMethod.DELETE, value="/topics/{id}")
	public void deleteTopic(@PathVariable long id, HttpServletRequest request, HttpServletResponse response) {
		topicService.deleteTopic(id);
		response.setStatus(HttpServletResponse.SC_ACCEPTED);
	}
	
	private void debugRequest(HttpHeaders headers) {
		headers.keySet().stream().forEach(k -> System.out.println(k + ": " + headers.get(k)));
	}
}
