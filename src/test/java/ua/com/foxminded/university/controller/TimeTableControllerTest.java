package ua.com.foxminded.university.controller;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.MediaType;

import ua.com.foxminded.university.model.TimeTable;
import ua.com.foxminded.university.service.TimeTableService;

public class TimeTableControllerTest extends AbstractControllerTest {
    private static final DateTimeFormatter TIME_FORMATT = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Mock
    TimeTableService timeTableServiceMock;    
    @InjectMocks
    TimeTableController timeTableController;
    
    @Test 
    public void checkMethodGetAllShouldShowAllTimeTables() throws Exception {
        Mockito.when(timeTableServiceMock.getAllTimeTables()).thenReturn(createFakeTimeTablesList());
        timeTableServiceMock.getAllTimeTables();
        mockMvc.perform(get("/timetables")).andDo(print())
        .andExpect(status().isOk())
                .andExpect(view().name("timetables/index"))
                .andExpect(model().attributeExists("timetable"))
                .andExpect(model().attribute("timetable", hasSize(66)))           
                .andExpect(model().attribute("timetable", hasItem(allOf(hasProperty("id", is(1)), 
                        hasProperty("startTime", is(LocalTime.parse("17:00:00", TIME_FORMATT))), 
                        hasProperty("endTime", is(LocalTime.parse("18:30:00", TIME_FORMATT))),
                        hasProperty("date", is(LocalDate.parse("2021-12-15", DATE_FORMATT)))))))
                .andExpect(model().attribute("timetable", hasItem(allOf(hasProperty("id", is(2)), 
                        hasProperty("startTime", is(LocalTime.parse("19:00:00", TIME_FORMATT))), 
                        hasProperty("endTime", is(LocalTime.parse("20:30:00", TIME_FORMATT))),
                        hasProperty("date", is(LocalDate.parse("2021-12-15", DATE_FORMATT)))))))
                .andExpect(model().attribute("timetable", hasItem(allOf(hasProperty("id", is(3)), 
                        hasProperty("startTime", is(LocalTime.parse("12:00:00", TIME_FORMATT))), 
                        hasProperty("endTime", is(LocalTime.parse("13:30:00", TIME_FORMATT))),
                        hasProperty("date", is(LocalDate.parse("2021-12-15", DATE_FORMATT)))))))
                .andExpect(model().attribute("timetable", hasItem(allOf(hasProperty("id", is(4)), 
                        hasProperty("startTime", is(LocalTime.parse("14:00:00", TIME_FORMATT))), 
                        hasProperty("endTime", is(LocalTime.parse("15:30:00", TIME_FORMATT))),
                        hasProperty("date", is(LocalDate.parse("2021-12-15", DATE_FORMATT)))))))
                .andExpect(model().attribute("timetable", hasItem(allOf(hasProperty("id", is(5)), 
                        hasProperty("startTime", is(LocalTime.parse("16:00:00", TIME_FORMATT))), 
                        hasProperty("endTime", is(LocalTime.parse("17:30:00", TIME_FORMATT))),
                        hasProperty("date", is(LocalDate.parse("2021-12-15", DATE_FORMATT)))))))
                .andExpect(model().attribute("timetable", hasItem(allOf(hasProperty("id", is(6)), 
                        hasProperty("startTime", is(LocalTime.parse("18:00:00", TIME_FORMATT))), 
                        hasProperty("endTime", is(LocalTime.parse("19:30:00", TIME_FORMATT))),
                        hasProperty("date", is(LocalDate.parse("2021-12-15", DATE_FORMATT)))))));                      
        Mockito.verify(timeTableServiceMock, times(1)).getAllTimeTables();
        Mockito.verifyNoMoreInteractions(timeTableServiceMock);
    }
    
    @Test
    public void checkMethodGetShouldShowNecessaryTimeTable() throws Exception {
        Mockito.when(timeTableServiceMock.getTimeTable(1)).thenReturn(createFakeTimeTable());
        timeTableServiceMock.getTimeTable(1);
        mockMvc.perform(get("/timetables/show/1")).andDo(print())
        .andExpect(status().isOk())
                .andExpect(view().name("timetables/show"))
                .andExpect(model().attributeExists("timetable"))
                .andExpect(model().attribute("timetable", hasProperty("id", is(1))))
                .andExpect(model().attribute("timetable", hasProperty("startTime", is(LocalTime.parse("17:00:00", TIME_FORMATT)))))
                .andExpect(model().attribute("timetable", hasProperty("endTime", is(LocalTime.parse("18:30:00", TIME_FORMATT)))))
                .andExpect(model().attribute("timetable", hasProperty("date", is(LocalDate.parse("2021-12-15", DATE_FORMATT)))));                      
        Mockito.verify(timeTableServiceMock, times(1)).getTimeTable(1);
        Mockito.verifyNoMoreInteractions(timeTableServiceMock);
    }
    
    @Test
    public void checkMethodCreateFormShouldOpenFormForCreateTimeTable() throws Exception {
        mockMvc.perform(get("/timetables/new")).andDo(print())
        .andExpect(status().isOk())
                .andExpect(view().name("timetables/new"));
    }
    
