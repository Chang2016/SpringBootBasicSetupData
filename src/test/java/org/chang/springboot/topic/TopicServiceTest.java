package org.chang.springboot.topic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class TopicServiceTest {

    @TestConfiguration
    static class TopicServiceTestContextConfiguration {
        @MockBean
        private TopicRepository topicRepository;

        @MockBean
        private RestTemplateBuilder restTemplateBuilder;

        @Bean
        public TopicService topicService() {
            List<Topic> topics = initTopicFixture();
            List<TopicDto> topicDtos = initTopicDtoFixture();
            when(topicRepository.findAll()).thenReturn(topics);
            when(topicRepository.findByNameStartingWith(any())).thenReturn(topicDtos);
            return new TopicService(restTemplateBuilder, topicRepository);
        }

        private List<Topic> initTopicFixture() {
            Topic topic = new Topic();
            topic.setId(1L);
            topic.setName("Dummy");
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

    //SUT
    @Autowired
    private TopicService topicService;

    @Test
    void testRetrieveTopics() {
        List<Topic> topics = topicService.retrieveTopics();
        assertThat(topics.size()).isEqualTo(1);
        Topic next = topics.iterator().next();
        assertThat(next.getName()).isEqualTo("Dummy");
        assertThat(next.getId()).isEqualTo(1L);
    }

    @Test
    void testRetrieveTopicsStartingWith() {
        TopicList topicList = topicService.retrieveTopicsStartingWith("sub");
        List<TopicDto> topics = topicList.getTopics();
        assertThat(topics.size()).isEqualTo(1);
    }
}
