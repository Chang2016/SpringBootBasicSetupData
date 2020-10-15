package org.chang.springboot.topic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TopicServiceTest {

    private TopicRepository topicRepository;

    private RestTemplateBuilder restTemplateBuilder;

    //SUT
    private TopicService topicService;

    @BeforeEach
    void init() {
        topicRepository = mock(TopicRepository.class);
        restTemplateBuilder = mock(RestTemplateBuilder.class);

        topicService = new TopicService(restTemplateBuilder, topicRepository);
    }

    @Test
    void testRetrieveTopics() {
        // given
        List<Topic> topics = initTopicFixture();
        when(topicRepository.findAll()).thenReturn(topics);
        // when
        List<Topic> retrieved = topicService.retrieveTopics();
        // then
        assertThat(retrieved.size()).isEqualTo(1);
        Topic next = retrieved.iterator().next();
        assertThat(next.getName()).isEqualTo("Dummy");
        assertThat(next.getId()).isEqualTo(1L);
    }

    @Test
    void testRetrieveTopicsStartingWith() {
        // given
        List<TopicDto> topicDtos = initTopicDtoFixture();
        when(topicRepository.findByNameStartingWith(anyString())).thenReturn(topicDtos);
        // when
        TopicList topicList = topicService.retrieveTopicsStartingWith("sub");
        // then
        List<TopicDto> topics = topicList.getTopics();
        assertThat(topics.size()).isEqualTo(1);
        TopicDto next = topics.iterator().next();
        assertThat(next.getId()).isEqualTo(1L);
        assertThat(next.getName()).isEqualTo("Dummy");
    }

    @Test
    void testRetrieveTopic() {
        // given
        Optional<Topic> maybeTopic = initTopicOptional();
        when(topicRepository.findById(any())).thenReturn(maybeTopic);
        // when
        Optional<Topic> result = topicService.retrieveTopic(10);
        // then
        assertThat(result.isPresent()).isTrue();
        Topic topic = result.get();
        assertThat(topic.getId()).isEqualTo(1L);
        assertThat(topic.getName()).isEqualTo("Dummy");
    }

    @Test
    void testUpdateTopic() {
        // given
        Topic topic = initTopic();
        when(topicRepository.save(any())).thenReturn(topic);
        // when
        Topic saved = topicService.updateTopic(topic);
        // then
        assertThat(saved).isNotNull();
    }

    @Test
    void testUpdateBrokenTopic() {
        Topic topic = initTopic();
        when(topicRepository.save(any())).thenThrow(DataIntegrityViolationException.class);
        assertThrows(DataIntegrityViolationException.class, () -> topicService.updateTopic(topic));
    }

    private Optional<Topic> initTopicOptional() {
        Topic topic = initTopic();
        return Optional.of(topic);
    }

    private Topic initTopic() {
        Topic topic = new Topic();
        topic.setId(1L);
        topic.setName("Dummy");
        return topic;
    }

    private List<Topic> initTopicFixture() {
        Topic topic = initTopic();
        List<Topic> topics = new ArrayList<>();
        topics.add(topic);
        return topics;
    }

    private List<TopicDto> initTopicDtoFixture() {
        List<TopicDto> topics = new ArrayList<>();
        TopicDto topic = new TopicDto();
        topic.setId(1L);
        topic.setName("Dummy");
        topics.add(topic);
        return topics;
    }
}
