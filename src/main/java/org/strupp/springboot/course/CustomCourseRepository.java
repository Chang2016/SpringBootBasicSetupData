package org.strupp.springboot.course;

import java.util.List;

public interface CustomCourseRepository {
	List<Course> getCoursesHavingTopic(String topic);
}
