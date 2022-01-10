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

import ua.com.foxminded.university.controller.web.thymeleaf.dto.input.AuditoryDtoIn;
import ua.com.foxminded.university.controller.web.thymeleaf.dto.output.AuditoryDtoOut;
import ua.com.foxminded.university.model.Auditory;
import ua.com.foxminded.university.service.TimeTableService;
import ua.com.foxminded.university.service.exception.UniversityServiceException;

@Controller
@RequestMapping("/auditories")
public class AuditoriesController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuditoriesController.class);
    
    private static final String KEY_AUDITORY = "auditory";
    private static final String KEY_AUDITORY_DTO_IN = "auditoryDtoIn";
    private static final String ID = "id";
    
    private TimeTableService timeTableService;
    private ModelMapper modelMapper;
    
    @Autowired
    public AuditoriesController(TimeTableService timeTableService, ModelMapper modelMapper) {
        this.timeTableService = timeTableService;
        this.modelMapper = modelMapper;
    }
    
    @GetMapping()
    public String getAll(Model model) throws UniversityServiceException {
        LOGGER.debug("Try show all auditories");
        try { 
            model.addAttribute(KEY_AUDITORY, convertToDtoList(timeTableService.getAllAuditories()));
        } catch (RuntimeException exception) {
            LOGGER.error("Auditories not found!", exception);
            model.addAttribute("auditories_getall_error", "Auditories not found!");
            return "auditories/error";
        }
        LOGGER.debug("All auditories was successfully showed");
        return "auditories/index";
    }
    
    @GetMapping("show/{id}")
    public String get(@PathVariable(ID) int id, Model model) throws UniversityServiceException {
        LOGGER.debug("Try show auditory {}", id);
        try {
            model.addAttribute(KEY_AUDITORY, convertToDto(timeTableService.getAuditory(id)));
        } catch (RuntimeException exception) {
            LOGGER.error("Auditory {} not found!", id, exception);
            model.addAttribute("auditory_get_error", "Auditory not found!");
            return "auditories/error";
        }
        LOGGER.debug("Auditory was successfully showed {}", id);
        return "auditories/show";
    }
    
    @GetMapping("/new")
    public String createForm(Integer capacity, AuditoryDtoIn auditoryDtoIn) {
        LOGGER.debug("Try open form for create new auditory");
        return "auditories/new";
    }
    
    @PostMapping()
    public String create(Integer capacity, @Valid AuditoryDtoIn auditoryDtoIn, BindingResult bindingResult, Model model) throws UniversityServiceException {
        LOGGER.debug("Try save new auditory from UI form");
        if (bindingResult.hasErrors())
            return "auditories/new";
        try {
            timeTableService.saveAuditory(convertToEntityList(Arrays.asList(AuditoryDtoIn.builder().capacity(auditoryDtoIn.getCapacity()).build())));
        } catch (RuntimeException exception) {
            LOGGER.error("Auditory can not be craete!", exception);
            model.addAttribute("auditory_create_error", "Auditory can not be craete!");
            return "auditories/error";
        }
        LOGGER.debug("New auditory was successfully saved from UI form to DB");
        return "redirect:/auditories";
    }
    
    @GetMapping("/edit/{id}")
    public String updateForm(@PathVariable(ID) int id, Model model) throws UniversityServiceException {
        LOGGER.debug("Try get auditory for update {}", id);
        try {
            model.addAttribute(KEY_AUDITORY_DTO_IN, convertToDto(timeTableService.getAuditory(id)));
        } catch (RuntimeException exception) {
            LOGGER.error("Auditory {} not found!", id, exception);
            model.addAttribute("auditory_get_error", "Auditory not found!");
            return "auditories/error";
        }
        LOGGER.debug("Auditory was successfully got and ready for update {}", id);
        return "auditories/edit";
    }
    
    @PostMapping("/edit/{id}")
    public String update(@PathVariable(ID) int id, Integer capacity, @Valid AuditoryDtoIn auditoryDtoIn, 
            BindingResult bindingResult, Model model) throws UniversityServiceException {
        LOGGER.debug("Try open form UI for update auditory");
        if (bindingResult.hasErrors())
            return "auditories/edit";
        try {
            timeTableService.updateAuditory(convertToEntity(AuditoryDtoIn.builder().id(id)
                    .capacity(auditoryDtoIn.getCapacity()).build()));
        } catch (RuntimeException exception) {
            LOGGER.error("Auditory can not be update!", exception);
            model.addAttribute("auditory_update_error", "Auditory can not be update!");
            return "auditories/error";
        }
        LOGGER.debug("Auditory was successfully updated from UI form to DB");
        return "redirect:/auditories";
    }
    
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(ID) int id, AuditoryDtoIn auditoryDtoIn, Model model) throws UniversityServiceException {
        LOGGER.debug("Try delete auditory {}", id);
        try {
            timeTableService.deleteAuditory(convertToEntity(AuditoryDtoIn.builder().id(auditoryDtoIn.getId()).build()));
        } catch (RuntimeException exception) {
            LOGGER.error("Auditory {} can not be delete!", id, exception);
            model.addAttribute("auditory_delete_error", "Auditory can not be delete!");
            return "auditories/error";
        }
        LOGGER.debug("Auditory was successfully deleted from UI form to DB {}", id);
        return "redirect:/auditories";
    }
    
    private AuditoryDtoOut convertToDto(Auditory auditory) {
        return modelMapper.map(auditory, AuditoryDtoOut.class);
    }
    
    private Auditory convertToEntity(AuditoryDtoIn auditoryDtoIn) {
        return modelMapper.map(auditoryDtoIn, Auditory.class);
    }
    
    private List<AuditoryDtoOut> convertToDtoList(List<Auditory> auditoriesList) {
        return auditoriesList.stream().map(this::convertToDto).collect(Collectors.toList());
    }
    
    private List<Auditory> convertToEntityList(List<AuditoryDtoIn> auditoriesDtoInList) {
        return auditoriesDtoInList.stream().map(this::convertToEntity).collect(Collectors.toList());
    }
}
