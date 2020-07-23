package org.strupp.springboot.course;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.strupp.springboot.topic.Topic;
import org.strupp.springboot.topic.TopicRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Before;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.hasSize;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

//Der Test initialisiert die H2-DB und lässt die Tests gegen diese laufen.
/*
 * @RunWith(SpringRunner.class) tells JUnit to run using Spring’s testing support. SpringRunner is the new name for 
 * SpringJUnit4ClassRunner, it’s just a bit easier on the eye.
 * @SpringBootTest is saying “bootstrap with Spring Boot’s support” (e.g. load application.properties and give me all the 
 * Spring Boot goodness)
 * The webEnvironment attribute allows specific “web environments” to be configured for the test. You can start tests with 
 * a MOCK servlet environment or with a real HTTP server running on either a RANDOM_PORT or a DEFINED_PORT.
 * If we want to load a specific configuration, we can use the classes attribute of @SpringBootTest. In this example, 
 * we’ve omitted classes which means that the test will first attempt to load @Configuration from any inner-classes, and 
 * if that fails, it will search for your primary @SpringBootApplication class.
 */

// Die  Requests mit MockMvc machen!!!!!!!!!!!!!!!!!!

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
@AutoConfigureMockMvc
@Sql({"/data-h2.sql"})
public class CourseServiceIT {
	
	@Autowired
	private WebApplicationContext wac;
	
	private MockMvc mockMvc;
	
	@LocalServerPort
	private int port;
	
	TestRestTemplate restTemplate = new TestRestTemplate();

	HttpHeaders headers = new HttpHeaders();
		
	@Autowired
	private CourseRepository courseRepository;
	
	@Autowired
	private TopicRepository topicRepository;

	@Value("${spring.datasource.driverClassName}")
	private String datasourceName;

	@Value("classpath:course.json")
	private Resource courseJsonScript;
	
	@Value("classpath:update_course.json")
	private Resource updateCourseJsonScript;
	
	@Value("classpath:update_course_no_topic.json")
	private Resource updateCourseNoTopicJsonScript;
	
	@Value("classpath:course_without_date.json")
	private Resource courseWithoutDateJsonScript;
	
	@Before
	public void init() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void checkIfTestEnvironmentIsLoaded() {
		assertThat(datasourceName, is("org.h2.Driver"));
	}

	@Test
	public void testCourseGet() throws Exception {
		this.mockMvc.perform(get("https://localhost:" + port + "/courses/1")
					.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
        	.andExpect(status().is2xxSuccessful())
	        .andExpect(jsonPath("$.id", is(1)))
	        .andExpect(jsonPath("$.name", is("Christentum")));
	}
	
	@Test
	public void testInitialization() {
		List<Topic> topics = (List<Topic>) topicRepository.findAll();
		assertThat(topics.size(), is(1));
		Topic topic = topics.get(0);
		assertThat(topic.getId(), is(1L));
		assertThat(topic.getName(), is("Religion"));
		List<Course> courses = (List<Course>) courseRepository.findAll();
		assertThat(Arrays.asList("Christentum", "Islam", "Judentum").stream()
			.allMatch(r -> courses.stream()
					.map(Course::getName)
					.filter(i -> r.equals(i))
					.findAny().isPresent()), is(true));
	}

	@Test
	public void testCoursesGet() throws Exception {
		this.mockMvc.perform(get("https://localhost:" + port + "/courses/?page=0&size=5")
				.contentType(MediaType.APPLICATION_JSON))
//		    .andDo(print())
	    	.andExpect(status().isOk())
	    	.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8))
	    	.andExpect(jsonPath("$", hasSize(3)))
	        .andExpect(jsonPath("$[0].id", is(1)))
	        .andExpect(jsonPath("$[0].name", is("Christentum")))
	        .andExpect(jsonPath("$[1].id", is(2)))
	        .andExpect(jsonPath("$[1].name", is("Islam")))
	        .andExpect(jsonPath("$[2].id", is(3)))
	        .andExpect(jsonPath("$[2].name", is("Judentum")));
	}
	
	@Test
	public void testCourseGetHavingtopicName() throws Exception {
		this.mockMvc.perform(get("https://localhost:" + port + "/courses/topics/Religion")
					.contentType(MediaType.APPLICATION_JSON))
//			    .andDo(print())
		    	.andExpect(status().isOk())
		    	.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_UTF8))
		    	.andExpect(jsonPath("$", hasSize(3)))
		        .andExpect(jsonPath("$[0].id", is(1)))
		        .andExpect(jsonPath("$[0].name", is("Christentum")))
		        .andExpect(jsonPath("$[1].id", is(2)))
		        .andExpect(jsonPath("$[1].name", is("Islam")))
		        .andExpect(jsonPath("$[2].id", is(3)))
		        .andExpect(jsonPath("$[2].name", is("Judentum")));
	}
	
