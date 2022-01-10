package ua.com.foxminded.university.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import ua.com.foxminded.university.controller.dao.GroupDAO;
import ua.com.foxminded.university.controller.dao.ProfessorDAO;
import ua.com.foxminded.university.controller.dao.StudentDAO;
import ua.com.foxminded.university.controller.dao.SubjectDAO;
import ua.com.foxminded.university.controller.exception.UniversityDaoException;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Professor;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.model.TimeTable;
import ua.com.foxminded.university.service.exception.UniversityServiceException;

public class GroupServiceImplTest extends AbstractServiceImplTest {
    @Mock
    private GroupDAO groupMock;
    @Mock
    private StudentDAO studentMock;
    @Mock
    private ProfessorDAO professorMock;
    @Mock
    private SubjectDAO subjectMock;
    @InjectMocks
    private GroupServiceImpl groupServiceImpl;
    
    @Test
    public void checkMethodGet() throws UniversityDaoException, UniversityServiceException {
        initMocksForOneObject();
        assertEquals(groupServiceImpl.getGroup(1), createFakeGroup());
        assertEquals(groupServiceImpl.getStudent(1), createFakeStudent());
        assertEquals(groupServiceImpl.getProfessor(1), createFakeProfessor());
        assertEquals(groupServiceImpl.getSubject(1), createFakeSubject());
    }
    
    @Test
    public void checkMethodGetAll() throws UniversityDaoException, UniversityServiceException {
        initMocksForManyObjects();
        List<Group> expectedGroupsList = groupServiceImpl.getAllGroups();
        List<Group> actualGroupsList = createFakeGroupsList();
        List<Student> expectedStudentsList = groupServiceImpl.getAllStudents();
        List<Student> actualStudentsList = createFakeStudentsList();
        List<Professor> expectedProfessorsList = groupServiceImpl.getAllProfessors();
        List<Professor> actualProfessorsList = createFakeProfessorsList();
        List<Subject> expectedSubjectsList = groupServiceImpl.getAllSubjects();
        List<Subject> actualSubjectsList = createFakeSubjectsList();
        assertEquals(expectedGroupsList.get(0), actualGroupsList.get(0));
        assertEquals(expectedGroupsList.get(1), actualGroupsList.get(1));
        assertEquals(expectedGroupsList.get(2), actualGroupsList.get(2));
        assertEquals(expectedStudentsList.get(0), actualStudentsList.get(0));
        assertEquals(expectedStudentsList.get(1), actualStudentsList.get(1));
        assertEquals(expectedStudentsList.get(2), actualStudentsList.get(2));
        assertEquals(expectedProfessorsList.get(0), actualProfessorsList.get(0));
        assertEquals(expectedProfessorsList.get(1), actualProfessorsList.get(1));
        assertEquals(expectedProfessorsList.get(2), actualProfessorsList.get(2));
        assertEquals(expectedSubjectsList.get(0), actualSubjectsList.get(0));
        assertEquals(expectedSubjectsList.get(1), actualSubjectsList.get(1));
        assertEquals(expectedSubjectsList.get(2), actualSubjectsList.get(2));
    }
    
    @Test
    public void checkMethodDelete() throws UniversityDaoException, UniversityServiceException {
        initMocksForManyObjects();
        Group group = Group.builder().id(1).build();
        groupServiceImpl.deleteGroup(group);
        Student student = Student.builder().id(1).build();
        groupServiceImpl.deleteStudent(student);
        Professor professor = Professor.builder().id(1).build();
        groupServiceImpl.deleteProfessor(professor);
        Subject subject = Subject.builder().id(1).build();
        groupServiceImpl.deleteSubject(subject);
        assertFalse(groupServiceImpl.getAllGroups().contains(group));
        assertFalse(groupServiceImpl.getAllStudents().contains(student));
        assertFalse(groupServiceImpl.getAllProfessors().contains(professor));
        assertFalse(groupServiceImpl.getAllSubjects().contains(subject));
    }
    
    @Test
    public void checkMethodUpdate() throws UniversityDaoException, UniversityServiceException {
        initMocksForOneObject();
        Group group = Group.builder().id(1).name("FF-22").build();
        groupServiceImpl.updateGroup(group);
        Student student = Student.builder().id(1).firstName("Yaroslav").lastName("Voronovskyi").build();
        groupServiceImpl.updateStudent(student);
        Professor professor = Professor.builder().id(1).firstName("Yaroslav").lastName("Voronovskyi").build();
        groupServiceImpl.updateProfessor(professor);
        Subject subject = Subject.builder().id(1).name("Java").description("Programming language").build();
        groupServiceImpl.updateSubject(subject);
        assertEquals(groupMock.findAll(), groupServiceImpl.getAllGroups());
        assertEquals(studentMock.findAll(), groupServiceImpl.getAllStudents());
        assertEquals(professorMock.findAll(), groupServiceImpl.getAllProfessors());
        assertEquals(subjectMock.findAll(), groupServiceImpl.getAllSubjects());
    }
    
