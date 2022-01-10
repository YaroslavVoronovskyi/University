package ua.com.foxminded.university.controller.rest;

import java.time.LocalDate;
import java.time.LocalTime;
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
import org.springframework.format.annotation.DateTimeFormat;
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
import ua.com.foxminded.university.controller.web.rest.dto.input.TimeTableDtoIn;
import ua.com.foxminded.university.controller.web.rest.dto.output.TimeTableDtoOut;
import ua.com.foxminded.university.model.TimeTable;
import ua.com.foxminded.university.service.TimeTableService;
import ua.com.foxminded.university.service.exception.UniversityServiceException;

@RestController
@RequestMapping("/rest/timetable")
public class TimeTableRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TimeTableRestController.class);
    private static final String ID = "id";
    
    private TimeTableService timeTableService;
    private ModelMapper modelMapper;
    
    @Autowired
    public TimeTableRestController(TimeTableService timeTableService, ModelMapper modelMapper) {
        this.timeTableService = timeTableService;
        this.modelMapper = modelMapper;
    }
    
    @GetMapping()
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public List<TimeTableDtoOut> getAllTimeTables() throws UniversityServiceException, UniversityRestControllerException {
        LOGGER.debug("Try show all timetables");
        List<TimeTableDtoOut> timeTablesDtoOutList = new ArrayList<>();
        try {
            timeTablesDtoOutList = convertToDtoList(timeTableService.getAllTimeTables());
        } catch (MappingException exception) {
            throw new UniversityRestControllerException("TimeTables not found!", exception);
        }
        LOGGER.debug("All timetables was successfully showed");
        return timeTablesDtoOutList;
    }
    
    @GetMapping("/{id}")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public TimeTableDtoOut getTimeTable(@PathVariable(ID) int id) throws UniversityServiceException, UniversityRestControllerException {
        LOGGER.debug("Try show timeTable {}", id);
        TimeTableDtoOut timeTableDtoOut = null;
        try {
            timeTableDtoOut = convertToDto(timeTableService.getTimeTable(id));
        } catch (MappingException exception) {
            throw new UniversityRestControllerException("TimeTable with ID " + id + " not found!", exception);
        }
        LOGGER.debug("TimeTable was successfully showed {}", id);
        return timeTableDtoOut;
    }

    @PostMapping()
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public List<TimeTableDtoOut> createTimeTable(@RequestBody @Valid TimeTableDtoIn timeTableDtoIn)
            throws UniversityServiceException, UniversityRestControllerException {
        LOGGER.debug("Try create new timetable");
        List<TimeTable> timeTablesList = timeTableService.saveTimeTable(convertToEntityList(
                Arrays.asList(TimeTableDtoIn.builder().startTime(timeTableDtoIn.getStartTime())
                        .endTime(timeTableDtoIn.getEndTime()).date(timeTableDtoIn.getDate()).build())));
        LOGGER.debug("New timetable was successfully saved from UI form to DB");
        return convertToDtoList(timeTablesList);
    }
    
    @PutMapping("/{id}")
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public TimeTableDtoOut updateTimeTable(@PathVariable(ID) int id,
            @DateTimeFormat(pattern = "HH:mm:ss") LocalTime startTime, @DateTimeFormat(pattern = "HH:mm:ss") LocalTime endTime,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, @RequestBody @Valid TimeTableDtoIn timeTableDtoIn)
            throws UniversityServiceException, UniversityRestControllerException {
        LOGGER.debug("Try open form UI for update timetable");
        TimeTable timeTable = null;
        try {
            timeTable = timeTableService.getTimeTable(id);
            timeTable = timeTableService.updateTimeTable(convertToEntity(TimeTableDtoIn.builder().id(id).startTime(timeTableDtoIn.getStartTime())
                    .endTime(timeTableDtoIn.getEndTime()).date(timeTableDtoIn.getDate()).build()));
        } catch (EntityNotFoundException exception) {
            throw new UniversityRestControllerException("TimeTable with " + id + " can't be update ", exception);
        }
        LOGGER.debug("TimeTable was successfully updated from UI form to DB");
        return convertToDto(timeTable);
    }
    
    @DeleteMapping("/{id}")
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    public void deleteTimeTable(@PathVariable(ID) int id, TimeTableDtoIn timeTableDtoIn) throws UniversityServiceException, UniversityRestControllerException {
        LOGGER.debug("Try delete timetable {}", id);
        try {
            timeTableService
                    .delete(convertToEntity(TimeTableDtoIn.builder().id(timeTableDtoIn.getId()).build()));
        } catch (EmptyResultDataAccessException exception) {
            throw new UniversityRestControllerException("Auditory with " + id + " can't be delete ", exception);
        }
        LOGGER.debug("TimeTable was successfully deleted from UI form to DB {}", id);
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
