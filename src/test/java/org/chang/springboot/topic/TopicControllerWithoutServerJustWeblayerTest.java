package org.chang.springboot.topic;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.chang.springboot.SecurityConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StreamUtils;
import org.chang.springboot.SpringBootBasicDataMain;
import org.springframework.web.context.WebApplicationContext;

/*
 * Hier wird als TextContext nur die Klasse TopicController geladen
 */
@RunWith(SpringRunner.class)
@WebMvcTest(TopicController.class) //bootstraps just TopicController
@ContextConfiguration(classes = {SpringBootBasicDataMain.class,
    SecurityConfig.class})
public class TopicControllerWithoutServerJustWeblayerTest {

  @Autowired
  private WebApplicationContext context;

  @Autowired
  private Environment env;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TopicTransformer topicTransformer;

  @MockBean
  private TopicService service;

  @MockBean
  private TokenStore tokenStore;

  @Value("classpath:create_topic.json")
  private Resource createJsonScript;

  @Value("classpath:create_topic_with_too_short_name.json")
  private Resource topicWithTooShortName;

  @Value("classpath:update_topic.json")
  private Resource updateJsonScript;

  @Captor
  private ArgumentCaptor<Topic> topicCaptor;

  private static final long TOPICID = 4;
  private static final String STUBNAME = "JSON";
  private static final String REQUESTURL = "https://localhost:8443/topics/";

  @Before
  public void setup() {
    mockMvc = MockMvcBuilders
        .webAppContextSetup(context)
//        .apply(springSecurity())
        .build();

    List<Topic> topics = new ArrayList<>();
    Topic topic = new Topic();
    topic.setName("Mockito");
    topic.setId(5L);
    topics.add(topic);
    List<TopicDto> dtos = new ArrayList<>();
    TopicDto dto = new TopicDto();
    dto.setName(topic.getName());
    dto.setId(topic.getId());
    dtos.add(dto);
    Optional<Topic> opt = Optional.of(topic);
    when(topicTransformer.toListOfTopicDto(any())).thenReturn(dtos);
    when(topicTransformer.toTopicDto(any())).thenReturn(dto);
    when(topicTransformer.toTopic(any())).thenReturn(topic);
    when(service.retrieveTopics()).thenReturn(topics);
    when(service.retrieveTopic(anyLong())).thenReturn(opt);
    when(service.createTopic(any(Topic.class))).thenAnswer((Answer<Topic>) invocationOnMock -> {
      Topic t = invocationOnMock.getArgument(0);
      t.setName(STUBNAME);
      t.setId(TOPICID);
      return t;
    });
    when(service.updateTopic(any(Topic.class))).thenAnswer((Answer<Topic>) invocationOnMock -> {
      Topic t = invocationOnMock.getArgument(0);
      t.setName(STUBNAME);
      t.setId(TOPICID);
      return t;
    });
  }

  @Test
  public void postTopicTest() throws Exception {
    String content = StreamUtils
        .copyToString(createJsonScript.getInputStream(), StandardCharsets.UTF_8);
    mockMvc.perform(post(REQUESTURL).contentType(MediaType.APPLICATION_JSON).content(content))
        .andExpect(status().is2xxSuccessful());
    verify(service).createTopic(topicCaptor.capture());
    Topic savedEntity = topicCaptor.getValue();
    assertThat(savedEntity.getName(), is("JSON"));
    assertThat(savedEntity.getId(), is(4L));
  }

  @Test
  public void postTopicWithTooShortName() throws Exception {
    String content = StreamUtils
        .copyToString(topicWithTooShortName.getInputStream(), StandardCharsets.UTF_8);
    mockMvc.perform(post(REQUESTURL).contentType(MediaType.APPLICATION_JSON).content(content))
        .andExpect(status().is4xxClientError());
//    verify(service).createTopic(topicCaptor.capture());
//    Topic savedEntity = topicCaptor.getValue();
//    assertThat(savedEntity.getName(), is("JSON"));
//    assertThat(savedEntity.getId(), is(4L));
  }

  @Test
  public void updateTopicTest() throws Exception {
    String content = StreamUtils
        .copyToString(updateJsonScript.getInputStream(), StandardCharsets.UTF_8);
    mockMvc.perform(put(REQUESTURL + "/4").contentType(MediaType.APPLICATION_JSON).content(content))
        .andExpect(status().is2xxSuccessful());
    verify(service).updateTopic(topicCaptor.capture());
    Topic savedEntity = topicCaptor.getValue();
    assertThat(savedEntity.getName(), is("JSON"));
    assertThat(savedEntity.getId(), is(4L));
  }

  @Test
  public void loadTopicTest() throws Exception {
    this.mockMvc.perform(get("https://localhost:8443/topics/1")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.id", is(5)))
        .andExpect(jsonPath("$.name", is("Mockito")))
    ;
  }

  @Test
  public void loadTopicsTest() throws Exception {
    this.mockMvc.perform(get("/topics"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.topics", hasSize(1)))
        .andExpect(jsonPath("$.topics[0].id", is(5)))
        .andExpect(jsonPath("$.topics[0].name", is("Mockito")));
  }

  @Test
  public void deleteTopicsTest() throws Exception {
    this.mockMvc.perform(get("/topics"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.topics", hasSize(1)))
        .andExpect(jsonPath("$.topics[0].id", is(5)))
        .andExpect(jsonPath("$.topics[0].name", is("Mockito")));
  }
}