//	@Ignore
	@Test
	@Transactional
	public void testCoursePost() throws Exception {
		String content = StreamUtils.copyToString(courseJsonScript.getInputStream(), Charset.forName("UTF-8"));
        mockMvc.perform(post("https://localhost:" + port + "/courses/")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(content))
        	.andExpect(status().is2xxSuccessful());
        int siz = ((List<Course>) courseRepository.findAll()).size();
        assertThat(siz, is(4));
	}
	
	@Test
	@Transactional
	public void testCoursePut() throws Exception {
//		Course course = convertResourceToClass(Course.class, courseJsonScript);
		String content = StreamUtils.copyToString(updateCourseJsonScript.getInputStream(), Charset.forName("UTF-8"));
		mockMvc.perform(put("https://localhost:" + port + "/courses/1")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(content))
        	.andExpect(status().is2xxSuccessful());
		Optional<Course> opt = courseRepository.findById(1L);
		assertThat(opt.isPresent(), is(true));
		assertThat(opt.get().getName().equals("Buddismus"), is(true));
		assertThat(opt.get().getId(), is(1L));
//		JSONAssert.assertEquals(expected, response.getBody(), false);
	}
	
	@Test
	@Transactional
	public void testCoursePutCourseWithoutTopic() throws Exception {
//		Course course = convertResourceToClass(Course.class, courseJsonScript);
		String content = StreamUtils.copyToString(updateCourseNoTopicJsonScript.getInputStream(), Charset.forName("UTF-8"));
		mockMvc.perform(put("https://localhost:" + port + "/courses/1")
        		.contentType(MediaType.APPLICATION_JSON)
        		.content(content))
        	.andExpect(status().is2xxSuccessful());
		Optional<Course> opt = courseRepository.findById(1L);
		assertThat(opt.isPresent(), is(true));
		assertThat(opt.get().getTopic(), is(nullValue()));
		assertThat(opt.get().getName().equals("Buddismus"), is(true));
		assertThat(opt.get().getId(), is(1L));
//		JSONAssert.assertEquals(expected, response.getBody(), false);
	}
	
	@Test
	@Transactional
	public void testCourseDelete() throws Exception {
		mockMvc.perform(delete("https://localhost:" + port + "/courses/2"))
        	.andExpect(status().isAccepted());
		Optional<Course> opt = courseRepository.findById(2L);
		assertThat(opt.isPresent(), is(false));
	}
	
	@Test
	@Transactional
	public void testCourseDeleteNonExistsing() throws Exception {
		mockMvc.perform(delete("https://localhost:" + port + "/courses/200"))
        	.andExpect(status().isNotFound());
	}
	
	@SuppressWarnings("unused")
	private <T> T convertResourceToClass(Class<T> clazz, Resource json) throws Exception {
		Optional<String> jsonString = readResource(json);
		if(jsonString.isPresent())
			return convertJsonStringToClass(clazz, jsonString.get());
		else return invokeConstructor(clazz);
	}
	
	private <T> T convertJsonStringToClass(Class<T> clazz, String jsonString) throws Exception {
		ObjectMapper objectMapper = createMapper();
		return objectMapper.readValue(jsonString, clazz);
	}
	
	private <T> T invokeConstructor(Class<T> clazz) throws Exception {
		return clazz.newInstance();
	}
	
	private Optional<String> readResource(Resource json) {
		try	{
		return Optional.ofNullable(StreamUtils.copyToString(courseJsonScript.getInputStream(), Charset.forName("UTF-8")));
		} catch (Exception e) {
			e.printStackTrace();
			return Optional.empty();
		} 
	}
	
	private ObjectMapper createMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		return objectMapper;
	}
}
