package ua.com.foxminded.university.service;

import java.util.List;

import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.university.controller.dao.CourseDAO;
import ua.com.foxminded.university.model.Course;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.TimeTable;
import ua.com.foxminded.university.service.exception.UniversityServiceException;

@Service
public class CourseServiceImpl implements CourseService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CourseServiceImpl.class);
    
    private CourseDAO courseDAO;
    
    @Autowired
    public CourseServiceImpl(CourseDAO courseDAO) {
        this.courseDAO = courseDAO;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Course getCourse(int id) throws UniversityServiceException {
        LOGGER.debug("Try get course {}", id);
        try {
            LOGGER.debug("Course was successfully got {}", id);
            return courseDAO.getById(id);
        } catch (PersistenceException exception) {
            throw new UniversityServiceException("Course " + id + " couldn't be got", exception);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Course> getAllCourses() throws UniversityServiceException {
        LOGGER.debug("Try get all courses");
        try {
            LOGGER.debug("All courses was successfully got");
            return courseDAO.findAll();
        } catch (PersistenceException exception) {
            throw new UniversityServiceException("Courses list couldn't be got", exception);
        }
    }
    
    @Override
    @Transactional
    public List<Course> saveCourse(List<Course> coursesList) throws UniversityServiceException {
        LOGGER.debug("Try save courses list");
        try {
            LOGGER.debug("All courses was successfully saved");
            return courseDAO.saveAll(coursesList);          
        } catch (PersistenceException exception) {
            throw new UniversityServiceException("Courses list " + coursesList + " couldn't be saved", exception);
        }
    }
    
    @Override
    @Transactional
    public Course updateCourse(Course course) throws UniversityServiceException {
        LOGGER.debug("Try update course {}", course);
        try {
            LOGGER.debug("Course was successfully updated {}", course);
            return courseDAO.save(course);          
        } catch (PersistenceException exception) {
            throw new UniversityServiceException("Course " + course + " couldn't be updated", exception);
        }
    }
    
    @Override
    @Transactional
    public void deleteCourse(Course course) throws UniversityServiceException {
        LOGGER.debug("Try delete course {}", course);
        try {
            courseDAO.deleteById(course.getId());
            LOGGER.debug("Course was successfully deleted {}", course);
        } catch (PersistenceException exception) {
            throw new UniversityServiceException("Course " + course + " couldn't be deleted", exception);
        }
    } 
    /**
    @Override
    public Course addSubjectToCourse() throws UniversityServiceException {
        LOGGER.debug("Try add subject to course");
        try {
            Course course = null;
            List<Subject> subjectsList = subjectDAO.getAll();
            List<Course> coursesList = courseDAO.getAll();
            for (Subject subject : subjectsList) {
                for (int index = 0; index < subjectsList.size(); index++) {
                    course = Course.builder().id(coursesList.get(index).getId())
                            .name(coursesList.get(index).getName()).subject(subject).build();
                    courseDAO.updateSubjectInCourse(subject, course);
                }
            }
            LOGGER.info("Subject was successfully added to course");
            return course;
        } catch (UniversityDaoException exception) {
            throw new UniversityServiceException("Subject couldn't be added to Course", exception);
        }
    }
    
    @Override
    public Course deleteSubjectFromCourse(Subject subject, Course course) throws UniversityServiceException {
        LOGGER.debug("Try delete subject from course {}, {}", subject, course);
        try {
            LOGGER.info("Subject was successfully deleted from course {}, {}", subject, course);
            return courseDAO.deleteSubjectFromCourse(subject, course);
        } catch (UniversityDaoException exception) {
            throw new UniversityServiceException("Subject " + subject + " couldn't be deleted from Course " + course,
                    exception);
        }
    }
    
    @Override
    public List<Course> addGroupToCourse() throws UniversityServiceException {
        LOGGER.debug("Try add groups list to courses list");
        try {
            Course course = null;
            List<Course> updateCoursesList = new ArrayList<>();
            List<Group> groupsList = groupDAO.getAll();
            List<Course> coursesList = courseDAO.getAll();
            for (Group group : groupsList) {
                for (int index = 0; index < groupsList.size(); index++) {
                    course = Course.builder().id(coursesList.get(index).getId())
                            .name(coursesList.get(index).getName())
                            .groupList((Arrays.asList(Group.builder().id(group.getId()).build()))).build();
                    updateCoursesList.add(course);
                    courseDAO.updateGroupInCourse(groupsList, updateCoursesList);
                }
            }
            LOGGER.info("Groups list was successfully added to Courses list");
            return updateCoursesList;           
        } catch (UniversityDaoException exception) {
            throw new UniversityServiceException("Groups list couldn't be added to Courses list", exception);
        }
    }
    
    @Override
    public Course deleteGroupFromCourse(Group group, Course course) throws UniversityServiceException {
        LOGGER.debug("Try delete group from course {}, {}", group, course);
        try {
            LOGGER.info("Group was successfully deleted from course {}, {}", group, course);
            return courseDAO.deleteGroupFromCourse(group, course);
        } catch (UniversityDaoException exception) {
            throw new UniversityServiceException("Group " + group + " couldn't be deleted from Course " + course, exception);
        }
    }
    
    @Override
    public Course addTimeTableToCourse() throws UniversityServiceException {
        LOGGER.debug("Try add timeTibles list to course");
        try {
            Course course = null;
            List<TimeTable> timeTablesList = timeTableDAO.getAll();
            List<Course> coursesList = courseDAO.getAll();
            for (TimeTable timeTable : timeTablesList) {
                for (int index = 0; index < timeTablesList.size(); index++) {
                    course = Course.builder().id(coursesList.get(index).getId())
                            .name(coursesList.get(index).getName())
                            .timeTableList(Arrays.asList(TimeTable.builder().id(timeTable.getId()).build()))
                            .build();
                    courseDAO.updateTimeTableInCourse(timeTablesList, course);
                }
            }
            LOGGER.info("TimeTibles list was successfully added to course {}, {}", timeTablesList, course);
            return course;
        } catch (UniversityDaoException exception) {
            throw new UniversityServiceException("TimeTables list couldn't be added to Course", exception);
        }       
    }
    
    @Override
    public int countCourses() throws UniversityServiceException {
        LOGGER.debug("Try count courses");
        try {
            List<Course> coursesList = courseDAO.getAll();
            LOGGER.info("Courses was successfully counted");
            return coursesList.size();
        } catch (UniversityDaoException exception) {
            throw new UniversityServiceException("Courses list couldn't be counted", exception);
        }
    }
    
    @Override
    public int countGroupsInCourse(Course course) throws UniversityServiceException {
        LOGGER.debug("Try count groups in course");
        try {
            course = courseDAO.get(course.getId());
            LOGGER.info("Groups was successfully counted in course");
            return course.getGroupList().size();
        } catch (UniversityDaoException exception) {
            throw new UniversityServiceException("Group couldn't be counted in Courses list", exception);
        }
    }
    
    @Override
    public int countTimeTableInCourse(Course course) throws UniversityServiceException {
        LOGGER.debug("Try count timeTables in course");
        try {
            course = courseDAO.get(course.getId());
            LOGGER.info("TimeTables was successfully counted in course");
            return course.getTimeTableList().size();
        } catch (UniversityDaoException exception) {
            throw new UniversityServiceException("TimeTable couldn't be counted in Courses list", exception);
        }
    }
    
    @Override
    public Course updateSubjectInCourse(Subject subject, Course course) throws UniversityServiceException {
        LOGGER.debug("Try update subject to course {}, {}", subject, course);
        try {
            LOGGER.info("Subject was successfully updated to course {}, {}", subject, course);
            return courseDAO.updateSubjectInCourse(subject, course);
        } catch (UniversityDaoException exception) {
            throw new UniversityServiceException("Subject couldn't be updated in Course", exception);
        }
    }
    
    @Override
    public List<Course> updateGroupInCourse(List<Group> groupsList, List<Course> coursesList) throws UniversityServiceException {
        LOGGER.debug("Try update groups list to courses list {}, {}", groupsList, coursesList);
        try {
            LOGGER.info("Groups list was successfully updated to Courses list {}, {}", groupsList, coursesList);
            return courseDAO.updateGroupInCourse(groupsList, coursesList);
        } catch (UniversityDaoException exception) {
            throw new UniversityServiceException("Groups list couldn't be updated in Courses list", exception);
        }
    }
    
    @Override
    public Course updateTimeTableInCourse(List<TimeTable> timeTablesList, Course course) throws UniversityServiceException {
        LOGGER.debug("Try update timeTibles list to course {}, {}", timeTablesList, course);
        try {
            LOGGER.info("TimeTibles list was successfully updated in course {}, {}", timeTablesList, course);
            return courseDAO.updateTimeTableInCourse(timeTablesList, course);
        } catch (UniversityDaoException exception) {
            throw new UniversityServiceException("TimeTables list couldn't be updated to Courses list", exception);
        }
    }
    */

    @Override
    public Course updateSubjectInCourse(Subject subject, Course course) throws UniversityServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Course> updateGroupInCourse(List<Group> groupsList, List<Course> coursesList)
            throws UniversityServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Course updateTimeTableInCourse(List<TimeTable> timeTable, Course course)
            throws UniversityServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Course addSubjectToCourse() throws UniversityServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Course deleteSubjectFromCourse(Subject subject, Course course) throws UniversityServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Course> addGroupToCourse() throws UniversityServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Course deleteGroupFromCourse(Group group, Course course) throws UniversityServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Course addTimeTableToCourse() throws UniversityServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int countCourses() throws UniversityServiceException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int countGroupsInCourse(Course course) throws UniversityServiceException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int countTimeTableInCourse(Course course) throws UniversityServiceException {
        // TODO Auto-generated method stub
        return 0;
    }
}
