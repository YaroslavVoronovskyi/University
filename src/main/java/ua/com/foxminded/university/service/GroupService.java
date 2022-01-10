package ua.com.foxminded.university.service;

import java.util.List;

import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Professor;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.service.exception.UniversityServiceException;

public interface GroupService {
    public Group getGroup(int id) throws UniversityServiceException;
    
    public List<Group> getAllGroups() throws UniversityServiceException;
    
    public List<Group> saveGroup(List<Group> groupsList) throws UniversityServiceException;
    
    public Group updateGroup(Group group) throws UniversityServiceException;
    
    public void deleteGroup(Group group) throws UniversityServiceException;
    
    public Student getStudent(int id) throws UniversityServiceException;
    
    public List<Student> getAllStudents() throws UniversityServiceException;
    
    public List<Student> saveStudent(List<Student> studentsList) throws UniversityServiceException;
    
    public Student updateStudent(Student student) throws UniversityServiceException;
    
    public void deleteStudent(Student student) throws UniversityServiceException;
    
    public Group updateStudentsInGroup(List<Student> studentsList, Group group) throws UniversityServiceException;
    
    public Group addStudentToGroup() throws UniversityServiceException;
    
    public Group deleteStudentFromGroup(Student student, Group group) throws UniversityServiceException;
    
    public Professor getProfessor(int id) throws UniversityServiceException;
    
    public List<Professor> getAllProfessors() throws UniversityServiceException;
    
    public List<Professor> saveProfessor(List<Professor> professorsList) throws UniversityServiceException;
    
    public Professor updateProfessor(Professor professor) throws UniversityServiceException;
    
    public void deleteProfessor(Professor professor) throws UniversityServiceException;
    
    public Professor updateSubjectInProfessor(List<Subject> studentsList, Professor professor) throws UniversityServiceException;
    
    public Subject getSubject(int id) throws UniversityServiceException;
    
    public List<Subject> getAllSubjects() throws UniversityServiceException;
    
    public List<Subject> saveSubject(List<Subject> subjectsList) throws UniversityServiceException;
    
    public Subject updateSubject(Subject subject) throws UniversityServiceException;
    
    public void deleteSubject(Subject subject) throws UniversityServiceException;
    
    public Professor addSubjectToProfessor() throws UniversityServiceException;
    
    public Professor deleteSubjectFromProfessor(Subject subject, Professor professor) throws UniversityServiceException;
    
    public int countStudents() throws UniversityServiceException;
    
    public int countGroups() throws UniversityServiceException;
    
    public int countProfessors() throws UniversityServiceException;
    
    public int countSubjects() throws UniversityServiceException;
    
    public int countStudentsInGroup(Group group) throws UniversityServiceException;
    
    public int countSubjectsInProfessors(Professor professor) throws UniversityServiceException;

}
