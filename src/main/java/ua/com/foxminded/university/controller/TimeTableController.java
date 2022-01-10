package ua.com.foxminded.university.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ua.com.foxminded.university.controller.web.thymeleaf.dto.input.TimeTableDtoIn;
import ua.com.foxminded.university.controller.web.thymeleaf.dto.output.TimeTableDtoOut;
import ua.com.foxminded.university.model.TimeTable;
import ua.com.foxminded.university.service.TimeTableService;
import ua.com.foxminded.university.service.exception.UniversityServiceException;

@Controller
@RequestMapping("/timetables")
public class TimeTableController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TimeTableController.class);
    
    private static final String KEY_TIME_TABLE = "timetable";
    private static final String KEY_TIME_TABLE_DTO_IN = "timeTableDtoIn";
    private static final String ID = "id";
    
    private TimeTableService timeTableService;
    private ModelMapper modelMapper;
    
    @Autowired
    public TimeTableController(TimeTableService timeTableService, ModelMapper modelMapper) {
        this.timeTableService = timeTableService;
        this.modelMapper = modelMapper;
    }
    
    @GetMapping()
    public String getAll(Model model) throws UniversityServiceException {
        LOGGER.debug("Try show all timetables");
        try {
            model.addAttribute(KEY_TIME_TABLE, convertToDtoList(timeTableService.getAllTimeTables()));
        } catch (RuntimeException exception) {
            LOGGER.error("TimeTables not found!", exception);
            model.addAttribute("timetables_getall_error", "TimeTables not found!");
            return "timetables/error";
        }
        LOGGER.debug("All timetables was successfully showed");
        return "timetables/index";
    }
    
    @GetMapping("show/{id}")
    public String get(@PathVariable(ID) int id, Model model) throws UniversityServiceException {
        LOGGER.debug("Try show timeTable {}", id);
        try {
            model.addAttribute(KEY_TIME_TABLE, convertToDto(timeTableService.getTimeTable(id)));
        } catch (RuntimeException exception) {
            LOGGER.error("TimeTable {} not found!", id, exception);
            model.addAttribute("timetable_get_error", "TimeTable not found!");
            return "timetables/error";
        }
        LOGGER.debug("TimeTable was successfully showed {}", id);
        return "timetables/show";
    }
    
    @GetMapping("/new")
    public String createForm(@DateTimeFormat(pattern = "HH:mm:ss") LocalTime startTime, 
            @DateTimeFormat(pattern = "HH:mm:ss") LocalTime endTime,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, TimeTableDtoIn timeTableDtoIn) {
        LOGGER.debug("Try open form for create new timeTable");
        return "timetables/new";
    }
    
    @PostMapping()
    public String create(@DateTimeFormat(pattern = "HH:mm:ss") LocalTime startTime,
            @DateTimeFormat(pattern = "HH:mm:ss") LocalTime endTime,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, @Valid TimeTableDtoIn timeTableDtoIn,
            BindingResult bindingResult, Model model) throws UniversityServiceException {
        LOGGER.debug("Try save new timetable from UI form");
        if (bindingResult.hasErrors())
            return "timetables/new";
        try {
            timeTableService.saveTimeTable(convertToEntityList(
                    Arrays.asList(TimeTableDtoIn.builder().startTime(timeTableDtoIn.getStartTime())
                            .endTime(timeTableDtoIn.getEndTime()).date(timeTableDtoIn.getDate()).build())));
        } catch (RuntimeException exception) {
            LOGGER.error("TimeTable can not be craete!", exception);
            model.addAttribute("timetable_create_error", "TimeTable can not be craete!");
            return "timetables/error";
        }
        LOGGER.debug("New timetable was successfully saved from UI form to DB");
        return "redirect:/timetables";
    }
    
    @GetMapping("/edit/{id}")
    public String updateForm(@PathVariable(ID) int id, Model model) throws UniversityServiceException {
        LOGGER.debug("Try get timetable for update {}", id);
        try {
            model.addAttribute(KEY_TIME_TABLE_DTO_IN, convertToDto(timeTableService.getTimeTable(id)));
        } catch (RuntimeException exception) {
            LOGGER.error("TimeTable {} not found!", id, exception);
            model.addAttribute("timetable_get_error", "TimeTable not found!");
            return "timetables/error";
        }
        LOGGER.debug("TimeTable was successfully got and ready for update {}", id);
        return "timetables/edit";
    }
    
    @PostMapping("/edit/{id}")
    public String update(@PathVariable(ID) int id, @DateTimeFormat(pattern = "HH:mm:ss") LocalTime startTime,
            @DateTimeFormat(pattern = "HH:mm:ss") LocalTime endTime,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, @Valid TimeTableDtoIn timeTableDtoIn,
            BindingResult bindingResult, Model model) throws UniversityServiceException {
        LOGGER.debug("Try open form UI for update timetable");
        if (bindingResult.hasErrors())
            return "timetables/edit";
        try {
            timeTableService.updateTimeTable(
                    convertToEntity(TimeTableDtoIn.builder().id(id).startTime(timeTableDtoIn.getStartTime())
                            .endTime(timeTableDtoIn.getEndTime()).date(timeTableDtoIn.getDate()).build()));
        } catch (RuntimeException exception) {
            LOGGER.error("TimeTable can not be update!", exception);
            model.addAttribute("timetable_update_error", "TimeTable can not be update!");
            return "timetables/error";
        }
        LOGGER.debug("TimeTable was successfully updated from UI form to DB");
        return "redirect:/timetables";
    }
    
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable(ID) int id, TimeTableDtoIn timeTableDtoIn, Model model) throws UniversityServiceException {
        LOGGER.debug("Try delete timetable {}", id);
        try {
            timeTableService.delete(convertToEntity(TimeTableDtoIn.builder().id(timeTableDtoIn.getId()).build()));
        } catch (RuntimeException exception) {
            LOGGER.error("TimeTable {} can not be delete!", id, exception);
            model.addAttribute("timetable_delete_error", "TimeTable can not be delete!");
            return "timetables/error";
        }
        LOGGER.debug("TimeTable was successfully deleted from UI form to DB {}", id);
        return "redirect:/timetables";
    }
    
    private TimeTableDtoOut convertToDto(TimeTable timeTable) {
        return modelMapper.map(timeTable, TimeTableDtoOut.class);
    }
    
    private TimeTable convertToEntity(TimeTableDtoIn timeTableDtoIn) {
        return modelMapper.map(timeTableDtoIn, TimeTable.class);
    }
    
    private List<TimeTableDtoOut> convertToDtoList(List<TimeTable> timeTablesList) {
        return timeTablesList.stream().map(this::convertToDto).collect(Collectors.toList());
    }
    
    private List<TimeTable> convertToEntityList(List<TimeTableDtoIn> timeTablesDtoInList) {
        return timeTablesDtoInList.stream().map(this::convertToEntity).collect(Collectors.toList());
    }
}
