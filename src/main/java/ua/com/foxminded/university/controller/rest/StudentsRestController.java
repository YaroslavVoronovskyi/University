package ua.com.foxminded.university.controller.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ua.com.foxminded.university.controller.rest.exception.UniversityRestControllerException;
import ua.com.foxminded.university.controller.web.rest.dto.input.StudentDtoIn;
import ua.com.foxminded.university.controller.web.rest.dto.output.StudentDtoOut;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.exception.UniversityServiceException;

@RestController
@RequestMapping("/rest/student")
public class StudentsRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(StudentsRestController.class);
    private static final String ID = "id";
    
    private GroupService groupService;
    private ModelMapper modelMapper;
    
    @Autowired
    public StudentsRestController(GroupService groupService, ModelMapper modelMapper) {
        this.groupService = groupService;
        this.modelMapper = modelMapper;
    }
    
    @GetMapping()
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public List<StudentDtoOut> getAllStudents() throws UniversityServiceException, UniversityRestControllerException {
        LOGGER.debug("Try show all students");
        List<StudentDtoOut> studentsDtoOutList = new ArrayList<>();
        try {
            studentsDtoOutList = convertToDtoList(groupService.getAllStudents());
        } catch (MappingException exception) {
            throw new UniversityRestControllerException("Students not found!", exception);
        }
        LOGGER.debug("All students was successfully showed");
        return studentsDtoOutList;
    }
    
    @GetMapping("/{id}")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public StudentDtoOut getStudent(@PathVariable(ID) int id) throws UniversityServiceException, UniversityRestControllerException {
        LOGGER.debug("Try show student {}", id);
        StudentDtoOut studentDtoOut = null;
        try {
            studentDtoOut = convertToDto(groupService.getStudent(id));
        } catch (MappingException exception) {
            throw new UniversityRestControllerException("Student with ID " + id + " not found!", exception);
        }
        LOGGER.debug("Student was successfully showed {}", id);
        return studentDtoOut;
    }
    
    @PostMapping()
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public List<StudentDtoOut> createStudent(@RequestBody @Valid StudentDtoIn studentDtoIn) throws UniversityServiceException, UniversityRestControllerException {
        LOGGER.debug("Try create new student");
        List<Student> studentsList = groupService.saveStudent(convertToEntityList(
                Arrays.asList(StudentDtoIn.builder().firstName(studentDtoIn.getFirstName())
                        .lastName(studentDtoIn.getLastName()).build())));
        LOGGER.debug("New student was successfully saved from UI form to DB");
        return convertToDtoList(studentsList);
    }
    
    @PutMapping("/{id}")
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public StudentDtoOut updateStudent(@PathVariable(ID) int id, String firstName, String lastName, 
            @RequestBody @Valid StudentDtoIn studentDtoIn) throws UniversityServiceException, UniversityRestControllerException {
        LOGGER.debug("Try open form UI for update student");
        Student student = null;
        try {
            student = groupService.getStudent(id);
            student = groupService.updateStudent(convertToEntity(StudentDtoIn.builder().id(id).firstName(studentDtoIn.getFirstName())
                    .lastName(studentDtoIn.getLastName()).build()));
        } catch (EntityNotFoundException exception) {
            throw new UniversityRestControllerException("Student with " + id + " can't be update ", exception);
        }
        LOGGER.debug("Student was successfully updated from UI form to DB");
        return convertToDto(student);
    }
    
    @DeleteMapping("/{id}")
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable(ID) int id, StudentDtoIn studentDtoIn) throws UniversityServiceException, UniversityRestControllerException {
        LOGGER.debug("Try delete student {}", id);
        try {
            groupService
                    .deleteStudent(convertToEntity(StudentDtoIn.builder().id(studentDtoIn.getId()).build()));
        } catch (EmptyResultDataAccessException exception) {
            throw new UniversityRestControllerException("Student with " + id + " can't be delete ", exception);
        }
        LOGGER.debug("Student was successfully deleted from UI form to DB {}", id);
    }
    
    private StudentDtoOut convertToDto(Student student) {
        return modelMapper.map(student, StudentDtoOut.class);
    }
    
    private Student convertToEntity(StudentDtoIn studentDtoIn) {
        return modelMapper.map(studentDtoIn, Student.class);
    }
    
    private List<StudentDtoOut> convertToDtoList(List<Student> studentsList) {
        return studentsList.stream().map(this::convertToDto).collect(Collectors.toList());
    }
    
    private List<Student> convertToEntityList(List<StudentDtoIn> studentsDtoInList) {
        return studentsDtoInList.stream().map(this::convertToEntity).collect(Collectors.toList());
    }
}