    @Test
    public void checkMethodSave() throws UniversityDaoException, UniversityServiceException {
        initMocksForManyObjects();
        Group group = Group.builder().id(4).name("FF-22").build();
        List<Group> groupsList = groupServiceImpl.getAllGroups();
        groupsList.add(group);
        groupServiceImpl.saveGroup(groupsList);
        Student student = Student.builder().id(4).firstName("Yaroslav").lastName("Voronovskyi").build();
        List<Student> studentsList = groupServiceImpl.getAllStudents();
        studentsList.add(student);
        groupServiceImpl.saveStudent(studentsList);
        Professor professor = Professor.builder().id(4).firstName("Yaroslav").lastName("Voronovskyi").build();
        List<Professor> professorsList = groupServiceImpl.getAllProfessors();
        professorsList.add(professor);
        groupServiceImpl.saveProfessor(professorsList);
        Subject subject = Subject.builder().id(4).name("Java").description("Programming language").build();
        List<Subject> subjectsList = groupServiceImpl.getAllSubjects();
        subjectsList.add(subject);
        groupServiceImpl.saveSubject(subjectsList);
        assertTrue(groupServiceImpl.getAllGroups().contains(group));
        assertTrue(groupServiceImpl.getAllStudents().contains(student));
        assertTrue(groupServiceImpl.getAllProfessors().contains(professor));
        assertTrue(groupServiceImpl.getAllSubjects().contains(subject));
    }
    
    @Test
    public void checkMethodUpdateSubjectsInProfessor() throws UniversityDaoException, UniversityServiceException {
        initMocksForOneObject();
        initMocksForManyObjects();
        Professor expecteProfessor = professorMock.getById(1);
        Professor actualProfessor = createFakeProfessor();
        List<Subject> expectedSubjectsList = subjectMock.findAll();
        List<Subject> actualSubjectsList = createFakeSubjectsList();
        groupServiceImpl.updateSubjectInProfessor(expectedSubjectsList, expecteProfessor);
        assertEquals(expecteProfessor.getId(), actualProfessor.getId());
        assertEquals(expecteProfessor.getFirstName(), actualProfessor.getFirstName());
        assertEquals(expecteProfessor.getLastName(), actualProfessor.getLastName());
        assertEquals(expectedSubjectsList.get(1).getId(), actualSubjectsList.get(1).getId());
        assertEquals(expectedSubjectsList.get(1).getName(), actualSubjectsList.get(1).getName());
        assertEquals(expectedSubjectsList.get(1).getDescription(),
                actualSubjectsList.get(1).getDescription());
    }
    
    @Test
    public void checkMethodAddSubjectsToProfessor() throws UniversityDaoException, UniversityServiceException {
        initMocksForManyObjects();
        initMocksForOneObject();
        Professor professor = groupServiceImpl.addSubjectToProfessor();
        assertEquals(professor.getSubjectList().stream().map(Subject::getId).collect(Collectors.toList())
                .get(TimeTable.builder().build().getId()), 3);
    }
    
    @Test
    public void checkMethodUpdateStudentsInGroup() throws UniversityDaoException, UniversityServiceException {
        initMocksForOneObject();
        initMocksForManyObjects();
        Group expectedGroup = groupMock.getById(1);
        Group actualGroup = createFakeGroup();
        List<Student> expectedStudentsList = studentMock.findAll();
        List<Student> actualStudentsList = createFakeStudentsList();
        groupServiceImpl.updateStudentsInGroup(expectedStudentsList, expectedGroup);
        assertEquals(expectedGroup.getId(), actualGroup.getId());
        assertEquals(expectedGroup.getName(), actualGroup.getName());
        assertEquals(expectedStudentsList.get(1).getId(), actualStudentsList.get(1).getId());
        assertEquals(expectedStudentsList.get(1).getFirstName(), actualStudentsList.get(1).getFirstName());
        assertEquals(expectedStudentsList.get(1).getLastName(), actualStudentsList.get(1).getLastName());
    }
    
    @Test
    public void checkMethodAddStudentsToGroup() throws UniversityDaoException, UniversityServiceException {
        initMocksForManyObjects();
        initMocksForOneObject();
        Group group = groupServiceImpl.addStudentToGroup();
        assertEquals(group.getStudentList().stream().map(Student::getId).collect(Collectors.toList())
                .get(TimeTable.builder().build().getId()), 3);
    }
    
