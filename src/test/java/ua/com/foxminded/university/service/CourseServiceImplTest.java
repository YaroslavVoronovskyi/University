package ua.com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import ua.com.foxminded.university.controller.dao.CourseDAO;
import ua.com.foxminded.university.controller.dao.GroupDAO;
import ua.com.foxminded.university.controller.dao.SubjectDAO;
import ua.com.foxminded.university.controller.dao.TimeTableDAO;
import ua.com.foxminded.university.controller.exception.UniversityDaoException;
import ua.com.foxminded.university.model.Course;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.TimeTable;
import ua.com.foxminded.university.service.exception.UniversityServiceException;

public class CourseServiceImplTest extends AbstractServiceImplTest {
    private static final DateTimeFormatter TIME_FORMATT = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Mock
    private CourseDAO courseMock;
    @Mock
    private GroupDAO groupMock;
    @Mock
    private SubjectDAO subjectMock;
    @Mock
    private TimeTableDAO timeTableMock;
    @InjectMocks
    private CourseServiceImpl courseServiceImpl;
    
    @Test
    public void checkMethodGet() throws UniversityServiceException, UniversityDaoException {
        initMocksForOneObject();
        assertEquals(courseServiceImpl.getCourse(1), createFakeCourse());
    }
    
    @Test
    public void checkMethodGetAll() throws UniversityServiceException, UniversityDaoException {
        initMocksForManyObjects();
        List<Course> expectedCoursesList = courseServiceImpl.getAllCourses();
        List<Course> actualCoursesList = createFakeCoursesList();
        assertEquals(expectedCoursesList.get(0), actualCoursesList.get(0));
        assertEquals(expectedCoursesList.get(1), actualCoursesList.get(1));
        assertEquals(expectedCoursesList.get(2), actualCoursesList.get(2));
    }
    
    @Test
    public void checkMethodDelete() throws UniversityServiceException, UniversityDaoException {
        initMocksForManyObjects();
        Course course = Course.builder().id(1).build();
        courseServiceImpl.deleteCourse(course);
        assertFalse(courseServiceImpl.getAllCourses().contains(course));
    }
    
    @Test
    public void checkMethodUpdate() throws UniversityServiceException, UniversityDaoException {
        initMocksForOneObject();
        Course course = Course.builder().id(1).name("SEVENTH").build();
        courseServiceImpl.updateCourse(course);
        assertEquals(course.getName(), "SEVENTH");
    }
    
    @Test
    public void checkMethodSave() throws UniversityServiceException, UniversityDaoException {
        initMocksForManyObjects();
        Course course = Course.builder().id(4).name("FOURTH").build();
        List<Course> coursesList = courseServiceImpl.getAllCourses();
        coursesList.add(course);
        courseServiceImpl.saveCourse(coursesList);     
        assertTrue(courseServiceImpl.getAllCourses().contains(course));
    }
    
    @Test
    public void chekMethodCountCourses() throws UniversityServiceException, UniversityDaoException {
        initMocksForManyObjects();
        assertEquals(courseServiceImpl.countCourses(), 3);
    }
    
    @Test
    public void checkMethodUpdateSubjectInCourse() throws UniversityServiceException, UniversityDaoException {
        initMocksForOneObject();
        Course expectedCourse = courseMock.getById(1);
        Course actualCourse = Course.builder().id(1).name("FIRST")
                .groupList(Arrays.asList(Group.builder().id(1).name("QW-11").build()))
                .timeTableList(Arrays
                        .asList(TimeTable.builder().id(1).startTime(LocalTime.parse("07:00:00", TIME_FORMATT))
                                .endTime(LocalTime.parse("08:30:00", TIME_FORMATT))
                                .date(LocalDate.parse("2021-04-15", DATE_FORMATT)).build()))
                .build();
        Subject expectedSubject = subjectMock.getById(1);
        Subject actualSubject = Subject.builder().id(1).name("Math").description(
                "Math includes the study of such topics as number theory, algebra, geometry and mathematical analysis")
                .build();
        courseServiceImpl.updateSubjectInCourse(expectedSubject, expectedCourse);
        assertEquals(expectedCourse, actualCourse);
        assertEquals(expectedSubject, actualSubject);
    }
    
