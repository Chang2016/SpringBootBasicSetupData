package org.strupp.springboot.course;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.strupp.springboot.exceptions.ResourceNotFoundException;
import org.strupp.springboot.model.student.Student;
import org.strupp.springboot.model.student.StudentRepository;

@Service
public class CourseService {
	
	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private CustomCourseRepository customCourseRepository;

	@Autowired
	private CourseRepository courseRepository;
	
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
	public Student addStudent(long courseId, Student student) {
		Optional<Course> course = courseRepository.findById(courseId);
		course.ifPresent(c -> c.addStudent(student));
		Student result = studentRepository.save(student);
		return result;
	}
	
	public List<Course> retrieveCoursesByTopicName(String topicName) {
		return customCourseRepository.getCoursesHavingTopic(topicName);
	}
	
	@Transactional
	public Course createCourse(Course course) {
//		try {
			return courseRepository.save(course);
//		} catch (Exception e) {
//			throw new ResourceNotFoundException(e.getMessage());
//		}
	}
	
	@Transactional
	public Course updateCourse(Course course) {
		try {
//			List<Student> detachedStudents = findDetachedStudents(course);
//			course.getStudents().removeAll(detachedStudents);
//			List<Student> updatedStudents = studentRepository.saveAll(detachedStudents);
//			replaceDetachedWithUpdatedStudents(course, updatedStudents);
			return courseRepository.save(course);
		} catch (Exception e) {
			throw new ResourceNotFoundException(e.getMessage());
		}
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
