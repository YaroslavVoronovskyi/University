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
import ua.com.foxminded.university.controller.web.rest.dto.input.SubjectDtoIn;
import ua.com.foxminded.university.controller.web.rest.dto.output.SubjectDtoOut;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.exception.UniversityServiceException;

@RestController
@RequestMapping("/rest/subject")
public class SubjectsRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubjectsRestController.class);
    private static final String ID = "id";
    
    private GroupService groupService;
    private ModelMapper modelMapper;
    
    @Autowired
    public SubjectsRestController(GroupService groupService, ModelMapper modelMapper) {
        this.groupService = groupService;
        this.modelMapper = modelMapper;
    }
    
    @GetMapping()
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public List<SubjectDtoOut> getAllSubjects() throws UniversityServiceException, UniversityRestControllerException {
        LOGGER.debug("Try show all subjects");
        List<SubjectDtoOut> subjectsDtoOutList = new ArrayList<>();
        try {
            subjectsDtoOutList = convertToDtoList(groupService.getAllSubjects());
        } catch (MappingException exception) {
            throw new UniversityRestControllerException("Subjects not found!", exception);
        }
        LOGGER.debug("All subjects was successfully showed");
        return subjectsDtoOutList;
    }
    
    @GetMapping("/{id}")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public SubjectDtoOut getSubject(@PathVariable(ID) int id) throws UniversityServiceException, UniversityRestControllerException {
        LOGGER.debug("Try show subject {}", id);
        SubjectDtoOut subjectDtoOut = null;
        try {
            subjectDtoOut = convertToDto(groupService.getSubject(id));
        } catch (MappingException exception) {
            throw new UniversityRestControllerException("Subject with ID " + id + " not found!", exception);
        }
        LOGGER.debug("Subject was successfully showed {}", id);
        return subjectDtoOut;
    }
    
    @PostMapping()
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public List<SubjectDtoOut> createSubject(@RequestBody @Valid SubjectDtoIn subjectDtoIn) throws UniversityServiceException, UniversityRestControllerException {
        LOGGER.debug("Try create new subject");
        List<Subject> subjectsList = groupService.saveSubject(convertToEntityList(Arrays.asList(SubjectDtoIn
                .builder().name(subjectDtoIn.getName()).description(subjectDtoIn.getDescription()).build())));
        LOGGER.debug("New subject was successfully saved from UI form to DB");
        return convertToDtoList(subjectsList);
    }
    
    @PutMapping("/{id}")
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public SubjectDtoOut updateSubject(@PathVariable(ID) int id, String description,
            @RequestBody @Valid SubjectDtoIn subjectDtoIn) throws UniversityServiceException, UniversityRestControllerException {
        LOGGER.debug("Try open form UI for update subject");
        Subject subject = null;
        try {
            subject = groupService.getSubject(id);
            subject = groupService.updateSubject(convertToEntity(SubjectDtoIn.builder().id(id)
                    .name(subjectDtoIn.getName()).description(subjectDtoIn.getDescription()).build()));
        } catch (EntityNotFoundException exception) {
            throw new UniversityRestControllerException("Subject with " + id + " can't be update ", exception);
        }
        LOGGER.debug("Subject was successfully updated from UI form to DB");
        return convertToDto(subject);
    }
    
    @DeleteMapping("{id}")
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    public void deleteSubjects(@PathVariable(ID) int id, SubjectDtoIn subjectDtoIn) throws UniversityServiceException, UniversityRestControllerException {
        LOGGER.debug("Try delete subject {}", id);
        try {
            groupService
                    .deleteSubject(convertToEntity(SubjectDtoIn.builder().id(subjectDtoIn.getId()).build()));
        } catch (EmptyResultDataAccessException exception) {
            throw new UniversityRestControllerException("Subject with " + id + " can't be delete ", exception);
        }
        LOGGER.debug("Subject was successfully deleted from UI form to DB {}", id);
    }
    
    private SubjectDtoOut convertToDto(Subject subject) {
        return modelMapper.map(subject, SubjectDtoOut.class);
    }
    
    private Subject convertToEntity(SubjectDtoIn subjectDtoIn) {
        return modelMapper.map(subjectDtoIn, Subject.class);
    }
    
    private List<SubjectDtoOut> convertToDtoList(List<Subject> subjectsList) {
        return subjectsList.stream().map(this::convertToDto).collect(Collectors.toList());
    }
    
    private List<Subject> convertToEntityList(List<SubjectDtoIn> subjectsDtoInList) {
        return subjectsDtoInList.stream().map(this::convertToEntity).collect(Collectors.toList());
    }
}
