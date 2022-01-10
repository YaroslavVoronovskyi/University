package ua.com.foxminded.university.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.foxminded.university.controller.web.thymeleaf.dto.input.StudentDtoIn;
import ua.com.foxminded.university.controller.web.thymeleaf.dto.output.StudentDtoOut;
import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.exception.UniversityServiceException;

@Controller
@RequestMapping("/students")
public class StudentsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(StudentsController.class);
    
    private static final String KEY_STUDENT = "student";
    private static final String KEY_STUDENT_DTO_IN = "studentDtoIn";
    private static final String ID = "id";
    
    private GroupService groupService;
    private ModelMapper modelMapper;
    
    @Autowired
    public StudentsController(GroupService groupService, ModelMapper modelMapper) {
        this.groupService = groupService;
        this.modelMapper = modelMapper;
    }
    
    @GetMapping()
    public String getAll(Model model) throws UniversityServiceException {
        LOGGER.debug("Try show all students");
        try {
            model.addAttribute(KEY_STUDENT, convertToDtoList(groupService.getAllStudents()));
        } catch (RuntimeException exception) {
            LOGGER.error("Students not found!", exception);
            model.addAttribute("students_getall_error", "Students not found!");
            return "students/error";
        }
        LOGGER.debug("All students was successfully showed");
        return "students/index";
    }
    
    @GetMapping("show/{id}")
    public String get(@PathVariable(ID) int id, Model model) throws UniversityServiceException {
        LOGGER.debug("Try show student {}", id);
        try {
            model.addAttribute(KEY_STUDENT, convertToDto(groupService.getStudent(id)));
        } catch (RuntimeException exception) {
            LOGGER.error("Student {} not found!", id, exception);
            model.addAttribute("student_get_error", "Student not found!");
            return "students/error";
        }
        LOGGER.debug("Student was successfully showed {}", id);
        return "students/show";
    }
    
    @GetMapping("/new")
    public String createForm(String firstName, String lastName, StudentDtoIn studentDtoIn, Model model) {
        LOGGER.debug("Try open form for create new student");
        return "students/new";
    }
    
    @PostMapping()
    public String create(String firstName, String lastName, @Valid StudentDtoIn studentDtoIn, 
            BindingResult bindingResult, Model model) throws UniversityServiceException {
        LOGGER.debug("Try save new student from UI form");
        try {
            if (bindingResult.hasErrors())
                return "students/new";
            groupService.saveStudent(convertToEntityList(Arrays.asList(StudentDtoIn.builder()
                    .firstName(studentDtoIn.getFirstName()).lastName(studentDtoIn.getLastName()).build())));
        } catch (RuntimeException exception) {
            LOGGER.error("Student can not be craete!", exception);
            model.addAttribute("student_create_error", "Student can not be craete!");
            return "students/error";
        }
        LOGGER.debug("New student was successfully saved from UI form to DB");
        return "redirect:/students";
    }
    
    @GetMapping("/edit/{id}")
    public String updateForm(@PathVariable(ID) int id, Model model) throws UniversityServiceException {
        LOGGER.debug("Try get course for update {}", id);
        try {
            model.addAttribute(KEY_STUDENT_DTO_IN, convertToDto(groupService.getStudent(id)));
        } catch (RuntimeException exception) {
            LOGGER.error("Student {} not found!", id, exception);
            model.addAttribute("student_get_error", "Student not found!");
            return "students/error";
        }
        LOGGER.debug("Course was successfully got and ready for update {}", id);
        return "students/edit";
    }
    
    @PostMapping("/edit/{id}")
    public String update(@PathVariable(ID) int id, String firstName, String lastName, @Valid StudentDtoIn studentDtoIn, 
            BindingResult bindingResult, Model model) throws UniversityServiceException {
        LOGGER.debug("Try open form UI for update student");
        try {
            if (bindingResult.hasErrors())
                return "students/edit";
            groupService.updateStudent(convertToEntity(StudentDtoIn.builder().id(id)
                    .firstName(studentDtoIn.getFirstName()).lastName(studentDtoIn.getLastName()).build()));
        } catch (RuntimeException exception) {
            LOGGER.error("Student can not be update!", exception);
            model.addAttribute("student_update_error", "Student can not be update!");
            return "students/error";
        }
        LOGGER.debug("Student was successfully updated from UI form to DB");
        return "redirect:/students";
    }
    
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(ID) int id, StudentDtoIn studentDtoIn, Model model) throws UniversityServiceException {
        LOGGER.debug("Try delete student {}", id);
        try {
            groupService.deleteStudent(convertToEntity(StudentDtoIn.builder().id(studentDtoIn.getId()).build()));
        } catch (Throwable exception) {
            LOGGER.error("Student {} can not be delete!", id, exception);
            model.addAttribute("student_delete_error", "Student can not be delete!");
            return "students/error";
        }
        LOGGER.debug("Student was successfully deleted from UI form to DB {}", id);
        return "redirect:/students";
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
