package org.strupp.springboot.course;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

import org.strupp.springboot.model.student.Student;
import org.strupp.springboot.topic.Topic;

@Entity
@NamedQuery(name = "Course.getCoursesHavingTopic", query = "SELECT c FROM Course c WHERE c.topic.name = :name")
public class Course {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String name;
	private LocalDate startDate;
	@ManyToOne
	private Topic topic;
	
//	Wenn CascadeType.REMOVE in der Relation verwendet wird, wird nicht nur der Eintrag in der JoinTable gelöscht, sondern 
//	auch der Student, wenn zusätzlich auf der Student Seite auch CascadeType.REMOVE verwendet wird, wird auch der 
//	Course gelöscht.
//	ManyToMany muss immer Set anstatt List verwenden, da sonst beim delete zuerst alles aus der join table gelöscht wird
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "course_student",
    joinColumns = {
        @JoinColumn(
            name = "course_id", 
            referencedColumnName = "id"
        )
    },
    inverseJoinColumns = {
        @JoinColumn(
            name = "student_id", 
            referencedColumnName = "id"
        )
    })
	private Set<Student> students = new HashSet<>();
	
	public Course() {}
	public Course(long id, String name, LocalDate startDate) {
		super();
		this.id = id;
		this.name = name;
		this.startDate = startDate;
	}
	
	public void addStudent(Student student) {
		student.getCourses().add(this);
		this.students.add(student);
	}
	
	public void removeStudent(Student student) {
		this.students.remove(student);
		student.getCourses().remove(this);
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}
	public Set<Student> getStudents() {
		return students;
	}
}
