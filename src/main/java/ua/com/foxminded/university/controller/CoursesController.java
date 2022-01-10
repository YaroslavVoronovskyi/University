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

import ua.com.foxminded.university.controller.web.thymeleaf.dto.input.CourseDtoIn;
import ua.com.foxminded.university.controller.web.thymeleaf.dto.output.CourseDtoOut;
import ua.com.foxminded.university.model.Course;
import ua.com.foxminded.university.service.CourseService;
import ua.com.foxminded.university.service.exception.UniversityServiceException;

@Controller
@RequestMapping("/courses")
public class CoursesController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CoursesController.class);
    
    private static final String KEY_COURSE = "course";
    private static final String KEY_COURSE_DTO_IN = "courseDtoIn";
    private static final String ID = "id";
    
    private CourseService courseService;
    private ModelMapper modelMapper;
    
    @Autowired
    public CoursesController(CourseService courseService, ModelMapper modelMapper) {
        this.courseService = courseService;
        this.modelMapper = modelMapper;
    }
    
    @GetMapping()
    public String getAll(Model model) throws UniversityServiceException {
        LOGGER.debug("Try show all courses");
        try {
            model.addAttribute(KEY_COURSE, convertToDtoList(courseService.getAllCourses()));
        } catch (RuntimeException exception) {
            LOGGER.error("Courses not found!", exception);
            model.addAttribute("courses_getall_error", "Courses not found!");
            return "courses/error";
        }
        LOGGER.debug("All courses was successfully showed");
        return "courses/index";
    }
    
    @GetMapping("show/{id}")
    public String get(@PathVariable(ID) int id, Model model) throws UniversityServiceException {
        LOGGER.debug("Try show course {}", id);
        try {
            model.addAttribute(KEY_COURSE, convertToDto(courseService.getCourse(id)));
        } catch (RuntimeException exception) {
            LOGGER.error("Course {} not found!", id, exception);
            model.addAttribute("course_get_error", "Course not found!");
            return "courses/error";
        }
        LOGGER.debug("Course was successfully showed {}", id);
        return "courses/show";
    }
    
    @GetMapping("/new")
    public String createForm(String name, CourseDtoIn courseDtoIn) {
        LOGGER.debug("Try open form for create new course");
        return "courses/new";
    }
    
    @PostMapping()
    public String create(String name, @Valid CourseDtoIn courseDtoIn, BindingResult bindingResult, Model model) throws UniversityServiceException {
        LOGGER.debug("Try save new course from UI form");
        try {
            if (bindingResult.hasErrors())
                return "courses/new";
            courseService.saveCourse(convertToEntityList(Arrays.asList(CourseDtoIn.builder().name(courseDtoIn.getName()).build())));
        } catch (RuntimeException exception) {
            LOGGER.error("Course can not be craete!", exception);
            model.addAttribute("course_create_error", "Course can not be craete!");
            return "courses/error";
        }
        LOGGER.debug("New course was successfully saved from UI form to DB");
        return "redirect:/courses";
    }
    
    @GetMapping("/edit/{id}")
    public String updateForm(@PathVariable(ID) int id, Model model) throws UniversityServiceException {
        LOGGER.debug("Try get course for update {}", id);
        try {
            model.addAttribute(KEY_COURSE_DTO_IN, convertToDto(courseService.getCourse(id)));
        } catch (RuntimeException exception) {
            LOGGER.error("Course {} not found!", id, exception);
            model.addAttribute("course_get_error", "Course not found!");
            return "courses/error";
        }
        LOGGER.debug("Course was successfully got and ready for update {}", id);
        return "courses/edit";
    }
    
    @PostMapping("/edit/{id}")
    public String update(@PathVariable(ID) int id, String name, @Valid CourseDtoIn courseDtoIn,
            BindingResult bindingResult, Model model) throws UniversityServiceException {
        LOGGER.debug("Try open form UI for update course");
        try {
            if (bindingResult.hasErrors())
                return "courses/edit";
            courseService.updateCourse(convertToEntity(CourseDtoIn.builder().id(id)
                    .name(courseDtoIn.getName()).build()));
        } catch (RuntimeException exception) {
            LOGGER.error("Course can not be update!", exception);
            model.addAttribute("course_update_error", "Course can not be update!");
            return "courses/error";
        }
        LOGGER.debug("Course was successfully updated from UI form to DB");
        return "redirect:/courses";
    }
    
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(ID) int id, CourseDtoIn courseDtoIn, Model model) throws UniversityServiceException {
        LOGGER.debug("Try delete course {}", id);
        try {
            courseService.deleteCourse(convertToEntity(CourseDtoIn.builder().id(courseDtoIn.getId()).build()));
        } catch (RuntimeException exception) {
            LOGGER.error("Course {} can not be delete!", id, exception);
            model.addAttribute("course_delete_error", "Course can not be delete!");
            return "courses/error";
        }
        LOGGER.debug("Course was successfully deleted from UI form to DB {}", id);
        return "redirect:/courses";
    }
    
    private CourseDtoOut convertToDto(Course course) {
        return modelMapper.map(course, CourseDtoOut.class);
    }
    
    private Course convertToEntity(CourseDtoIn courseDtoIn) {
        return modelMapper.map(courseDtoIn, Course.class);
    }
    
    private List<CourseDtoOut> convertToDtoList(List<Course> coursesList) {
        return coursesList.stream().map(this::convertToDto).collect(Collectors.toList());
    }
    
    private List<Course> convertToEntityList(List<CourseDtoIn> coursesDtoInList) {
        return coursesDtoInList.stream().map(this::convertToEntity).collect(Collectors.toList());
    }
}
