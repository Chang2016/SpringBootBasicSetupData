package org.strupp.springboot.course;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

/**
 * Erweiterung des Standardrepository
 * @author michaelstrupp
 *
 */
@Repository
public class CustomCourseRepositoryImpl<T, ID extends Serializable> implements CustomCourseRepository {
	
	@PersistenceContext
	EntityManager entityManager;
	
	public CustomCourseRepositoryImpl() {}

	@Override
	public List<Course> getCoursesHavingTopic(String topic) {
		TypedQuery<Course> query = entityManager.createNamedQuery("Course.getCoursesHavingTopic", Course.class);
		query.setParameter("name", topic);
        return query.getResultList();
	}
}
