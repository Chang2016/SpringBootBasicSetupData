package org.chang.springboot.course;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
	//Methode wird automatisch erzeugt Methodenname muss 'findBy'+ [Propertyname] sein
	List<Course> findByName(String name);
	//wenn es um eine Klasse wie bspw Topic geht muss hinten noch die ID von Topic angehängt werden
	List<Course> findByTopicId(long topicId);
}