    @Test
    public void checkMethodCreateShouldCreateTimeTable() throws Exception {
        timeTableServiceMock.saveTimeTable(Arrays.asList(createFakeTimeTable()));
        mockMvc.perform(post("/timetables")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("startTime", "17:00:00")
                .param("endTime", "18:30:00")
                .param("date", "2021-12-15")
                .sessionAttr("timetable", new TimeTable()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/timetables"));
        ArgumentCaptor<TimeTable> argumentCaptor = ArgumentCaptor.forClass(TimeTable.class);
        Mockito.verify(timeTableServiceMock, times(1)).saveTimeTable(Arrays.asList(argumentCaptor.capture()));
        Mockito.verifyNoMoreInteractions(timeTableServiceMock);
    }
    
    @Test
    public void checkMethodUpdateFormShouldOpenNecessaryTimeTable() throws Exception {
        Mockito.when(timeTableServiceMock.getTimeTable(1)).thenReturn(createFakeTimeTable());
        timeTableServiceMock.getTimeTable(1);
        mockMvc.perform(get("/timetables/edit/1")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("timetables/edit")).andExpect(model().attributeExists("timeTableDtoIn"))
                .andExpect(model().attribute("timeTableDtoIn", hasProperty("id", is(1))))
                .andExpect(model().attribute("timeTableDtoIn", hasProperty("startTime", is(LocalTime.parse("17:00:00", TIME_FORMATT)))))
                .andExpect(model().attribute("timeTableDtoIn", hasProperty("endTime", is(LocalTime.parse("18:30:00", TIME_FORMATT)))))
                .andExpect(model().attribute("timeTableDtoIn", hasProperty("date", is(LocalDate.parse("2021-12-15", DATE_FORMATT)))));
        Mockito.verify(timeTableServiceMock, times(1)).getTimeTable(1);
        Mockito.verifyNoMoreInteractions(timeTableServiceMock);
    }
    
    @Test
    public void checkMethodUpdateShouldUpdateNecessaryTimeTable() throws Exception {
        timeTableServiceMock.updateTimeTable(createFakeTimeTable());
        mockMvc.perform(post("/timetables/edit/1?startTime=17:00:00&endTime=18:30:00&date=2021-12-15")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("startTime", "17:00:00")
                .param("endTime", "18:30:00")
                .param("date", "2021-12-15"))
        .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/timetables"));
        ArgumentCaptor<TimeTable> argumentCaptor = ArgumentCaptor.forClass(TimeTable.class);
        Mockito.verify(timeTableServiceMock, times(1)).updateTimeTable(argumentCaptor.capture());
        Mockito.verifyNoMoreInteractions(timeTableServiceMock);
    }
    
    @Test
    public void checkMethodDeleteShouldDeleteNecessaryTimeTable() throws Exception {
        mockMvc.perform(get("/timetables/delete/11")).andDo(print())
        .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/timetables"));
    }
    
    @Test
    public void checkMethodGetShouldThrowExceptionNecessaryTimeTable() throws Exception {
        mockMvc.perform(get("/timetables/show/111")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("timetables/error"));
    }
    
    @Test
    public void checkMethodCreateShouldThrowException() throws Exception {
        mockMvc.perform(post("/timetables?startTime=10:00:00")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("timetables/new"));
    }
    
    @Test
    public void checkMethodUpdateFormShouldThrowExceptionNecessaryTimeTable() throws Exception {
        mockMvc.perform(get("/timetables/edit/111")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("timetables/error"));
    }
    
    @Test
    public void checkMethodUpdateShouldThrowExceptionNecessaryTimeTable() throws Exception {
        mockMvc.perform(post("/timetables/edit/111?startTime=10:00:00")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("timetables/edit"));
    }
    
    @Test
    public void checkMethodDeleteShouldThrowExceptionNecessaryTimeTable() throws Exception {
        mockMvc.perform(get("/timetables/delete/111")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("timetables/error"));
    }
    
    private TimeTable createFakeTimeTable() {
        return TimeTable.builder().id(1).startTime(LocalTime.parse("17:00:00", TIME_FORMATT))
                .endTime(LocalTime.parse("18:30:00", TIME_FORMATT))
                .date(LocalDate.parse("2021-12-15", DATE_FORMATT)).build();
    }
    
    private List<TimeTable> createFakeTimeTablesList() {
        return Arrays.asList(
                TimeTable.builder().id(1).startTime(LocalTime.parse("17:00:00", TIME_FORMATT))
                        .endTime(LocalTime.parse("18:30:00", TIME_FORMATT))
                        .date(LocalDate.parse("2021-12-15", DATE_FORMATT)).build(),
                TimeTable.builder().id(2).startTime(LocalTime.parse("19:00:00", TIME_FORMATT))
                        .endTime(LocalTime.parse("20:30:00", TIME_FORMATT))
                        .date(LocalDate.parse("2021-12-15", DATE_FORMATT)).build(),
                TimeTable.builder().id(3).startTime(LocalTime.parse("12:00:00", TIME_FORMATT))
                        .endTime(LocalTime.parse("13:30:00", TIME_FORMATT))
                        .date(LocalDate.parse("2021-12-15", DATE_FORMATT)).build(),
                TimeTable.builder().id(4).startTime(LocalTime.parse("14:00:00", TIME_FORMATT))
                        .endTime(LocalTime.parse("15:30:00", TIME_FORMATT))
                        .date(LocalDate.parse("2021-12-15", DATE_FORMATT)).build(),
                TimeTable.builder().id(5).startTime(LocalTime.parse("16:00:00", TIME_FORMATT))
                        .endTime(LocalTime.parse("17:30:00", TIME_FORMATT))
                        .date(LocalDate.parse("2021-12-15", DATE_FORMATT)).build(),
                TimeTable.builder().id(6).startTime(LocalTime.parse("18:00:00", TIME_FORMATT))
                        .endTime(LocalTime.parse("19:30:00", TIME_FORMATT))
                        .date(LocalDate.parse("2021-12-15", DATE_FORMATT)).build());
    }
}
