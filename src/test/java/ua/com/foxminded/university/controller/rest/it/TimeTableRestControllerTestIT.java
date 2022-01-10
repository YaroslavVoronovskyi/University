package ua.com.foxminded.university.controller.rest.it;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import ua.com.foxminded.university.controller.web.rest.dto.input.TimeTableDtoIn;
import ua.com.foxminded.university.controller.web.rest.dto.output.TimeTableDtoOut;

public class TimeTableRestControllerTestIT extends AbstractRestControllerTestIT {
    private static final DateTimeFormatter TIME_FORMATT = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    @Test
    public void checkMethodGetAllShouldShowAllTimeTables() throws Exception {
        String response = restTemplate.getForObject("http://localhost:" + port + "/rest/timetable/", String.class);
        ObjectMapper mapper = new ObjectMapper();
        List<TimeTableDtoOut> timeTablesDtoOutList = mapper.readerForListOf(TimeTableDtoOut.class).readValue(response);
        assertEquals(timeTablesDtoOutList.size(), 24);
        assertEquals(timeTablesDtoOutList.get(2).getId(), 3);
        assertEquals(timeTablesDtoOutList.get(2).getStartTime().format(TIME_FORMATT), "12:00:00");
        assertEquals(timeTablesDtoOutList.get(2).getEndTime().format(TIME_FORMATT), "13:30:00");
        assertEquals(timeTablesDtoOutList.get(2).getDate().format(DATE_FORMATT), "2022-12-15");
        assertEquals(timeTablesDtoOutList.get(3).getId(), 4);
        assertEquals(timeTablesDtoOutList.get(3).getStartTime().format(TIME_FORMATT), "14:00:00");
        assertEquals(timeTablesDtoOutList.get(3).getEndTime().format(TIME_FORMATT), "15:30:00");
        assertEquals(timeTablesDtoOutList.get(3).getDate().format(DATE_FORMATT), "2022-12-15");
        assertEquals(timeTablesDtoOutList.get(4).getId(), 5);
        assertEquals(timeTablesDtoOutList.get(4).getStartTime().format(TIME_FORMATT), "16:00:00");
        assertEquals(timeTablesDtoOutList.get(4).getEndTime().format(TIME_FORMATT), "17:30:00");
        assertEquals(timeTablesDtoOutList.get(4).getDate().format(DATE_FORMATT), "2022-12-15");
    }
    
    @Test
    public void checkMethodGetShouldShowNecessaryTimeTable() throws Exception {
        TimeTableDtoOut timeTableDtoOut = restTemplate.getForObject("http://localhost:" + port + "/rest/timetable/1", TimeTableDtoOut.class);       
        assertEquals(timeTableDtoOut.getId(), 1);
        assertEquals(timeTableDtoOut.getStartTime().format(TIME_FORMATT), "21:00:00");
        assertEquals(timeTableDtoOut.getEndTime().format(TIME_FORMATT), "22:30:00");
        assertEquals(timeTableDtoOut.getDate().format(DATE_FORMATT), "2022-12-31");
    }
    
    @Test
    public void checkMethodCreateShouldCreateTimeTable() throws Exception {
        TimeTableDtoIn timeTableDtoIn = TimeTableDtoIn.builder().startTime(LocalTime.parse("21:00:00"))
                .endTime(LocalTime.parse("22:30:00")).date(LocalDate.parse("2022-12-31")).build();
        restTemplate.postForObject("http://localhost:" + port + "/rest/timetable/", timeTableDtoIn, String.class);
        String response = restTemplate.getForObject("http://localhost:" + port + "/rest/timetable/", String.class);
        ObjectMapper mapper = new ObjectMapper();
        List<TimeTableDtoOut> timeTablesDtoOutList = mapper.readerForListOf(TimeTableDtoOut.class).readValue(response);
        assertEquals(timeTablesDtoOutList.size(), 18);        
        TimeTableDtoOut timeTableDtoOut = restTemplate.getForObject("http://localhost:" + port + "/rest/timetable/19", TimeTableDtoOut.class);
        assertEquals(timeTableDtoOut.getId(), 19);
        assertEquals(timeTableDtoOut.getStartTime().format(TIME_FORMATT), "21:00:00");
        assertEquals(timeTableDtoOut.getEndTime().format(TIME_FORMATT), "22:30:00");
        assertEquals(timeTableDtoOut.getDate().format(DATE_FORMATT), "2022-12-31");
    }
    
    @Test
    public void checkMethodUpdateShouldUpdateNecessaryTimeTable() throws Exception {
        TimeTableDtoIn timeTableDtoIn = TimeTableDtoIn.builder().startTime(LocalTime.parse("21:00:00"))
                .endTime(LocalTime.parse("22:30:00")).date(LocalDate.parse("2022-12-31")).build();
        restTemplate.put("http://localhost:" + port + "/rest/timetable/1", timeTableDtoIn);
        TimeTableDtoOut timeTableDtoOut = restTemplate.getForObject("http://localhost:" + port + "/rest/timetable/1", TimeTableDtoOut.class);
        assertEquals(timeTableDtoOut.getId(), 1);
        assertEquals(timeTableDtoOut.getStartTime().format(TIME_FORMATT), "21:00:00");
        assertEquals(timeTableDtoOut.getEndTime().format(TIME_FORMATT), "22:30:00");
        assertEquals(timeTableDtoOut.getDate().format(DATE_FORMATT), "2022-12-31");
    }
    
    @Test
    public void checkMethodDeleteShouldDeleteNecessaryTimeTable() throws Exception {
        restTemplate.delete("http://localhost:" + port + "/rest/timetable/10");
        String response = restTemplate.getForObject("http://localhost:" + port + "/rest/timetable/", String.class);
        ObjectMapper mapper = new ObjectMapper();
        List<TimeTableDtoOut> timeTablesDtoOutList = mapper.readerForListOf(TimeTableDtoOut.class).readValue(response);
        assertEquals(timeTablesDtoOutList.size(), 11);
        assertFalse(timeTablesDtoOutList.contains(TimeTableDtoOut.builder().id(10).build()));
    }    
}
