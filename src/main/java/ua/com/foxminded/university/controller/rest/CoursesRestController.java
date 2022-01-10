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
import ua.com.foxminded.university.controller.web.rest.dto.input.CourseDtoIn;
import ua.com.foxminded.university.controller.web.rest.dto.output.CourseDtoOut;
import ua.com.foxminded.university.model.Course;
import ua.com.foxminded.university.service.CourseService;
import ua.com.foxminded.university.service.exception.UniversityServiceException;

@RestController
@RequestMapping("/rest/course")
public class CoursesRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CoursesRestController.class);
    private static final String ID = "id";
    
    private CourseService courseService;
    private ModelMapper modelMapper;
    
    @Autowired
    public CoursesRestController(CourseService courseService, ModelMapper modelMapper) {
        this.courseService = courseService;
        this.modelMapper = modelMapper;
    }
    
    @GetMapping()
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public List<CourseDtoOut> getAllCourses() throws UniversityServiceException, UniversityRestControllerException {
        LOGGER.debug("Try show all courses");
        List<CourseDtoOut> coursesDtoOutList = new ArrayList<>();
        try {
            coursesDtoOutList = convertToDtoList(courseService.getAllCourses());
        } catch (MappingException exception) {
            throw new UniversityRestControllerException("Courses not found!", exception);
        }
        LOGGER.debug("All courses was successfully showed");
        return coursesDtoOutList;
    }
    
    @GetMapping("/{id}")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public CourseDtoOut getCourse(@PathVariable(ID) int id) throws UniversityServiceException, UniversityRestControllerException {
        LOGGER.debug("Try show course {}", id);
        CourseDtoOut courseDtoOut = null;
        try {
            courseDtoOut = convertToDto(courseService.getCourse(id));
        } catch (MappingException exception) {
            throw new UniversityRestControllerException("Course with ID " + id + " not found!", exception);
        }
        LOGGER.debug("Course was successfully showed {}", id);
        return courseDtoOut;
    }
    
    @PostMapping()
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public List<CourseDtoOut> createCourse(@RequestBody @Valid CourseDtoIn courseDtoIn) throws UniversityServiceException {
        LOGGER.debug("Try create new course");
        List<Course> coursesList = courseService.saveCourse(convertToEntityList(Arrays.asList(CourseDtoIn.builder().name(courseDtoIn.getName()).build())));
        LOGGER.debug("New course was successfully saved from UI form to DB");
        return convertToDtoList(coursesList);
    }
    
    @PutMapping("/{id}")
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public CourseDtoOut updateCourse(@PathVariable(ID) int id, String name, @RequestBody @Valid CourseDtoIn courseDtoIn) throws UniversityServiceException, UniversityRestControllerException {
        LOGGER.debug("Try update course");
        Course course = null;
        try {
            course = courseService.getCourse(id);
            course = courseService.updateCourse(convertToEntity(CourseDtoIn.builder().id(id).name(courseDtoIn.getName()).build()));
        } catch (EntityNotFoundException exception) {
            throw new UniversityRestControllerException("Course with " + id + " can't be update ", exception);
        }
        LOGGER.debug("Course was successfully updated from UI form to DB");
        return convertToDto(course);
    }
    
    @DeleteMapping("/{id}")
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    public void deleteCourse(@PathVariable(ID) int id, CourseDtoIn courseDtoIn) throws UniversityServiceException, UniversityRestControllerException {
        LOGGER.debug("Try delete course {}", id);
        try {
            courseService
                    .deleteCourse(convertToEntity(CourseDtoIn.builder().id(courseDtoIn.getId()).build()));
        } catch (EmptyResultDataAccessException exception) { 
            throw new UniversityRestControllerException("Course with " + id + " can't be delete ", exception);
        }
        LOGGER.debug("Course was successfully deleted from UI form to DB {}", id);
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
