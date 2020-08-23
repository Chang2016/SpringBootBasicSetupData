package org.chang.springboot.student;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.chang.springboot.course.Course;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Student {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Basic
	private Instant created;
	@Column(nullable = false)
	private String name;
	@Basic
	private LocalDate birthday;
//	mappedBy macht die Relation bidirektional und zeigt auf den owner der Relation, also müssen alle Änderungen auf der Course Seite gemacht 
//	werden!
	@JsonIgnore
	@ManyToMany(mappedBy = "students",
			cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Set<Course> courses = new HashSet<>();
	
	public Student() {
	}
	
	public Instant getCreated() {
		return created;
	}
	public void setCreated(Instant created) {
		this.created = created;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LocalDate getBirthday() {
		return birthday;
	}
	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}
	public Set<Course> getCourses() {
		return courses;
	}
//	public void setCourses(Set<Course> courses) {
//		this.courses = courses;
//	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