    @Test
    public void chekMethodCount() throws UniversityDaoException, UniversityServiceException {
        initMocksForManyObjects();
        assertEquals(groupServiceImpl.countGroups(), 3);
        assertEquals(groupServiceImpl.countStudents(), 3);
        assertEquals(groupServiceImpl.countProfessors(), 3);
        assertEquals(groupServiceImpl.countSubjects(), 3);
    }
    /**
    @Test
    public void checkMethodDeleteStudentFromGroup() throws UniversityDaoException, UniversityServiceException {
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        Student student = createFakeStudent();
        Group group = createFakeGroupWithStudent();
        groupServiceImpl.deleteStudentFromGroup(student, group);
        Mockito.verify(groupMock, Mockito.times(1)).deleteStudentFromGroup(student, group);
        groupServiceImpl.getGroup(1);
        Mockito.verify(groupMock, Mockito.times(1)).get(captor.capture());
        assertEquals(1, captor.getValue());
    }
    
    @Test
    public void checkMethodDeleteSubjectFromProfessor() throws UniversityDaoException, UniversityServiceException {
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        Subject subject = createFakeSubject();
        Professor professor = createFakeProfessorWithSubject();
        groupServiceImpl.deleteSubjectFromProfessor(subject, professor);
        Mockito.verify(professorMock, Mockito.times(1)).deleteSubjectFromProfessor(subject, professor);
        groupServiceImpl.getProfessor(1);
        Mockito.verify(professorMock, Mockito.times(1)).get(captor.capture());
        assertEquals(1, captor.getValue());
    }
    */
    @Test
    public void checkMethodCountStudentsInGroup() throws UniversityDaoException, UniversityServiceException {
        initMocksForOneObject();
        Group group = createFakeGroup();
        assertEquals(groupServiceImpl.countStudentsInGroup(group), 1);
    }
    
    @Test
    public void checkMethodCountSubjectsInProfessors() throws UniversityDaoException, UniversityServiceException {
        initMocksForOneObject();
        Professor professor = createFakeProfessor();
        assertEquals(groupServiceImpl.countSubjectsInProfessors(professor), 1);
    }
    
    private void initMocksForOneObject() throws UniversityDaoException {
        Mockito.when(groupMock.getById(1)).thenReturn(createFakeGroup());
        Mockito.when(studentMock.getById(1)).thenReturn(createFakeStudent());
        Mockito.when(professorMock.getById(1)).thenReturn(createFakeProfessor());
        Mockito.when(subjectMock.getById(1)).thenReturn(createFakeSubject());
    }
    
    private void initMocksForManyObjects() throws UniversityDaoException {
        Mockito.when(groupMock.findAll()).thenReturn(createFakeGroupsList());
        Mockito.when(studentMock.findAll()).thenReturn(createFakeStudentsList());
        Mockito.when(professorMock.findAll()).thenReturn(createFakeProfessorsList());
        Mockito.when(subjectMock.findAll()).thenReturn(createFakeSubjectsList());
    }
    
    private Group createFakeGroup() {
        return Group.builder().id(1).name("QW-11")
                .studentList(Arrays.asList(Student.builder().id(1).firstName("Viktor").lastName("Tsygankov").build()))
                .build();
    }
    
    private Student createFakeStudent() {
        return Student.builder().id(1).firstName("Viktor").lastName("Tsygankov").build();
    }
    
    private Group createFakeGroupWithStudent() {
        return Group.builder().id(1).name("QW-11")
                .studentList(Arrays
                        .asList(Student.builder().id(1).firstName("Viktor").lastName("Tsygankov").build()))
                .build();
    }
    
    private Professor createFakeProfessor() {
        return Professor.builder().id(1).firstName("Viktor").lastName("Rebrov")
                .subjectList(Arrays.asList(Subject.builder().id(1).name("Math").description(
                "Math includes the study of such topics as number theory, algebra, geometry and mathematical analysis")
                .build()))
                .build();
    }
    
    private Professor createFakeProfessorWithSubject() {
        return Professor.builder().id(1).firstName("Viktor").lastName("Rebrov")
                .subjectList(Arrays.asList(Subject.builder().id(1).name("Math").description(
                        "Math includes the study of such topics as number theory, algebra, geometry and mathematical analysis")
                        .build()))
                .build();
    }
    
    private Subject createFakeSubject() {
        return Subject.builder().id(1).name("Math").description(
                "Math includes the study of such topics as number theory, algebra, geometry and mathematical analysis")
                .build();
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
    
    private List<Student> createFakeStudentsList() {
        List<Student> studentsList = new ArrayList<>();
        Student studentFirst = Student.builder().id(1).firstName("Viktor").lastName("Tsygankov").build();
        Student studentSecond = Student.builder().id(2).firstName("Denys").lastName("Tsygankov").build();
        Student studentThird = Student.builder().id(3).firstName("Olena").lastName("Tsygankov").build();
        studentsList.add(studentFirst);
        studentsList.add(studentSecond);
        studentsList.add(studentThird);
        return studentsList;
    }
    
    private List<Professor> createFakeProfessorsList() {
        List<Professor> professorsList = new ArrayList<>();
        Professor professorFirst = Professor.builder().id(1).firstName("Viktor").lastName("Rebrov").build();
        Professor professorSecond = Professor.builder().id(1).firstName("Denys").lastName("Rebrov").build();
        Professor professorThird = Professor.builder().id(1).firstName("Olena").lastName("Rebrov").build();
        professorsList.add(professorFirst);
        professorsList.add(professorSecond);
        professorsList.add(professorThird);
        return professorsList;
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
}
