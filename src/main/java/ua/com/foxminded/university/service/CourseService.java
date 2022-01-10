package ua.com.foxminded.university.service;

import java.util.List;

import ua.com.foxminded.university.model.Course;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.TimeTable;
import ua.com.foxminded.university.service.exception.UniversityServiceException;

public interface CourseService {
    public Course getCourse(int id) throws UniversityServiceException;
    
    public List<Course> getAllCourses() throws UniversityServiceException;
    
    public List<Course> saveCourse(List<Course> coursesList) throws UniversityServiceException;
    
    public Course updateCourse(Course course) throws UniversityServiceException;
    
    public void deleteCourse(Course course) throws UniversityServiceException;
    
    public Course updateSubjectInCourse(Subject subject, Course course) throws UniversityServiceException;
    
    public List<Course> updateGroupInCourse(List<Group> groupsList, List<Course> coursesList) throws UniversityServiceException;
    
    public Course updateTimeTableInCourse(List<TimeTable> timeTable, Course course) throws UniversityServiceException;
    
    public Course addSubjectToCourse() throws UniversityServiceException;
    
    public Course deleteSubjectFromCourse(Subject subject, Course course) throws UniversityServiceException;
    
    public List<Course> addGroupToCourse() throws UniversityServiceException;
    
    public Course deleteGroupFromCourse(Group group, Course course) throws UniversityServiceException;
    
    public Course addTimeTableToCourse() throws UniversityServiceException;
    
    public int countCourses() throws UniversityServiceException;
    
    public int countGroupsInCourse(Course course) throws UniversityServiceException;
    
    public int countTimeTableInCourse(Course course) throws UniversityServiceException;
}
