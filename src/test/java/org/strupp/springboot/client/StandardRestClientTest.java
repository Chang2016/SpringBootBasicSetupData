package org.strupp.springboot.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;
import org.strupp.springboot.topic.Topic;
import org.strupp.springboot.topic.TopicDto;
import org.strupp.springboot.topic.TopicList;

public class StandardRestClientTest {
  private RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
  //SUT
  private StandardRestClient standardRestClient = new StandardRestClient(restTemplate, "");

  @Test
  public void findExistingTopic() {
    //Given
    Topic topic = new Topic();
    topic.setId(1);
    topic.setName("Mock");
    Mockito.when(restTemplate.getForObject(anyString(), eq(Topic.class))).thenReturn(topic);
    //When
    Optional<Topic> maybeTopic = standardRestClient.findTopicHavingId(1);
    //Then
    assertThat(maybeTopic).isNotEmpty();
    Topic found = maybeTopic.get();
    assertThat(found).isEqualTo(topic);
  }

  @Test
  public void findNonExistingTopic() {
    //Given
    Mockito.when(restTemplate.getForObject(anyString(), eq(Topic.class))).thenReturn(null);
    //When
    Optional<Topic> maybeTopic = standardRestClient.findTopicHavingId(1);
    //Then
    assertThat(maybeTopic).isEmpty();
  }

//  @Test(expected = RestClientException.class)
//  public void findNonExistingTopicWithMalformedBaseUrl() {
//    //Given
//    Mockito.when(restTemplate.getForObject(anyString(), eq(Topic.class))).thenThrow(new RestClientException(""));
//    //When
//    Optional<Topic> maybeTopic = standardRestClient.findTopicHavingId(1);
//    //Then
//    //assertThat(maybeTopic).isEmpty();
//    Mockito.verify()
//  }

  @Test
  public void testFindNonEmptyListOfTopics() {
    //Given
    TopicDto t1 = new TopicDto(1, "lala");
    TopicDto t2 = new TopicDto(2, "huhu");
    List<TopicDto> list = new ArrayList<>();
    list.add(t1);
    list.add(t2);
    TopicList tl = new TopicList(list);
    Mockito.when(restTemplate.getForObject(anyString(), eq(TopicList.class))).thenReturn(tl);
    //When
    TopicList topics = standardRestClient.findTopics();
    //Then
    assertThat(topics.getTopics()).containsAll(list);
  }

  @Test
  public void testFindEmptyListOfTopics() {
    //Given
    Mockito.when(restTemplate.getForObject(anyString(), eq(TopicList.class))).thenReturn(new TopicList());
    //When
    TopicList topics = standardRestClient.findTopics();
    //Then
    assertThat(topics.getTopics()).isEmpty();
  }
}
