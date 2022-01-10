package ua.com.foxminded.university.controller.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.university.model.Auditory;

@Repository
public interface AuditoryDAO extends JpaRepository<Auditory, Integer> {

}
