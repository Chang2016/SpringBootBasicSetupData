package org.chang.springboot.course;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;
import org.chang.springboot.SecurityConfig;
import org.chang.springboot.SpringBootBasicDataMain;
import org.chang.springboot.student.StudentTransformer;
import org.chang.springboot.topic.TopicTransformer;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@Ignore
@RunWith(SpringRunner.class)
@WebMvcTest(CourseController.class)
@ContextConfiguration(classes = {SpringBootBasicDataMain.class,
    SecurityConfig.class})
public class CourseSecurityWebMvcTest {

  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TopicTransformer topicTransformer;

  @MockBean
  private StudentTransformer studentTransformer;

  @MockBean
  private CourseJmsMessageSender courseJmsMessageSender;

  @MockBean
  private CourseService courseService;

  @MockBean
  private CourseTransformer courseTransformer;

  @Before
  public void init() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
        .apply(springSecurity())
        .build();
  }

  @Test
  @WithMockUser(roles="ADMIN")
  public void loadCourses() throws Exception {
    when(courseService.retrieveCourse(1)).thenReturn(Optional.of(new Course()));
    this.mockMvc.perform(get("https://localhost:8443/courses/1")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  @WithAnonymousUser
  public void loadCoursesWithUnauthorizedUser() throws Exception {
    when(courseService.retrieveCourse(1)).thenReturn(Optional.of(new Course()));
    this.mockMvc.perform(get("https://localhost:8443/courses/1")
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void loadCoursesWithInvalidUser() throws Exception {
    when(courseService.retrieveCourse(1)).thenReturn(Optional.of(new Course()));
    this.mockMvc.perform(get("https://localhost:8443/courses/1")
        .with(user("ooos").password("123").roles(""))
        .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

}
