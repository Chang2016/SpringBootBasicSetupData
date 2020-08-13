package org.strupp.springboot.topic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.strupp.springboot.exceptions.ResourceNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class TopicControllerTest {

  private TopicTransformer topicTransformer;
  private TopicService topicService;

  private TopicController topicController;

  @Captor
  private ArgumentCaptor<Long> deleteCaptor;

  @Before
  public void init() {
    topicTransformer = new TopicTransformer();
    topicService = mock(TopicService.class);
    topicController = new TopicController(topicTransformer, topicService);
  }

  @Test
  public void getHi() {
    assertThat(topicController.hi()).isEqualTo("hello");
  }

  @Test
  public void retrieveTopics() {
    Topic topic = new Topic();
    topic.setId(1L);
    topic.setName("Blabla");
    List<Topic> topics = new ArrayList<>();
    topics.add(topic);
    when(topicService.retrieveTopics()).thenReturn(topics);
    TopicList topicList = topicController.retrieveTopics();
    assertThat(topicList.getTopics().size()).isEqualTo(1);
    List<TopicDto> listOfTopics = topicList.getTopics();
    assertThat(listOfTopics.size()).isEqualTo(1);
    TopicDto topicDto = listOfTopics.get(0);
    assertThat(topicDto.getId()).isEqualTo(topic.getId());
    assertThat(topicDto.getName()).isEqualTo(topic.getName());
  }

  @Test
  public void retrieveTopicsDelayed() {
    Topic topic = new Topic();
    topic.setId(1L);
    topic.setName("Blabla");
    List<Topic> topics = new ArrayList<>();
    topics.add(topic);
    when(topicService.retrieveTopics()).thenReturn(topics);
    TopicList topicList = topicController.retrieveTopicsDelayed(HttpHeaders.EMPTY);
    assertThat(topicList.getTopics().size()).isEqualTo(1);
    List<TopicDto> listOfTopics = topicList.getTopics();
    assertThat(listOfTopics.size()).isEqualTo(1);
    TopicDto topicDto = listOfTopics.get(0);
    assertThat(topicDto.getId()).isEqualTo(topic.getId());
    assertThat(topicDto.getName()).isEqualTo(topic.getName());
  }

  @Test
  public void retrieveExistingTopic() {
    Topic topic = new Topic();
    topic.setId(1L);
    topic.setName("Blabla");
    when(topicService.retrieveTopic(topic.getId())).thenReturn(Optional.of(topic));
    TopicDto topicResult = topicController.retrieveTopic(topic.getId(), HttpHeaders.EMPTY);
    assertThat(topicResult.getId()).isEqualTo(topic.getId());
    assertThat(topicResult.getName()).isEqualTo(topic.getName());
  }

  @Test(expected = ResourceNotFoundException.class)
  public void retrieveNonExistingTopic() {
    when(topicService.retrieveTopic(1)).thenReturn(Optional.empty());
    topicController.retrieveTopic(1, HttpHeaders.EMPTY);
  }

  @Test
  public void createTopic() {
    long id = 432L;
    Topic topic = new Topic();
    topic.setId(id);
    topic.setName("Blabla");
    TopicDto topicDto = new TopicDto();
//    topicDto.setId(id);
    topicDto.setName("Blabla");
    when(topicService.createTopic(any())).thenReturn(topic);
    ResponseEntity<TopicDto> topicResult = topicController.createTopic(topicDto, new MockHttpServletRequest());
    assertThat(topicResult.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(Objects.requireNonNull(topicResult.getBody()).getName()).isEqualTo(topic.getName());
    assertThat(Objects.requireNonNull(topicResult.getBody()).getId()).isEqualByComparingTo(id);
//    assertThat(topicResult.getHeaders().getLocation().toString()).endsWith(String.valueOf(id));
  }

  @Test
  public void createInvalidTopic() {
    long id = 432L;
    String tooShortName = "b";
    Topic topic = new Topic();
    topic.setId(id);
    topic.setName(tooShortName);
    TopicDto topicDto = new TopicDto();
    //topicDto.setId(id);
    topicDto.setName(tooShortName);
    when(topicService.createTopic(any())).thenReturn(topic);
    ResponseEntity<TopicDto> topicResult = topicController.createTopic(topicDto, new MockHttpServletRequest());
    assertThat(topicResult.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(Objects.requireNonNull(topicResult.getBody()).getName()).isEqualTo(topic.getName());
    assertThat(Objects.requireNonNull(topicResult.getBody()).getId()).isEqualByComparingTo(id);
//    assertThat(topicResult.getHeaders().getLocation().toString()).endsWith(String.valueOf(id));
  }

  @Test
  public void updateTopic() {
    long id = 432L;
    TopicDto topicDto = new TopicDto();
    topicDto.setId(id);
    topicDto.setName("Blabla");
    Topic topic = new Topic();
    topic.setId(id);
    topic.setName("Blabla");
    when(topicService.updateTopic(any())).thenReturn(topic);
    ResponseEntity<TopicDto> topicResult = topicController.updateTopic(id, topicDto, new MockHttpServletRequest());
    assertThat(topicResult.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(Objects.requireNonNull(topicResult.getBody()).getId()).isEqualTo(id);
    assertThat(Objects.requireNonNull(topicResult.getBody()).getName()).isEqualTo("Blabla");
  }

  @Test
  public void deleteTopic() {
    long id = 432L;
    topicController.deleteTopic(id, new MockHttpServletRequest(), new MockHttpServletResponse());
    verify(topicService).deleteTopic(deleteCaptor.capture());
    Long value = deleteCaptor.getValue();
    MatcherAssert.assertThat(value, equalTo(id));
  }
}
