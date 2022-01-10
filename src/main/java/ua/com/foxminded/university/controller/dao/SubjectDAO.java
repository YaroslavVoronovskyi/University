package ua.com.foxminded.university.controller.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.university.model.Subject;

@Repository
public interface SubjectDAO extends JpaRepository<Subject, Integer> {

}
