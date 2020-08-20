package org.chang.springboot.topic;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.chang.springboot.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

  private final TopicTransformer topicTransformer;

  private final TopicService topicService;

  @Autowired
  public TopicController(TopicTransformer topicTransformer, TopicService topicService) {
    this.topicTransformer = topicTransformer;
    this.topicService = topicService;
  }

  @RequestMapping("/")
  public String hi() {
    return "hello";
  }

  //	automatische Serialisierung nach JSON
  @RequestMapping("/topics")
  public ResponseEntity<TopicList> retrieveTopics() {
    List<Topic> topics = topicService.retrieveTopics();
    List<TopicDto> topicDtos = topicTransformer.toListOfTopicDto(topics);
    return new ResponseEntity<>(new TopicList(topicDtos), HttpStatus.OK);
  }

  @GetMapping("topicsstartingwith/{subs}")
  public ResponseEntity<TopicList> retrieveTopicsStartingWith(@PathVariable String subs) {
    TopicList topicList = topicService.retrieveTopicsStartingWith(subs);
    return new ResponseEntity<>(topicList, HttpStatus.OK);
  }

  @RequestMapping("/topics/delayed")
  public ResponseEntity<TopicList> retrieveTopicsDelayed(@RequestHeader HttpHeaders headers) {
    try {
      TimeUnit.SECONDS.sleep(1);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      e.printStackTrace();
    }
    List<Topic> topics = topicService.retrieveTopics();
    List<TopicDto> topicDtos = topicTransformer.toListOfTopicDto(topics);
    return new ResponseEntity<>(new TopicList(topicDtos), HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.GET, value = "/topics/{id}")
  public ResponseEntity<TopicDto> retrieveTopic(@PathVariable long id, @RequestHeader HttpHeaders headers) {
    Optional<Topic> opt = topicService.retrieveTopic(id);
    TopicDto topicDto = topicTransformer.toTopicDto(opt
        .orElseThrow(() -> new ResourceNotFoundException("No Topic with id " + id + " found.")));
    return new ResponseEntity<>(topicDto, HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.POST, value = "/topics/", produces = "application/json;charset=UTF-8")
  public ResponseEntity<TopicEnvelope> createTopic(@Valid @RequestBody TopicDto topicDto,
      HttpServletRequest request) {
    TopicEnvelope topicEnvelope = new TopicEnvelope();
    Topic topic = topicTransformer.toTopic(topicDto);
    Topic t = topicService.createTopic(topic);
    TopicDto resultDto = topicTransformer.toTopicDto(t);
    topicEnvelope.setTopicDto(resultDto);
    return new ResponseEntity<>(topicEnvelope, HttpStatus.CREATED);
  }

  @RequestMapping(method = RequestMethod.PUT, value = "/topics/{id}", produces = "application/json;charset=UTF-8")
  public ResponseEntity<TopicDto> updateTopic(@PathVariable long id, @Valid @RequestBody TopicDto topicDto,
      HttpServletRequest request) {
    topicDto.setId(id);
    Topic topic = topicTransformer.toTopic(topicDto);
    Topic updatedTopic = topicService.updateTopic(topic);
    TopicDto resultDto = topicTransformer.toTopicDto(updatedTopic);
    return new ResponseEntity<>(resultDto, HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.DELETE, value = "/topics/{id}", produces = "application/json;charset=UTF-8")
  public ResponseEntity<Long> deleteTopic(@PathVariable long id, HttpServletRequest request,
      HttpServletResponse response) {
    topicService.deleteTopic(id);
    return new ResponseEntity<>(id, HttpStatus.OK);
  }

}