    @Test
    public void checkMethodUpdateGroupstInCourses() throws UniversityServiceException, UniversityDaoException {
        initMocksForManyObjects();
        List<Course> expectedCourseList = courseMock.findAll();
        List<Course> actualCourseList = createFakeCoursesList();
        List<Group> expectedGroupsList = groupMock.findAll();
        List <Group> actualGroupsList = createFakeGroupsList(); 
        courseServiceImpl.updateGroupInCourse(expectedGroupsList, expectedCourseList);
        assertEquals(expectedCourseList.get(1).getId(), actualCourseList.get(1).getId());
        assertEquals(expectedCourseList.get(1).getName(), actualCourseList.get(1).getName());
        assertEquals(expectedGroupsList.get(1).getId(), actualGroupsList.get(1).getId());
        assertEquals(expectedGroupsList.get(1).getName(), actualGroupsList.get(1).getName());
    }
    
    @Test
    public void checkMethodUpdateTimeTablestInCourses() throws UniversityServiceException, UniversityDaoException {
        initMocksForOneObject();
        initMocksForManyObjects();
        Course expectedCourse = courseMock.getById(1);
        Course actualCourse = createFakeCourse();
        List<TimeTable> expectedTimeTablesList = timeTableMock.findAll();
        List <TimeTable> actualTimeTablesList = createFakeTimeTablesList(); 
        courseServiceImpl.updateTimeTableInCourse(expectedTimeTablesList, expectedCourse);
        assertEquals(expectedCourse.getId(), actualCourse.getId());
        assertEquals(expectedCourse.getName(), actualCourse.getName());
        assertEquals(expectedTimeTablesList.get(1).getId(), actualTimeTablesList.get(1).getId());
        assertEquals(expectedTimeTablesList.get(1).getStartTime(), actualTimeTablesList.get(1).getStartTime());
        assertEquals(expectedTimeTablesList.get(1).getEndTime(), actualTimeTablesList.get(1).getEndTime());
        assertEquals(expectedTimeTablesList.get(1).getDate(), actualTimeTablesList.get(1).getDate());
    }
    
    @Test
    public void checkMethodAddSubjectToCourse() throws UniversityServiceException, UniversityDaoException {
        initMocksForManyObjects();
        Course course = courseServiceImpl.addSubjectToCourse();
        assertEquals(course.getSubject().getId(), 3);
    }
    
    @Test
    public void checkMethodAddGroupsToCourses() throws UniversityServiceException, UniversityDaoException {
        initMocksForManyObjects();
        List<Course> coursesList = courseServiceImpl.addGroupToCourse();
        Course course = coursesList.get(1);
        assertEquals(course.getGroupList().stream().map(Group::getId).collect(Collectors.toList())
                .get(Group.builder().build().getId()), 1);
    }
    
    @Test
    public void checkMethodAddTimeTablesToCourse() throws UniversityServiceException, UniversityDaoException {
        initMocksForManyObjects();
        initMocksForOneObject();
        Course course = courseServiceImpl.addTimeTableToCourse();
        assertEquals(course.getTimeTableList().stream().map(TimeTable::getId).collect(Collectors.toList())
                .get(TimeTable.builder().build().getId()), 1);
    }
    /**
    @Test
    public void checkMethodDeleteSubjectFromCourse() throws UniversityServiceException, UniversityDaoException {
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        Course course = createFakeCourseWithSubject();
        Subject subject = createFakeSubject();         
        courseServiceImpl.deleteSubjectFromCourse(subject, course);
        Mockito.verify(courseMock, Mockito.times(1)).deleteSubjectFromCourse(subject, course);
        courseServiceImpl.getCourse(1);
        Mockito.verify(courseMock, Mockito.times(1)).get(captor.capture());
        assertEquals(1, captor.getValue());
    }
    
    @Test
    public void checkMethodDeleteGrouptFromCourse() throws UniversityServiceException, UniversityDaoException {
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        Course course = createFakeCourseWithGroup();
        Group group = createFakeGroup();
        courseServiceImpl.deleteGroupFromCourse(group, course);
        Mockito.verify(courseMock, Mockito.times(1)).deleteGroupFromCourse(group, course);
        courseServiceImpl.getCourse(1);
        Mockito.verify(courseMock, Mockito.times(1)).get(captor.capture());
        assertEquals(1, captor.getValue());
    }
    */
    @Test
    public void checkMethodCountTimeTablesInCourse() throws UniversityServiceException, UniversityDaoException {
        initMocksForManyObjects();
        initMocksForOneObject();
        Course course = courseServiceImpl.getCourse(1);
        assertEquals(courseServiceImpl.countTimeTableInCourse(course), 1);
    }
    
