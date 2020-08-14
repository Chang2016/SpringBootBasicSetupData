package org.strupp.springboot.topic;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

/*
 * Dieser Test startet nicht den Server, Spring nimmt den HTTP-Request entgegen und testet Controller, DAO,...
 */
@RunWith(SpringRunner.class)
@SpringBootTest  //bootstraps whole ApplicationContext
@ActiveProfiles(profiles = "test")
@AutoConfigureMockMvc
public class TopicControllerWithoutServerIT {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private Environment env;

  @Value("classpath:create_topic.json")
  private Resource createJsonScript;

  @Value("classpath:create_topic_with_too_short_name.json")
  private Resource topicWithTooShortName;

  @Value("classpath:update_topic.json")
  private Resource updateJsonScript;

  //	Es wird gegen den tatsächlichen Wert in der DB getestet
  @Test
  public void findNonExistingTopic() throws Exception {
    this.mockMvc.perform(get("/topics/4"))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  public void findExistingTopic() throws Exception {
    this.mockMvc.perform(get("/topics/1"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Religion")));
  }

  @Test
  public void findTopicStartingWith() throws Exception {
    this.mockMvc.perform(get("/topicsstartingwith/Re"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Religion")));
  }

  @Test
  @Transactional
  public void writeTopicTest() throws Exception {
    String content = StreamUtils
        .copyToString(createJsonScript.getInputStream(), StandardCharsets.UTF_8);
    this.mockMvc.perform(post("/topics/").contentType(MediaType.APPLICATION_JSON).content(content))
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().string(containsString("JSON2")));
  }

  @Test
  @Transactional
  public void writeTopicWithTooShortNameTest() throws Exception {
    String content = StreamUtils
        .copyToString(topicWithTooShortName.getInputStream(), StandardCharsets.UTF_8);
    this.mockMvc.perform(post("/topics/").contentType(MediaType.APPLICATION_JSON).content(content))
        .andExpect(status().is4xxClientError());
  }

  @Test
  @Transactional
  public void updateTopicTest() throws Exception {
    String content = StreamUtils
        .copyToString(updateJsonScript.getInputStream(), StandardCharsets.UTF_8);
    this.mockMvc.perform(put("/topics/1").contentType(MediaType.APPLICATION_JSON).content(content))
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.name", is("JSON2")));
  }

  @Test
  @Transactional
  public void updateTopicWithTooShortNameTest() throws Exception {
    String content = StreamUtils
        .copyToString(topicWithTooShortName.getInputStream(), StandardCharsets.UTF_8);
    this.mockMvc.perform(put("/topics/1").contentType(MediaType.APPLICATION_JSON).content(content))
        .andExpect(status().is4xxClientError());
  }

  @Test
  @Transactional
  public void deleteTopicTest() throws Exception {
    this.mockMvc.perform(delete("/topics/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful());
    this.mockMvc.perform(get("/topics/1"))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void deleteNonExistingTopicTest() throws Exception {
    this.mockMvc.perform(delete("/topics/1123").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
//        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//        .andExpect(jsonPath("$.timestamp", not(isEmptyString())))
//        .andExpect(jsonPath("$.status", is("404")))
//        .andExpect(jsonPath("$.error", is("Not Found")))
//        .andExpect(jsonPath("$.message", is("No class org.strupp.springboot.topic.Topic entity with id 1123 exists!")))
//        .andExpect(jsonPath("$.path", is("/topics/1123")));
  }
}
