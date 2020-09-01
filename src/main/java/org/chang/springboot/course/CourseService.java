package org.chang.springboot.course;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.chang.springboot.student.Student;
import org.chang.springboot.student.StudentRepository;
import org.chang.springboot.topic.Topic;
import org.chang.springboot.topic.TopicRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.chang.springboot.exceptions.ResourceNotFoundException;

@Service
public class CourseService {

	private static final Logger logger = LoggerFactory.getLogger(CourseService.class);

	@Autowired
	private CourseJmsMessageSender courseJmsMessageSender;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private CustomCourseRepository customCourseRepository;

	@Autowired
	private TopicRepository topicRepository;

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private CourseStatusValidator courseStatusValidator;

	public Page<Course> retrieveCourses(int p, int size) {
		Pageable page = PageRequest.of(p, size);
		Page<Course> res = courseRepository.findAll(page);
		return res;
	}
	
	public Optional<Course> retrieveCourse(long id) {
		Optional<Course> opt = courseRepository.findById(id);
		return opt;
	}
	
	@Transactional
	public CourseResult addStudent(long courseId, Student student) {
		Optional<Course> maybeCourse = courseRepository.findById(courseId);
		CourseStatusEnum courseStatusEnum = courseStatusValidator.checkCourse(maybeCourse, student);
		if(courseStatusEnum == CourseStatusEnum.OK) {
			if (maybeCourse.isPresent()) {
				Course course = maybeCourse.get();
				Student result = studentRepository.save(student);
				course.addStudent(result);
				Course save = courseRepository.save(course);
				return CourseResult.builder().error(false).course(save).courseStatusEnum(courseStatusEnum).build();
			}
		}
		return CourseResult.builder().error(true).courseStatusEnum(courseStatusEnum).build();
	}

	public List<Course> retrieveCoursesByTopicName(String topicName) {
		return customCourseRepository.getCoursesHavingTopic(topicName);
	}
	
	@Transactional
	public CourseResult createCourse(Course course) {
		Optional<Topic> maybeTopic = Optional.empty();
		if(null != course.getTopic())
			maybeTopic = topicRepository.findById(course.getTopic().getId());
		CourseStatusEnum courseStatusEnum = courseStatusValidator.checkCourse(course, maybeTopic);
		if(courseStatusEnum == CourseStatusEnum.OK) {
//		try {
			Course save = courseRepository.save(course);
//		courseJmsMessageSender.sendMessage(save);
			return CourseResult.builder().error(false).course(save).courseStatusEnum(courseStatusEnum).build();
//		} catch (Exception e) {
//			throw new ResourceNotFoundException(e.getMessage());
//		}
		}
		return CourseResult.builder().error(true).courseStatusEnum(courseStatusEnum).build();
	}
	
	@Transactional
	public CourseResult updateCourse(Course course) {
		Optional<Topic> maybeTopic = Optional.empty();
		if(null != course.getTopic())
			maybeTopic = topicRepository.findById(course.getTopic().getId());
		CourseStatusEnum courseStatusEnum = courseStatusValidator.checkCourse(course, maybeTopic);
		if(courseStatusEnum == CourseStatusEnum.OK) {
			try {
//				List<Student> detachedStudents = findDetachedStudents(course);
//				course.getStudents().removeAll(detachedStudents);
//				List<Student> updatedStudents = studentRepository.saveAll(detachedStudents);
//				replaceDetachedWithUpdatedStudents(course, updatedStudents);
				Course save = courseRepository.save(course);
				return CourseResult.builder().error(false).course(save).courseStatusEnum(courseStatusEnum).build();
			} catch (InvalidDataAccessApiUsageException e) {
				logger.error("An error occured", e);
			} catch (Exception e) {
				throw new ResourceNotFoundException(e.getMessage());
			}
		}
		return CourseResult.builder().error(true).courseStatusEnum(courseStatusEnum).build();
	}
	
	private void replaceDetachedWithUpdatedStudents(Course course, List<Student> updatedEntities) {
		course.getStudents().removeAll(updatedEntities);
		updatedEntities.forEach(course::addStudent);
	}
	
	private List<Student> findDetachedStudents(Course course) {
		return course.getStudents().stream().filter(s -> s.getId() != null).collect(Collectors.toList());
	}
	
	@Transactional
	public void deleteCourse(long id) {
		try {
			courseRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			ResourceNotFoundException re = new ResourceNotFoundException(e);
			throw re;
		}
	}
}
