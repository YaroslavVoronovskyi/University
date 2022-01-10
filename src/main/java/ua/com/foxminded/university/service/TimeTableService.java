package ua.com.foxminded.university.service;

import java.util.List;

import ua.com.foxminded.university.model.Auditory;
import ua.com.foxminded.university.model.TimeTable;
import ua.com.foxminded.university.service.exception.UniversityServiceException;

public interface TimeTableService {
    public TimeTable getTimeTable(int id) throws UniversityServiceException;
    
    public List<TimeTable> getAllTimeTables() throws UniversityServiceException;
    
    public List<TimeTable> saveTimeTable(List<TimeTable> timeTablesList) throws UniversityServiceException;
    
    public TimeTable updateTimeTable(TimeTable timeTable) throws UniversityServiceException;
    
    public void delete(TimeTable timeTable) throws UniversityServiceException;
    
    public Auditory getAuditory(int id) throws UniversityServiceException;
    
    public List<Auditory> getAllAuditories() throws UniversityServiceException;
    
    public List<Auditory> saveAuditory(List<Auditory> auditoriesList) throws UniversityServiceException;
    
    public Auditory updateAuditory(Auditory auditory) throws UniversityServiceException;
    
    public void deleteAuditory(Auditory auditory) throws UniversityServiceException;
    
    public TimeTable updateAuditoryInTimeTamle(Auditory auditory, TimeTable timeTable) throws UniversityServiceException;
    
    public TimeTable addAuditoryToTimeTable() throws UniversityServiceException;
    
    public TimeTable deleteAuditoryFromTimeTamle(Auditory auditory, TimeTable timeTable) throws UniversityServiceException;
}
