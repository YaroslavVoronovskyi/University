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
import ua.com.foxminded.university.controller.web.rest.dto.input.GroupDtoIn;
import ua.com.foxminded.university.controller.web.rest.dto.output.GroupDtoOut;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.exception.UniversityServiceException;

@RestController
@RequestMapping("/rest/group")
public class GroupsRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupsRestController.class);
    private static final String ID = "id";
    
    private GroupService groupService;
    private ModelMapper modelMapper;
    
    @Autowired
    public GroupsRestController(GroupService groupService, ModelMapper modelMapper) {
        this.groupService = groupService;
        this.modelMapper = modelMapper;
    }
    
    @GetMapping()
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public List<GroupDtoOut> getAllGroups() throws UniversityServiceException, UniversityRestControllerException {
        LOGGER.debug("Try show all groups");
        List<GroupDtoOut> groupsDtoOutList = new ArrayList<>();
        try {
            groupsDtoOutList = convertToDtoList(groupService.getAllGroups());
        } catch (MappingException exception) {
            throw new UniversityRestControllerException("Groups not found!", exception);
        }
        LOGGER.debug("All groups was successfully showed");
        return groupsDtoOutList;
    }
    
    @GetMapping("/{id}")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public GroupDtoOut getGroup(@PathVariable(ID) int id) throws UniversityServiceException, UniversityRestControllerException {
        LOGGER.debug("Try show group {}", id);
        GroupDtoOut groupDtoOut = null;
        try {
            groupDtoOut = convertToDto(groupService.getGroup(id));
        } catch (MappingException exception) {
            throw new UniversityRestControllerException("Group with ID " + id + " not found!", exception);
        }
        LOGGER.debug("Group was successfully showed {}", id);
        return groupDtoOut;
    } 
    
    @PostMapping()
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public List<GroupDtoOut> createGroup(@RequestBody @Valid GroupDtoIn groupDtoIn) throws UniversityServiceException, UniversityRestControllerException {
        LOGGER.debug("Try create new group");
        List<Group> groupsList = groupService.saveGroup(convertToEntityList(Arrays.asList(GroupDtoIn.builder().name(groupDtoIn.getName()).build())));
        LOGGER.debug("New group was successfully saved from UI form to DB");
        return convertToDtoList(groupsList);
    }
    
    @PutMapping("/{id}")
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public GroupDtoOut updateGroup(@PathVariable(ID) int id, String name, @RequestBody @Valid GroupDtoIn groupDtoIn) throws UniversityServiceException, UniversityRestControllerException {
        LOGGER.debug("Try open form UI for update group");
        Group group = null;
        try {
            group = groupService.getGroup(id);
            group = groupService.updateGroup(convertToEntity(GroupDtoIn.builder().id(id).name(groupDtoIn.getName()).build()));
        } catch (EntityNotFoundException exception) {
            throw new UniversityRestControllerException("Group with " + id + " can't be update ", exception);
        }
        LOGGER.debug("Group was successfully updated from UI form to DB");
        return convertToDto(group);
    }
    
    @DeleteMapping("/{id}")
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    public void deleteGroup(@PathVariable(ID) int id, GroupDtoIn groupDtoIn) throws UniversityServiceException, UniversityRestControllerException {
        LOGGER.debug("Try delete group {}", id);
        try {
            groupService.deleteGroup(convertToEntity(GroupDtoIn.builder().id(groupDtoIn.getId()).build()));
        } catch (EmptyResultDataAccessException exception) {
            throw new UniversityRestControllerException("Auditory with " + id + " can't be delete ", exception);
        }
        LOGGER.debug("Group was successfully deleted from UI form to DB {}", id);
    }
    
    private GroupDtoOut convertToDto(Group group) {
        return modelMapper.map(group, GroupDtoOut.class);
    }
    
    private Group convertToEntity(GroupDtoIn groupDtoIn) {
        return modelMapper.map(groupDtoIn, Group.class);
    }
    
    private List<GroupDtoOut> convertToDtoList(List<Group> groupsList) {
        return groupsList.stream().map(this::convertToDto).collect(Collectors.toList());
    }
    
    private List<Group> convertToEntityList(List<GroupDtoIn> groupsDtoInList) {
        return groupsDtoInList.stream().map(this::convertToEntity).collect(Collectors.toList());
    }
}
