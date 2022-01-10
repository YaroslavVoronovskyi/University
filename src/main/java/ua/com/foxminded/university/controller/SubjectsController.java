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

import ua.com.foxminded.university.controller.web.thymeleaf.dto.input.SubjectDtoIn;
import ua.com.foxminded.university.controller.web.thymeleaf.dto.output.SubjectDtoOut;
import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.exception.UniversityServiceException;

@Controller
@RequestMapping("/subjects")
public class SubjectsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubjectsController.class);
    
    private static final String KEY_SUBJECT = "subject";
    private static final String KEY_SUBJECT_DTO_IN = "subjectDtoIn";
    private static final String ID = "id";
    
    private GroupService groupService;
    private ModelMapper modelMapper;
    
    @Autowired
    public SubjectsController(GroupService groupService, ModelMapper modelMapper) {
        this.groupService = groupService;
        this.modelMapper = modelMapper;
    }
    
    @GetMapping()
    public String getAll(Model model) throws UniversityServiceException {
        LOGGER.debug("Try show all subjects");
        try {
            model.addAttribute(KEY_SUBJECT, convertToDtoList(groupService.getAllSubjects()));
        } catch (RuntimeException exception) {
            LOGGER.error("Subjects not found!", exception);
            model.addAttribute("subjects_getall_error", "Subjects not found!");
            return "subjects/error";
        }
        LOGGER.debug("All subjects was successfully showed");
        return "subjects/index";
    }
    
    @GetMapping("show/{id}")
    public String get(@PathVariable(ID) int id, Model model) throws UniversityServiceException {
        LOGGER.debug("Try show subject {}", id);
        try {
            model.addAttribute(KEY_SUBJECT, convertToDto(groupService.getSubject(id)));
        } catch (RuntimeException exception) {
            LOGGER.error("Subject {} not found!", id, exception);
            model.addAttribute("subject_get_error", "Subject not found!");
            return "subjects/error";
        }
        LOGGER.debug("Subject was successfully showed {}", id);
        return "subjects/show";
    }
    
    @GetMapping("/new")
    public String createForm(String name, String description, SubjectDtoIn subjectDtoIn) {
        LOGGER.debug("Try open form for create new subject");
        return "subjects/new";
    }
    
    @PostMapping()
    public String create(String name, String description, @Valid SubjectDtoIn subjectDtoIn, 
            BindingResult bindingResult, Model model) throws UniversityServiceException {
        LOGGER.debug("Try save new subject from UI form");
        try {
            if (bindingResult.hasErrors())
                return "subjects/new";
            groupService.saveSubject(convertToEntityList(Arrays.asList(SubjectDtoIn.builder()
                    .name(subjectDtoIn.getName()).description(subjectDtoIn.getDescription()).build())));
        } catch (RuntimeException exception) {
            LOGGER.error("Subject can not be craete!", exception);
            model.addAttribute("subject_create_error", "Subject can not be craete!");
            return "subjects/error";
        }
        LOGGER.debug("New subject was successfully saved from UI form to DB");
        return "redirect:/subjects";
    }
    
    @GetMapping("/edit/{id}")
    public String updateForm(@PathVariable(ID) int id, Model model) throws UniversityServiceException {
        LOGGER.debug("Try get subject for update {}", id);
        try {
            model.addAttribute(KEY_SUBJECT_DTO_IN, convertToDto(groupService.getSubject(id)));
        } catch (RuntimeException exception) {
            LOGGER.error("Subject {} not found!", id, exception);
            model.addAttribute("subject_get_error", "Subject not found!");
            return "subjects/error";
        }
        LOGGER.debug("Subject was successfully got and ready for update {}", id);
        return "subjects/edit";
    }
    
    @PostMapping("/edit/{id}")
    public String update(@PathVariable(ID) int id, String description, @Valid SubjectDtoIn subjectDtoIn,
            BindingResult bindingResult, Model model) throws UniversityServiceException {
        LOGGER.debug("Try open form UI for update subject");
        try {
            if (bindingResult.hasErrors())
                return "subjects/edit";
            groupService.updateSubject(convertToEntity(SubjectDtoIn.builder().id(id)
                    .name(subjectDtoIn.getName()).description(subjectDtoIn.getDescription()).build()));
        } catch (RuntimeException exception) {
            LOGGER.error("Subject can not be update!", exception);
            model.addAttribute("subject_update_error", "Subject can not be update!");
            return "subjects/error";
        }
        LOGGER.debug("Subject was successfully updated from UI form to DB");
        return "redirect:/subjects";
    }
    
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(ID) int id, SubjectDtoIn subjectDtoIn, Model model) throws UniversityServiceException {
        LOGGER.debug("Try delete subject {}", id);
        try {
            groupService.deleteSubject(convertToEntity(SubjectDtoIn.builder().id(subjectDtoIn.getId()).build()));
        } catch (RuntimeException exception) {
            LOGGER.error("Subject {} can not be delete!", id, exception);
            model.addAttribute("subject_delete_error", "Subject can not be delete!");
            return "subjects/error";
        }
        LOGGER.debug("Subject was successfully deleted from UI form to DB {}", id);
        return "redirect:/subjects";
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
