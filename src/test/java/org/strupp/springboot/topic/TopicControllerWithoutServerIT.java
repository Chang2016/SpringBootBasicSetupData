package org.strupp.springboot.topic;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
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
//    sorgt dafür dass TransactionalTestExecutionListener die Transaktion zurückrollt, also die DB nicht geändert wird
  @Transactional
  public void writeTopicTest() throws Exception {
    String content = StreamUtils
        .copyToString(createJsonScript.getInputStream(), Charset.forName("UTF-8"));
    this.mockMvc.perform(post("/topics/").contentType(MediaType.APPLICATION_JSON).content(content))
        .andExpect(status().is2xxSuccessful());
  }
}
