package ua.com.foxminded.university.controller.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.university.model.Course;

@Repository
public interface CourseDAO extends JpaRepository<Course, Integer> {

}
