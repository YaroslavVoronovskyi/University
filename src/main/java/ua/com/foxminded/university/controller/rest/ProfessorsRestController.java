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
import ua.com.foxminded.university.controller.web.rest.dto.input.ProfessorDtoIn;
import ua.com.foxminded.university.controller.web.rest.dto.output.ProfessorDtoOut;
import ua.com.foxminded.university.model.Professor;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.exception.UniversityServiceException;

@RestController
@RequestMapping("/rest/professor")
public class ProfessorsRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfessorsRestController.class);
    private static final String ID = "id";
    
    private GroupService groupService;
    private ModelMapper modelMapper;
    
    @Autowired
    public ProfessorsRestController(GroupService groupService, ModelMapper modelMapper) {
        this.groupService = groupService;
        this.modelMapper = modelMapper;
    }
    
    @GetMapping()
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public List<ProfessorDtoOut> getAllProfessors() throws UniversityServiceException, UniversityRestControllerException {
        LOGGER.debug("Try show all professors");
        List<ProfessorDtoOut> professorsDtoOutList = new ArrayList<>();
        try {
            professorsDtoOutList = convertToDtoList(groupService.getAllProfessors());
        } catch (MappingException exception) {
            throw new UniversityRestControllerException("Professors not found!", exception);
        }
        LOGGER.debug("All professors was successfully showed");
        return professorsDtoOutList;
    }
    
    @GetMapping("/{id}")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public ProfessorDtoOut getProfessor(@PathVariable(ID) int id) throws UniversityServiceException, UniversityRestControllerException {
        LOGGER.debug("Try show professor {}", id);
        ProfessorDtoOut professorDtoOut = null;
        try {
            professorDtoOut = convertToDto(groupService.getProfessor(id));
        } catch (MappingException exception) {
            throw new UniversityRestControllerException("Professor with ID " + id + " not found!", exception);
        }
        LOGGER.debug("Professor was successfully showed {}", id);
        return professorDtoOut;
    }
    
    @PostMapping()
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public List<ProfessorDtoOut> createProfessor(@RequestBody @Valid ProfessorDtoIn professorDtoIn) throws UniversityServiceException, UniversityRestControllerException {
        LOGGER.debug("Try create new professor");
        List<Professor> professorsList = groupService.saveProfessor(convertToEntityList(
                Arrays.asList(ProfessorDtoIn.builder().firstName(professorDtoIn.getFirstName())
                        .lastName(professorDtoIn.getLastName()).build())));
        LOGGER.debug("New professor was successfully saved from UI form to DB");
        return convertToDtoList(professorsList);
    }
    
    @PutMapping("/{id}")
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public ProfessorDtoOut updateProfessor(@PathVariable(ID) int id, String firstName, String lastName, 
            @RequestBody @Valid ProfessorDtoIn professorDtoIn) throws UniversityServiceException, UniversityRestControllerException {
        LOGGER.debug("Try open form UI for update professor");
        Professor professor = null;       
        try {
            professor = groupService.getProfessor(id);
            professor = groupService.updateProfessor(convertToEntity(ProfessorDtoIn.builder().id(id).firstName(professorDtoIn.getFirstName())
                    .lastName(professorDtoIn.getLastName()).build()));
        } catch (EntityNotFoundException exception) {
            throw new UniversityRestControllerException("Professor with " + id + " can't be update ", exception);
        }
        LOGGER.debug("Professor was successfully updated from UI form to DB");
        return convertToDto(professor);
    }
    
    @DeleteMapping("/{id}")
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    public void deleteProfessor(@PathVariable(ID) int id, ProfessorDtoIn professorDtoIn) throws UniversityServiceException, UniversityRestControllerException {
        LOGGER.debug("Try delete professor {}", id);
        try {
            groupService.deleteProfessor(
                    convertToEntity(ProfessorDtoIn.builder().id(professorDtoIn.getId()).build()));
        } catch (EmptyResultDataAccessException exception) {
            throw new UniversityRestControllerException("Professor with " + id + " can't be delete ", exception);
        }
        LOGGER.debug("Professor was successfully deleted from UI form to DB {}", id);
    }
    
    private ProfessorDtoOut convertToDto(Professor professor) {
        return modelMapper.map(professor, ProfessorDtoOut.class);
    }
    
    private Professor convertToEntity(ProfessorDtoIn professorDtoIn) {
        return modelMapper.map(professorDtoIn, Professor.class);
    }
    
    private List<ProfessorDtoOut> convertToDtoList(List<Professor> professorsList) {
        return professorsList.stream().map(this::convertToDto).collect(Collectors.toList());
    }
    
    private List<Professor> convertToEntityList(List<ProfessorDtoIn> professorsDtoInsList) {
        return professorsDtoInsList.stream().map(this::convertToEntity).collect(Collectors.toList());
    }
}