    @Test
    public void checkMethodCountGroupsInCourse() throws UniversityServiceException, UniversityDaoException {
        initMocksForOneObject();
        Course course = createFakeCourse();
        assertEquals(courseServiceImpl.countGroupsInCourse(course), 1);
    }
  
    private void initMocksForOneObject() throws UniversityDaoException {
        Mockito.when(courseMock.getById(1)).thenReturn(createFakeCourse());
        Mockito.when(groupMock.getById(1)).thenReturn(createFakeGroup());
        Mockito.when(subjectMock.getById(1)).thenReturn(createFakeSubject());
        Mockito.when(timeTableMock.getById(1)).thenReturn(createFakeTimeTable());
    }
    
    private void initMocksForManyObjects() throws UniversityDaoException {
        Mockito.when(courseMock.findAll()).thenReturn(createFakeCoursesList());
        Mockito.when(groupMock.findAll()).thenReturn(createFakeGroupsList());
        Mockito.when(subjectMock.findAll()).thenReturn(createFakeSubjectsList());
        Mockito.when(timeTableMock.findAll()).thenReturn(createFakeTimeTablesList());
    }
    
    private List<Course> createFakeCoursesList() {
        List<Course> coursesList = new ArrayList<>();
        Course courseFirst = Course.builder().id(1).name("FIRST")
                .timeTableList(Arrays
                        .asList(TimeTable.builder().id(1).startTime(LocalTime.parse("07:00:00", TIME_FORMATT))
                                .endTime(LocalTime.parse("08:30:00", TIME_FORMATT))
                                .date(LocalDate.parse("2021-04-15", DATE_FORMATT)).build()))
                .build();
        Course courseSecond = Course.builder().id(2).name("SECOND")
                .timeTableList(Arrays
                        .asList(TimeTable.builder().id(1).startTime(LocalTime.parse("09:00:00", TIME_FORMATT))
                                .endTime(LocalTime.parse("10:30:00", TIME_FORMATT))
                                .date(LocalDate.parse("2021-04-15", DATE_FORMATT)).build()))
                .build();
        Course courseThird = Course.builder().id(3).name("THIRD")
                .timeTableList(Arrays
                        .asList(TimeTable.builder().id(1).startTime(LocalTime.parse("11:00:00", TIME_FORMATT))
                                .endTime(LocalTime.parse("12:30:00", TIME_FORMATT))
                                .date(LocalDate.parse("2021-04-15", DATE_FORMATT)).build()))
                .build();
        coursesList.add(courseFirst);
        coursesList.add(courseSecond);
        coursesList.add(courseThird);
        return coursesList;
    }
    
    private List<Group> createFakeGroupsList() {
        List<Group> groupsList = new ArrayList<>();
        Group groupFirst = Group.builder().id(1).name("QW-11").build();
        Group groupSecond = Group.builder().id(2).name("ER-12").build();
        Group groupThird = Group.builder().id(3).name("TY-21").build();
        groupsList.add(groupFirst);
        groupsList.add(groupSecond);
        groupsList.add(groupThird);
        return groupsList;
    }
    
