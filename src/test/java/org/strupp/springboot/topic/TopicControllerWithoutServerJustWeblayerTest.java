package org.strupp.springboot.topic;

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

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.After;
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
import org.springframework.util.StreamUtils;
import org.strupp.springboot.SpringBootBasicDataMain;
import org.strupp.springboot.authentication.AuthorizationServerConfiguration;
import org.strupp.springboot.authentication.OAuth2SecurityConfiguration;

/*
 * Hier wird als TextContext nur die Klasse TopicController geladen
 */
@RunWith(SpringRunner.class)
@WebMvcTest(TopicController.class) //bootstraps just TopicController
//@WebMvcTest(includeFilters = @Filter(classes = EnableWebSecurity.class))
//@Import(includeFilters = @Filter(classes = EnableWebSecurity.class), OAuth2SecurityConfiguration.class)
@ContextConfiguration(classes = {SpringBootBasicDataMain.class,
    AuthorizationServerConfiguration.class,
    OAuth2SecurityConfiguration.class})
public class TopicControllerWithoutServerJustWeblayerTest {

  @Autowired
  private Environment env;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TopicService service;

  @MockBean
  private TokenStore tokenStore;

//    @MockBean
//    TokenAuthProvider tokenAuthProvider;

  @Value("classpath:create_topic.json")
  private Resource createJsonScript;

  @Value("classpath:update_topic.json")
  private Resource updateJsonScript;

  @Captor
  private ArgumentCaptor<Topic> topicCaptor;

  private static final long TOPICID = 4;
  private static final String STUBNAME = "JSON";
  private static final String REQUESTURL = "https://localhost:8443/topics/";

  @Before
  public void setup() {
    String keyStoreType = env.getProperty("server.ssl.key-store-type");
    System.out.println(keyStoreType);
    List<Topic> topics = new ArrayList<>();
    Topic topic = new Topic();
    topic.setName("Mockito");
    topic.setId(5L);
    topics.add(topic);
    Optional<Topic> opt = Optional.of(topic);
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

  //	@Ignore
  @Test
  public void postTopicTest() throws Exception {
    String content = StreamUtils
        .copyToString(createJsonScript.getInputStream(), Charset.forName("UTF-8"));
    mockMvc.perform(post(REQUESTURL).contentType(MediaType.APPLICATION_JSON).content(content))
        .andExpect(status().is2xxSuccessful());
    verify(service).createTopic(topicCaptor.capture());
    Topic savedEntity = topicCaptor.getValue();
    assertThat(savedEntity.getName(), is("JSON"));
    assertThat(savedEntity.getId(), is(4L));
  }

  //	@Ignore
  @Test
  public void updateTopicTest() throws Exception {
    String content = StreamUtils
        .copyToString(updateJsonScript.getInputStream(), Charset.forName("UTF-8"));
    mockMvc.perform(put(REQUESTURL + "/4").contentType(MediaType.APPLICATION_JSON).content(content))
        .andExpect(status().is2xxSuccessful());
    verify(service).updateTopic(topicCaptor.capture());
    Topic savedEntity = topicCaptor.getValue();
    assertThat(savedEntity.getName(), is("JSON"));
    assertThat(savedEntity.getId(), is(4L));
  }

  @Test
  public void loadTopicTest() throws Exception {
    this.mockMvc.perform(get("https://localhost:8443/topics/1"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.id", is(5)))
        .andExpect(jsonPath("$.name", is("Mockito")))
    ;
  }

  //    @Ignore
  @Test
  public void loadTopicsTest() throws Exception {
    this.mockMvc.perform(get("/topics"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id", is(5)))
        .andExpect(jsonPath("$[0].name", is("Mockito")));
  }

  @Test
  public void deleteTopicsTest() throws Exception {
    this.mockMvc.perform(get("/topics"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id", is(5)))
        .andExpect(jsonPath("$[0].name", is("Mockito")));
  }

  @After
  public void createTodoItem() throws Exception {
//        todoRepository.deleteAll();
  }
}
