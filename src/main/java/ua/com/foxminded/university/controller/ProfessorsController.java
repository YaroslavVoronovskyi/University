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

import ua.com.foxminded.university.controller.web.thymeleaf.dto.input.ProfessorDtoIn;
import ua.com.foxminded.university.controller.web.thymeleaf.dto.output.ProfessorDtoOut;
import ua.com.foxminded.university.model.Professor;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.exception.UniversityServiceException;

@Controller
@RequestMapping("/professors")
public class ProfessorsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfessorsController.class);
    
    private static final String KEY_PROFESSOR = "professor";
    private static final String KEY_PROFESSOR_DTO_IN = "professorDtoIn";
    private static final String ID = "id";
    
    private GroupService groupService;
    private ModelMapper modelMapper;
    
    @Autowired
    public ProfessorsController(GroupService groupService, ModelMapper modelMapper) {
        this.groupService = groupService;
        this.modelMapper = modelMapper;
    }
    
    @GetMapping()
    public String getAll(Model model) throws UniversityServiceException {
        LOGGER.debug("Try show all professors");
        try {
            model.addAttribute(KEY_PROFESSOR, convertToDtoList(groupService.getAllProfessors()));
        } catch (RuntimeException exception) {
            LOGGER.error("Professors not found!", exception);
            model.addAttribute("professors_getall_error", "Professors not found!");
            return "professors/error";
        }
        LOGGER.debug("All professors was successfully showed");
        return "professors/index";
    }
    
    @GetMapping("show/{id}")
    public String get(@PathVariable(ID) int id, Model model) throws UniversityServiceException {
        LOGGER.debug("Try show professor {}", id);
        try {
            model.addAttribute(KEY_PROFESSOR, convertToDto(groupService.getProfessor(id)));
        } catch (RuntimeException exception) {
            LOGGER.error("Professor {} not found!", id, exception);
            model.addAttribute("professor_get_error", "Professor not found!");
            return "professors/error";
        }
        LOGGER.debug("Professor was successfully showed {}", id);
        return "professors/show";
    }
    
    @GetMapping("/new")
    public String createForm(String firstName, String lastName, ProfessorDtoIn professorDtoIn) {
        LOGGER.debug("Try open form for create new professor");
        return "professors/new";
    }
    
    @PostMapping()
    public String create(String firstName, String lastName, @Valid ProfessorDtoIn professorDtoIn,
            BindingResult bindingResult, Model model) throws UniversityServiceException {
        LOGGER.debug("Try save new professor from UI form");
        try {
            if (bindingResult.hasErrors())
                return "professors/new";
            groupService.saveProfessor(convertToEntityList(Arrays.asList(ProfessorDtoIn.builder()
                    .firstName(professorDtoIn.getFirstName()).lastName(professorDtoIn.getLastName()).build())));
        } catch (RuntimeException exception) {
            LOGGER.error("Professor can not be craete!", exception);
            model.addAttribute("professor_create_error", "Professor can not be craete!");
            return "professors/error";
        }
        LOGGER.debug("New professor was successfully saved from UI form to DB");
        return "redirect:/professors";
    }
    
    @GetMapping("/edit/{id}")
    public String updateForm(@PathVariable(ID) int id, Model model) throws UniversityServiceException {
        LOGGER.debug("Try get professor for update {}", id);
        try {
            model.addAttribute(KEY_PROFESSOR_DTO_IN, convertToDto(groupService.getProfessor(id)));
        } catch (RuntimeException exception) {
            LOGGER.error("Professor {} not found!", id, exception);
            model.addAttribute("professor_get_error", "Professor not found!");
            return "professors/error";
        }
        LOGGER.debug("Professor was successfully got and ready for update {}", id);
        return "professors/edit";
    }
    
    @PostMapping("/edit/{id}")
    public String update(@PathVariable(ID) int id, String firstName, String lastName, @Valid ProfessorDtoIn professorDtoIn,
            BindingResult bindingResult, Model model) throws UniversityServiceException {
        LOGGER.debug("Try open form UI for update professor");
        try {
            if (bindingResult.hasErrors())
                return "professors/edit";
            groupService.updateProfessor(convertToEntity(ProfessorDtoIn.builder().id(id)
                    .firstName(professorDtoIn.getFirstName()).lastName(professorDtoIn.getLastName()).build()));
        } catch (RuntimeException exception) {
            LOGGER.error("Professor can not be update!", exception);
            model.addAttribute("professor_update_error", "Professor can not be update!");
            return "professors/error";
        }
        LOGGER.debug("Professor was successfully updated from UI form to DB");
        return "redirect:/professors";
    }
    
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(ID) int id, ProfessorDtoIn professorDtoIn, Model model) throws UniversityServiceException {
        LOGGER.debug("Try delete professor {}", id);
        try {
            groupService.deleteProfessor(convertToEntity(ProfessorDtoIn.builder().id(professorDtoIn.getId()).build()));
        } catch (RuntimeException exception) {
            LOGGER.error("Professor {} can not be delete!", id, exception);
            model.addAttribute("professor_delete_error", "Professor can not be delete!");
            return "professors/error";
        }
        LOGGER.debug("Professor was successfully deleted from UI form to DB {}", id);
        return "redirect:/professors";
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