    private List<Subject> createFakeSubjectsList() {
        List<Subject> subjectsList = new ArrayList<>();
        Subject subjectFirst = Subject.builder().id(1).name("Math").description(
                "Math includes the study of such topics as number theory, algebra, geometry and mathematical analysis")
                .build();
        Subject subjectSecond = Subject.builder().id(2).name("Physics").description(
                "Physics is the natural science that studies matter, its motion and behavior through space and time, and the related entities of energy and force")
                .build();
        Subject subjectThird = Subject.builder().id(3).name("Chemistry").description(
                "Chemistry is the scientific discipline involved with elements and compounds composed of atoms, molecules and ions: their composition, structure, properties, behavior and the changes they undergo during a reaction with other substances")
                .build();
        subjectsList.add(subjectFirst);
        subjectsList.add(subjectSecond);
        subjectsList.add(subjectThird);
        return subjectsList;
    }
    
    private List<TimeTable> createFakeTimeTablesList() {
        List<TimeTable> timeTablesList = new ArrayList<>();
        TimeTable timeTableFirst = TimeTable.builder().id(1)
                .startTime(LocalTime.parse("07:00:00", TIME_FORMATT))
                .endTime(LocalTime.parse("08:30:00", TIME_FORMATT))
                .date(LocalDate.parse("2021-04-15", DATE_FORMATT)).build();
        TimeTable timeTableSecond = TimeTable.builder().id(1)
                .startTime(LocalTime.parse("09:00:00", TIME_FORMATT))
                .endTime(LocalTime.parse("10:30:00", TIME_FORMATT))
                .date(LocalDate.parse("2021-04-15", DATE_FORMATT)).build();
        TimeTable timeTableThird = TimeTable.builder().id(1)
                .startTime(LocalTime.parse("11:00:00", TIME_FORMATT))
                .endTime(LocalTime.parse("12:30:00", TIME_FORMATT))
                .date(LocalDate.parse("2021-04-15", DATE_FORMATT)).build();
        timeTablesList.add(timeTableFirst);
        timeTablesList.add(timeTableSecond);
        timeTablesList.add(timeTableThird);
        return timeTablesList;
    }
    
    private Course createFakeCourse() {
        return Course.builder().id(1).name("FIRST")
                .groupList(Arrays.asList(Group.builder().id(1).name("QW-11").build()))
                .timeTableList(Arrays
                        .asList(TimeTable.builder().id(1).startTime(LocalTime.parse("07:00:00", TIME_FORMATT))
                                .endTime(LocalTime.parse("08:30:00", TIME_FORMATT))
                                .date(LocalDate.parse("2021-04-15", DATE_FORMATT)).build()))
                .build();
    }
    
    private Course createFakeCourseWithSubject() {
        return Course.builder().id(1).name("FIRST")
                .subject(Subject.builder().id(1).name("Math").description(
                "Math includes the study of such topics as number theory, algebra, geometry and mathematical analysis")
                .build())
                .timeTableList(Arrays
                        .asList(TimeTable.builder().id(1).startTime(LocalTime.parse("07:00:00", TIME_FORMATT))
                                .endTime(LocalTime.parse("08:30:00", TIME_FORMATT))
                                .date(LocalDate.parse("2021-04-15", DATE_FORMATT)).build()))
                .build();
    }
    
    private Group createFakeGroup() {
        return Group.builder().id(1).name("QW-11").build();
    }
    
    private TimeTable createFakeTimeTable() {
        return TimeTable.builder().id(1).startTime(LocalTime.parse("07:00:00", TIME_FORMATT))
                .endTime(LocalTime.parse("08:30:00", TIME_FORMATT))
                .date(LocalDate.parse("2021-04-15", DATE_FORMATT)).build();
    }
    
    private Subject createFakeSubject() {
        return Subject.builder().id(1).name("Math").description(
                "Math includes the study of such topics as number theory, algebra, geometry and mathematical analysis")
                .build();
    }
    
    
    private Course createFakeCourseWithGroup() {
        return Course.builder().id(1).name("FIRST")              
                .groupList(Arrays.asList(Group.builder().id(1).name("QW-11").build()))
                .timeTableList(Arrays
                        .asList(TimeTable.builder().id(1).startTime(LocalTime.parse("07:00:00", TIME_FORMATT))
                                .endTime(LocalTime.parse("08:30:00", TIME_FORMATT))
                                .date(LocalDate.parse("2021-04-15", DATE_FORMATT)).build()))
                .build();
    }
}
