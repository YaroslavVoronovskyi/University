package ua.com.foxminded.university.service;

import java.util.List;

import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.university.controller.dao.GroupDAO;
import ua.com.foxminded.university.controller.dao.ProfessorDAO;
import ua.com.foxminded.university.controller.dao.StudentDAO;
import ua.com.foxminded.university.controller.dao.SubjectDAO;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.model.Professor;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.service.exception.UniversityServiceException;

@Service
public class GroupServiceImpl implements GroupService {   
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupServiceImpl.class);
    
    private GroupDAO groupDAO;
    private StudentDAO studentDAO;
    private ProfessorDAO professorDAO;
    private SubjectDAO subjectDAO;
    
    @Autowired
    public GroupServiceImpl(GroupDAO groupDAO, StudentDAO studentDAO, ProfessorDAO professorDAO,
            SubjectDAO subjectDAO) {
        this.groupDAO = groupDAO;
        this.studentDAO = studentDAO;
        this.professorDAO = professorDAO;
        this.subjectDAO = subjectDAO;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Group getGroup(int id) throws UniversityServiceException {
        LOGGER.debug("Try get group {}", id);
        try {
            LOGGER.debug("Group was successfully got {}", id);
            return groupDAO.getById(id);
        } catch (PersistenceException exception) {
            throw new UniversityServiceException("Group " + id + " couldn't be got", exception);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Group> getAllGroups() throws UniversityServiceException {
        LOGGER.debug("Try get all groups");
        try {
            LOGGER.debug("All groups was successfully got");
            return groupDAO.findAll();
        } catch (PersistenceException exception) {
            throw new UniversityServiceException("Groups list  couldn't be got", exception);
        }
    }
    
    @Override
    @Transactional
    public List<Group> saveGroup(List<Group> groupsList) throws UniversityServiceException {
        LOGGER.debug("Try save groups list");
        try {
            LOGGER.debug("All groups was successfully saved");
            return groupDAO.saveAll(groupsList);           
        } catch (PersistenceException exception) {
            throw new UniversityServiceException("Groups list " + groupsList + " couldn't be saved",
                    exception);
        }
    }
    
    @Override
    @Transactional
    public Group updateGroup(Group group) throws UniversityServiceException {
        LOGGER.debug("Try update group {}", group);
        try {
            LOGGER.debug("Group was successfully updated {}", group);
            return groupDAO.save(group);            
        } catch (PersistenceException exception) {
            throw new UniversityServiceException("Course " + group + " couldn't be updated", exception);
        }
    }
    
    @Override
    @Transactional
    public void deleteGroup(Group group) throws UniversityServiceException {
        LOGGER.debug("Try delete group {}", group);
        try {
            groupDAO.deleteById(group.getId());
            LOGGER.debug("Group was successfully deleted {}", group);
        } catch (PersistenceException exception) {
            throw new UniversityServiceException("Group " + group + " couldn't be deleted", exception);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public Student getStudent(int id) throws UniversityServiceException {
        LOGGER.debug("Try get student {}", id);
        try {
            LOGGER.debug("Student was successfully got {}", id);
            return studentDAO.getById(id);
        } catch (PersistenceException exception) {
            throw new UniversityServiceException("Student " + id + " couldn't be got", exception);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Student> getAllStudents() throws UniversityServiceException {
        LOGGER.debug("Try get all students");
        try {
            LOGGER.debug("All students was successfully got");
            return studentDAO.findAll();
        } catch (PersistenceException exception) {
            throw new UniversityServiceException("Students list  couldn't be got", exception);
        }
    }
    
    @Override
    @Transactional
    public List<Student> saveStudent(List<Student> studentsList) throws UniversityServiceException {
        LOGGER.debug("Try save students list");
        try {
            LOGGER.debug("All students was successfully saved");
            return studentDAO.saveAll(studentsList);           
        } catch (PersistenceException exception) {
            throw new UniversityServiceException("Students list " + studentsList + " couldn't be saved",
                    exception);
        }
    }
    
    @Override
    @Transactional
    public Student updateStudent(Student student) throws UniversityServiceException {
        LOGGER.debug("Try update student {}", student);
        try {
            LOGGER.debug("Student was successfully updated {}", student);
            return studentDAO.save(student);           
        } catch (PersistenceException exception) {
            throw new UniversityServiceException("Student " + student + " couldn't be updated", exception);
        }
    }
    
    @Override
    @Transactional
    public void deleteStudent(Student student) throws UniversityServiceException {
        LOGGER.debug("Try delete student {}", student);
        try {
            studentDAO.deleteById(student.getId());
            LOGGER.debug("Student was successfully deleted {}", student);
        } catch (PersistenceException exception) {
            throw new UniversityServiceException("Student " + student + " couldn't be deleted", exception);
        }
    }
/**
    @Override
    public Group updateStudentsInGroup(List<Student> studentsList, Group group) throws UniversityServiceException {
        LOGGER.debug("Try update students list in group {}, {}", studentsList, group);
        try {
            groupDAO.updateStudentInGroup(studentsList, group);
            LOGGER.info("Students list was successfully added to group {}, {}", studentsList, group);
            return group;
        } catch (UniversityDaoException exception) {
            throw new UniversityServiceException("Students list couldn't be updated in Group", exception);
        }
    }
    
    @Override
    public Group addStudentToGroup() throws UniversityServiceException {
        LOGGER.debug("Try add students list to group");
        try {
            Group group = null;
            List<Student> studentsList = studentDAO.getAll();
            List<Group> groupsList = groupDAO.getAll();
            for (Student student : studentsList) {
                for (int index = 0; index < studentsList.size(); index++) {
                    group = Group.builder().id(groupsList.get(index).getId())
                            .name(groupsList.get(index).getName())
                            .studentList(Arrays.asList(
                                    Student.builder().id(student.getId()).firstName(student.getFirstName())
                                            .lastName(student.getLastName()).build()))
                            .build();
                    groupDAO.updateStudentInGroup(studentsList, group);
                }
            }
            LOGGER.info("Students list was successfully added to group");
            return group;
        } catch (UniversityDaoException exception) {
            throw new UniversityServiceException("Students list couldn't be added to Group", exception);
        }
    }
    
    @Override
    public Group deleteStudentFromGroup(Student student, Group group) throws UniversityServiceException {
        LOGGER.debug("Try delete student from group {}, {}", student, group);
        try {
            LOGGER.info("Student was successfully deleted from group {}, {}", student, group);
            return groupDAO.deleteStudentFromGroup(student, group);
        } catch (UniversityDaoException exception) {
            throw new UniversityServiceException("Student " + student + " couldn't be deleted from Course " + group, exception);
        }
    }
    */
    @Override
    @Transactional(readOnly = true)
    public Professor getProfessor(int id) throws UniversityServiceException {
        LOGGER.debug("Try get professor {}", id);
        try {
            LOGGER.debug("Professor was successfully got {}", id);
            return professorDAO.getById(id);
        } catch (PersistenceException exception) {
            throw new UniversityServiceException("Professor " + id + " couldn't be got", exception);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Professor> getAllProfessors() throws UniversityServiceException {
        LOGGER.debug("Try get all professors");
        try {
            LOGGER.debug("All professors was successfully got");
            return professorDAO.findAll();
        } catch (PersistenceException exception) {
            throw new UniversityServiceException("Professors list  couldn't be got", exception);
        }
    }
    
    @Override
    @Transactional
    public List<Professor> saveProfessor(List<Professor> professorsList) throws UniversityServiceException {
        LOGGER.debug("Try save professors list");
        try {
            LOGGER.debug("All professors was successfully saved");
            return professorDAO.saveAll(professorsList);            
        } catch (PersistenceException exception) {
            throw new UniversityServiceException("Professors list " + professorsList + " couldn't be saved",
                    exception);
        }
    }
    
    @Override
    @Transactional
    public Professor updateProfessor(Professor professor) throws UniversityServiceException {
        LOGGER.debug("Try update professor {}", professor);
        try {
            LOGGER.debug("Professor was successfully updated {}", professor);
            return professorDAO.save(professor);           
        } catch (PersistenceException exception) {
            throw new UniversityServiceException("Professor " + professor + " couldn't be updated", exception);
        }
    }
    
    @Override
    @Transactional
    public void deleteProfessor(Professor professor) throws UniversityServiceException {
        LOGGER.debug("Try delete professor {}", professor);
        try {
            professorDAO.deleteById(professor.getId());
            LOGGER.debug("Professor was successfully deleted {}", professor);
        } catch (PersistenceException exception) {
            throw new UniversityServiceException("Professor " + professor + " couldn't be deleted", exception);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public Subject getSubject(int id) throws UniversityServiceException {
        LOGGER.debug("Try get subject {}", id);
        try {
            LOGGER.debug("Subject was successfully got {}", id);
            return subjectDAO.getById(id);
        } catch (PersistenceException exception) {
            throw new UniversityServiceException("Subject " + id + " couldn't be got", exception);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Subject> getAllSubjects() throws UniversityServiceException {
        LOGGER.debug("Try get all subjects");
        try {
            LOGGER.debug("All subjects was successfully got");
            return subjectDAO.findAll();
        } catch (PersistenceException exception) {
            throw new UniversityServiceException("Subject list  couldn't be got", exception);
        }
    }
    
    @Override
    @Transactional
    public List<Subject> saveSubject(List<Subject> subjectsList) throws UniversityServiceException {
        LOGGER.debug("Try save subjects list");
        try {
            LOGGER.debug("All subjects was successfully saved");
            return subjectDAO.saveAll(subjectsList);            
        } catch (PersistenceException exception) {
            throw new UniversityServiceException("Subjects list " + subjectsList + " couldn't be saved",
                    exception);
        }
    }
    
    @Override
    @Transactional
    public Subject updateSubject(Subject subject) throws UniversityServiceException {
        LOGGER.debug("Try update subject {}", subject);
        try {
            LOGGER.debug("Subject was successfully updated {}", subject);
            return subjectDAO.save(subject);        
        } catch (PersistenceException exception) {
            throw new UniversityServiceException("Subject " + subject + " couldn't be updated", exception);
        }
    }
    
    @Override
    @Transactional
    public void deleteSubject(Subject subject) throws UniversityServiceException {
        LOGGER.debug("Try delete subject {}", subject);
        try {
            subjectDAO.deleteById(subject.getId());
            LOGGER.debug("Subject was successfully deleted {}", subject);
        } catch (PersistenceException exception) {
            throw new UniversityServiceException("Subject " + subject + " couldn't be deleted", exception);
        }
    }

    @Override
    public Group updateStudentsInGroup(List<Student> studentsList, Group group)
            throws UniversityServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Group addStudentToGroup() throws UniversityServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Group deleteStudentFromGroup(Student student, Group group) throws UniversityServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Professor updateSubjectInProfessor(List<Subject> studentsList, Professor professor)
            throws UniversityServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Professor addSubjectToProfessor() throws UniversityServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Professor deleteSubjectFromProfessor(Subject subject, Professor professor)
            throws UniversityServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int countStudents() throws UniversityServiceException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int countGroups() throws UniversityServiceException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int countProfessors() throws UniversityServiceException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int countSubjects() throws UniversityServiceException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int countStudentsInGroup(Group group) throws UniversityServiceException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int countSubjectsInProfessors(Professor professor) throws UniversityServiceException {
        // TODO Auto-generated method stub
        return 0;
    }
    
/**
    @Override
    public Professor updateSubjectInProfessor(List<Subject> subjectsList, Professor professor) throws UniversityServiceException {
        LOGGER.debug("Try update subjects list in professor {}, {}", subjectsList, professor);
        try {
            professorDAO.updateSubjectInProfessor(subjectsList, professor);
            LOGGER.info("Subjects list was successfully added to professor {}, {}", subjectsList, professor);
            return professor;
        } catch (UniversityDaoException exception) {
            throw new UniversityServiceException("Subjects list couldn't be updated in Professor", exception);
        }
    }
    
    @Override
    public Professor addSubjectToProfessor() throws UniversityServiceException {
        LOGGER.debug("Try add subjects list to professor");
        try {
            Professor professor = null;
            List<Subject> subjectsList = subjectDAO.getAll();
            List<Professor> professorsList = professorDAO.getAll();
            for (Subject subject : subjectsList) {
                for (int index = 0; index < subjectsList.size(); index++) {
                    professor = Professor.builder().id(professorsList.get(index).getId())
                            .firstName(professorsList.get(index).getFirstName())
                            .lastName(professorsList.get(index).getLastName())
                            .subjectList(Arrays.asList(Subject.builder().id(subject.getId()).build()))
                            .build();
                    professorDAO.updateSubjectInProfessor(subjectsList, professor);
                }
            }
            LOGGER.info("Subjects list was successfully added to professor {}, {}", subjectsList, professor);
            return professor;
        } catch (UniversityDaoException exception) {
            throw new UniversityServiceException("Subjects list couldn't be added to Professor", exception);
        }
    }
    
    @Override
    public Professor deleteSubjectFromProfessor(Subject subject, Professor professor) throws UniversityServiceException {
        LOGGER.debug("Try delete subject from professor {}, {}", subject, professor);
        try {
            LOGGER.info("Subject was successfully deleted from professor {}, {}", subject, professor);
            return professorDAO.deleteSubjectFromProfessor(subject, professor);
        } catch (UniversityDaoException exception) {
            throw new UniversityServiceException(
                    "Subject " + subject + " couldn't be deleted from Professor " + professor, exception);
        }
    }
    
    @Override
    public int countStudents() throws UniversityServiceException {
        LOGGER.debug("Try count students");
        try {
            List<Student> studentsList = studentDAO.getAll();
            LOGGER.info("Students was successfully counted");
            return studentsList.size();
        } catch (UniversityDaoException exception) {
            throw new UniversityServiceException("Students list couldn't be counted", exception);
        }
    }
    
    @Override
    public int countGroups() throws UniversityServiceException {
        LOGGER.debug("Try count groups");
        try {
            List<Group> groupsList = groupDAO.getAll();
            LOGGER.info("Groups was successfully counted");
            return groupsList.size();
        } catch (UniversityDaoException exception) {
            throw new UniversityServiceException("Groups list couldn't be counted", exception);
        }
    }

    @Override
    public int countProfessors() throws UniversityServiceException {
        LOGGER.debug("Try count professors");
        try {
            List<Professor> professorsList = professorDAO.getAll();
            LOGGER.info("Professors was successfully counted");
            return professorsList.size();
        } catch (UniversityDaoException exception) {
            throw new UniversityServiceException("Professors list couldn't be counted", exception);
        }
    }

    @Override
    public int countSubjects() throws UniversityServiceException {
        LOGGER.debug("Try count subjects");
        try {
            List<Subject> subjectsList = subjectDAO.getAll();
            LOGGER.info("Subjects was successfully counted");
            return subjectsList.size();
        } catch (UniversityDaoException exception) {
            throw new UniversityServiceException("Subjects list couldn't be counted", exception);
        }
    }

    @Override
    public int countStudentsInGroup(Group group) throws UniversityServiceException {
        LOGGER.debug("Try count students in group");
        try {
            group = groupDAO.get(group.getId());
            LOGGER.info("Students was successfully counted in group");
            return group.getStudentList().size();
        } catch (UniversityDaoException exception) {
            throw new UniversityServiceException("Students list couldn't be counted in Course", exception);
        }
    }
    
    @Override
    public int countSubjectsInProfessors(Professor professor) throws UniversityServiceException {
        LOGGER.debug("Try count subjects in professor");
        try {
            professor = professorDAO.get(professor.getId());
            LOGGER.info("Subjects was successfully counted in professor");
            return professor.getSubjectList().size();
        } catch (UniversityDaoException exception) {
            throw new UniversityServiceException("Subjects list couldn't be counted in Professor", exception);
        }
    }
    */
}
