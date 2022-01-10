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

import ua.com.foxminded.university.controller.web.thymeleaf.dto.input.GroupDtoIn;
import ua.com.foxminded.university.controller.web.thymeleaf.dto.output.GroupDtoOut;
import ua.com.foxminded.university.model.Group;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.exception.UniversityServiceException;

@Controller
@RequestMapping("/groups")
public class GroupsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GroupsController.class);
    
    private static final String KEY_GROUP = "group";
    private static final String KEY_GROUP_DTO_IN = "groupDtoIn";
    private static final String ID = "id";
    
    private GroupService groupService;
    private ModelMapper modelMapper;
    
    @Autowired
    public GroupsController(GroupService groupService, ModelMapper modelMapper) {
        this.groupService = groupService;
        this.modelMapper = modelMapper;
    }
    
    @GetMapping()
    public String getAll(Model model) throws UniversityServiceException {
        LOGGER.debug("Try show all groups");
        try {
            model.addAttribute(KEY_GROUP, convertToDtoList(groupService.getAllGroups()));
        } catch (RuntimeException exception) {
            LOGGER.error("Groups not found!", exception);
            model.addAttribute("groups_getall_error", "Groups not found!");
            return "groups/error";
        }
        LOGGER.debug("All groups was successfully showed");
        return "groups/index";
    }
    
    @GetMapping("show/{id}")
    public String get(@PathVariable(ID) int id, Model model) throws UniversityServiceException {
        LOGGER.debug("Try show group {}", id);
        try {
            model.addAttribute(KEY_GROUP, convertToDto(groupService.getGroup(id)));
        } catch (RuntimeException exception) {
            LOGGER.error("Group {} not found!", id, exception);
            model.addAttribute("group_get_error", "Group not found!");
            return "groups/error";
        }
        LOGGER.debug("Group was successfully showed {}", id);
        return "groups/show";
    }
    
    @GetMapping("/new")
    public String createForm(String name, GroupDtoIn groupDtoIn) {
        LOGGER.debug("Try open form for create new group");
        return "groups/new";
    }
    
    @PostMapping()
    public String create(String name, @Valid GroupDtoIn groupDtoIn, BindingResult bindingResult, Model model) throws UniversityServiceException {
        LOGGER.debug("Try save new group from UI form");
        try {
            if (bindingResult.hasErrors())
                return "groups/new";
            groupService.saveGroup(convertToEntityList(Arrays.asList(GroupDtoIn.builder().name(groupDtoIn.getName()).build())));
        } catch (RuntimeException exception) {
            LOGGER.error("Group can not be craete!", exception);
            model.addAttribute("group_create_error", "Group can not be craete!");
            return "groups/error";
        }
        LOGGER.debug("New group was successfully saved from UI form to DB");
        return "redirect:/groups";
    }
    
    @GetMapping("/edit/{id}")
    public String updateForm(@PathVariable(ID) int id, Model model) throws UniversityServiceException {
        LOGGER.debug("Try get group for update {}", id);
        try {
            model.addAttribute(KEY_GROUP_DTO_IN, convertToDto(groupService.getGroup(id)));
        } catch (RuntimeException exception) {
            LOGGER.error("Group {} not found!", id, exception);
            model.addAttribute("group_get_error", "Group not found!");
            return "groups/error";
        }
        LOGGER.debug("Group was successfully got and ready for update {}", id);
        return "groups/edit";
    }
    
    @PostMapping("/edit/{id}")
    public String update(@PathVariable(ID) int id, String name, @Valid GroupDtoIn groupDtoIn, 
            BindingResult bindingResult, Model model) throws UniversityServiceException {
        LOGGER.debug("Try open form UI for update group");
        try {
            if (bindingResult.hasErrors())
                return "groups/edit";
            groupService.updateGroup(convertToEntity(GroupDtoIn.builder().id(id)
                    .name(groupDtoIn.getName()).build()));
        } catch (RuntimeException exception) {
            LOGGER.error("Group can not be update!", exception);
            model.addAttribute("group_update_error", "Group can not be update!");
            return "groups/error";
        }
        LOGGER.debug("Group was successfully updated from UI form to DB");
        return "redirect:/groups";
    }
    
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(ID) int id, GroupDtoIn groupDtoIn, Model model) throws UniversityServiceException {
        LOGGER.debug("Try delete group {}", id);
        try {
            groupService.deleteGroup(convertToEntity(GroupDtoIn.builder().id(groupDtoIn.getId()).build()));
        } catch (RuntimeException exception) {
            LOGGER.error("Group {} can not be delete!", id, exception);
            model.addAttribute("group_delete_error", "Group can not be delete!");
            return "groups/error";
        }
        LOGGER.debug("Group was successfully deleted from UI form to DB {}", id);
        return "redirect:/groups";
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
