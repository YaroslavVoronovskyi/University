package ua.com.foxminded.university.controller.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ua.com.foxminded.university.model.TimeTable;

@Repository
public interface TimeTableDAO extends JpaRepository<TimeTable, Integer> {

}
