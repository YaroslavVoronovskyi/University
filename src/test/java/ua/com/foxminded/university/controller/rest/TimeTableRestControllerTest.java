package ua.com.foxminded.university.controller.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import ua.com.foxminded.university.controller.web.rest.dto.input.TimeTableDtoIn;
import ua.com.foxminded.university.controller.web.rest.dto.output.TimeTableDtoOut;

public class TimeTableRestControllerTest extends AbstractRestControllerTest {
    private static final DateTimeFormatter TIME_FORMATT = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    @Test
    public void checkMethodGetAllShouldShowAllTimeTables() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/rest/timetable/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<TimeTableDtoOut> timeTablesDtoOutList = mapper.readerForListOf(TimeTableDtoOut.class).readValue(response);
        assertEquals(timeTablesDtoOutList.size(), 54);
        assertEquals(timeTablesDtoOutList.get(2).getId(), 3);
        assertEquals(timeTablesDtoOutList.get(2).getStartTime().format(TIME_FORMATT), "12:00:00");
        assertEquals(timeTablesDtoOutList.get(2).getEndTime().format(TIME_FORMATT), "13:30:00");
        assertEquals(timeTablesDtoOutList.get(2).getDate().format(DATE_FORMATT), "2021-12-15");
        assertEquals(timeTablesDtoOutList.get(3).getId(), 4);
        assertEquals(timeTablesDtoOutList.get(3).getStartTime().format(TIME_FORMATT), "14:00:00");
        assertEquals(timeTablesDtoOutList.get(3).getEndTime().format(TIME_FORMATT), "15:30:00");
        assertEquals(timeTablesDtoOutList.get(3).getDate().format(DATE_FORMATT), "2021-12-15");
        assertEquals(timeTablesDtoOutList.get(4).getId(), 5);
        assertEquals(timeTablesDtoOutList.get(4).getStartTime().format(TIME_FORMATT), "16:00:00");
        assertEquals(timeTablesDtoOutList.get(4).getEndTime().format(TIME_FORMATT), "17:30:00");
        assertEquals(timeTablesDtoOutList.get(4).getDate().format(DATE_FORMATT), "2021-12-15");
    }
    
    @Test
    public void checkMethodGetShouldShowNecessaryTimeTable() throws Exception {
        String response =  mockMvc.perform(MockMvcRequestBuilders.get("/rest/timetable/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        TimeTableDtoOut timeTableDtoOut = mapper.readValue(response, TimeTableDtoOut.class);
        assertEquals(timeTableDtoOut.getId(), 1);
        assertEquals(timeTableDtoOut.getStartTime().format(TIME_FORMATT), "21:00:00");
        assertEquals(timeTableDtoOut.getEndTime().format(TIME_FORMATT), "22:30:00");
        assertEquals(timeTableDtoOut.getDate().format(DATE_FORMATT), "2021-12-31");
    }
    
    @Test
    public void checkMethodCreateShouldCreateTimeTable() throws Exception {
        TimeTableDtoIn timeTableDtoIn = TimeTableDtoIn.builder().startTime(LocalTime.parse("21:00:00"))
                .endTime(LocalTime.parse("22:30:00")).date(LocalDate.parse("2021-12-31")).build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(timeTableDtoIn);  
        mockMvc.perform(MockMvcRequestBuilders.post("/rest/timetable").content(requestJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());        
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/rest/timetable/37")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        TimeTableDtoOut timeTableDtoOut = mapper.readValue(response, TimeTableDtoOut.class);
        assertEquals(timeTableDtoOut.getId(), 37);
        assertEquals(timeTableDtoOut.getStartTime().format(TIME_FORMATT), "21:00:00");
        assertEquals(timeTableDtoOut.getEndTime().format(TIME_FORMATT), "22:30:00");
        assertEquals(timeTableDtoOut.getDate().format(DATE_FORMATT), "2021-12-31");
    }
    
    @Test
    public void checkMethodUpdateShouldUpdateNecessaryTimeTable() throws Exception {
        TimeTableDtoIn timeTableDtoIn = TimeTableDtoIn.builder().startTime(LocalTime.parse("21:00:00"))
                .endTime(LocalTime.parse("22:30:00")).date(LocalDate.parse("2021-12-31")).build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(timeTableDtoIn);  
        String response = mockMvc.perform(MockMvcRequestBuilders.put("/rest/timetable/1").content(requestJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();
        TimeTableDtoOut timeTableDtoOut = mapper.readValue(response, TimeTableDtoOut.class);
        assertEquals(timeTableDtoOut.getId(), 1);
        assertEquals(timeTableDtoOut.getStartTime().format(TIME_FORMATT), "21:00:00");
        assertEquals(timeTableDtoOut.getEndTime().format(TIME_FORMATT), "22:30:00");
        assertEquals(timeTableDtoOut.getDate().format(DATE_FORMATT), "2021-12-31");
    }
    
    @Test
    public void checkMethodDeleteShouldDeleteNecessaryTimeTable() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/rest/timetable/10")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/rest/timetable/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<TimeTableDtoOut> timeTablesDtoOutList = mapper.readerForListOf(TimeTableDtoOut.class).readValue(response);
        assertEquals(timeTablesDtoOutList.size(), 23);
        assertFalse(timeTablesDtoOutList.contains(TimeTableDtoOut.builder().id(10).build()));
    }
    
    @Test
    public void checkMethodGetShouldThrowExceptionNecessaryTimeTable() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/rest/timetable/111")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void checkMethodCreateShouldThrowException() throws Exception {
        TimeTableDtoIn timeTableDtoIn = TimeTableDtoIn.builder().startTime(LocalTime.parse("11:00"))
                .endTime(LocalTime.parse("13:30")).date(LocalDate.parse("2021-12-12")).build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(timeTableDtoIn);  
        mockMvc.perform(MockMvcRequestBuilders.post("/rest/timetable").content(requestJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
    }
    
    @Test
    public void checkMethodUpdateFormShouldThrowExceptionNecessaryTimeTable() throws Exception {
        TimeTableDtoIn timeTableDtoIn = TimeTableDtoIn.builder().startTime(LocalTime.parse("21:00:00"))
                .endTime(LocalTime.parse("22:30:00")).date(LocalDate.parse("2021-12-31")).build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(timeTableDtoIn);
        mockMvc.perform(MockMvcRequestBuilders.get("/rest/timetable/111").content(requestJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isNotFound());
    }
    
    @Test
    public void checkMethodUpdateShouldThrowExceptionNecessaryTimeTable() throws Exception {
        TimeTableDtoIn timeTableDtoIn = TimeTableDtoIn.builder().startTime(LocalTime.parse("11:00"))
                .endTime(LocalTime.parse("13:30")).date(LocalDate.parse("2021-12-12")).build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(timeTableDtoIn);
        mockMvc.perform(MockMvcRequestBuilders.put("/rest/timetable/1").content(requestJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
    }
    
    @Test
    public void checkMethodDeleteShouldThrowExceptionNecessaryTimeTable() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/rest/timetable/111")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    } 
}
