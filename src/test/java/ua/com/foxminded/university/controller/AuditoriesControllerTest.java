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

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.MediaType;

import ua.com.foxminded.university.model.Auditory;
import ua.com.foxminded.university.service.TimeTableService;

public class AuditoriesControllerTest extends AbstractControllerTest {
    @Mock
    TimeTableService timeTableServiceMock;    
    @InjectMocks
    AuditoriesController auditoriesController;
    
    @Test
    public void checkMethodGetAllShouldShowAllAuditories() throws Exception {
        Mockito.when(timeTableServiceMock.getAllAuditories()).thenReturn(createFakeAuditoriesList());
        timeTableServiceMock.getAllAuditories();
        mockMvc.perform(get("/auditories")).andDo(print())
        .andExpect(status().isOk())
                .andExpect(view().name("auditories/index"))
                .andExpect(model().attributeExists("auditory"))
                .andExpect(model().attribute("auditory", hasSize(55)))   
                .andExpect(model().attribute("auditory", hasItem(allOf(hasProperty("id", is(1)), hasProperty("capacity", is(40))))))
                .andExpect(model().attribute("auditory", hasItem(allOf(hasProperty("id", is(2)), hasProperty("capacity", is(45))))))
                .andExpect(model().attribute("auditory", hasItem(allOf(hasProperty("id", is(3)), hasProperty("capacity", is(20))))))
                .andExpect(model().attribute("auditory", hasItem(allOf(hasProperty("id", is(4)), hasProperty("capacity", is(25))))))
                .andExpect(model().attribute("auditory", hasItem(allOf(hasProperty("id", is(5)), hasProperty("capacity", is(30))))));
        Mockito.verify(timeTableServiceMock, times(1)).getAllAuditories();
        Mockito.verifyNoMoreInteractions(timeTableServiceMock);
    }
    
    @Test
    public void checkMethodGetShouldShowNecessaryAuditory() throws Exception {
        Mockito.when(timeTableServiceMock.getAuditory(1)).thenReturn(createFakeAuditory());
        timeTableServiceMock.getAuditory(1);
        mockMvc.perform(get("/auditories/show/1")).andDo(print())
        .andExpect(status().isOk())
                .andExpect(view().name("auditories/show"))
                .andExpect(model().attributeExists("auditory"))
                .andExpect(model().attribute("auditory", hasProperty("id", is(1))))
                .andExpect(model().attribute("auditory", hasProperty("capacity", is(40))));
        Mockito.verify(timeTableServiceMock, times(1)).getAuditory(1);
        Mockito.verifyNoMoreInteractions(timeTableServiceMock);
    }
    
    @Test
    public void checkMethodCreateFormShouldOpenFormForCreateAuditory() throws Exception {
        mockMvc.perform(get("/auditories/new")).andDo(print())
        .andExpect(status().isOk())
                .andExpect(view().name("auditories/new"));
    }
    
    @Test
    public void checkMethodCreateShouldCreateAuditory() throws Exception {
        timeTableServiceMock.saveAuditory(Arrays.asList(createFakeAuditory()));
        mockMvc.perform(post("/auditories")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("capacity", "40")
                .sessionAttr("auditory", new Auditory()))
                .andExpect(status()
                        .is3xxRedirection())
                .andExpect(view().name("redirect:/auditories"));
        ArgumentCaptor<Auditory> argumentCaptor = ArgumentCaptor.forClass(Auditory.class);
        Mockito.verify(timeTableServiceMock, times(1)).saveAuditory(Arrays.asList(argumentCaptor.capture()));
        Mockito.verifyNoMoreInteractions(timeTableServiceMock);
    }
    
    @Test
    public void checkMethodUpdateFormShouldOpenNecessaryAuditory() throws Exception {
        Mockito.when(timeTableServiceMock.getAuditory(1)).thenReturn(createFakeAuditory());
        timeTableServiceMock.getAuditory(1);
        mockMvc.perform(get("/auditories/edit/1")).andDo(print())
        .andExpect(status().isOk())
                .andExpect(view().name("auditories/edit")).andExpect(model().attributeExists("auditoryDtoIn"))
                .andExpect(model().attribute("auditoryDtoIn", hasProperty("id", is(1))))
                .andExpect(model().attribute("auditoryDtoIn", hasProperty("capacity", is(40))));
        Mockito.verify(timeTableServiceMock, times(1)).getAuditory(1);
        Mockito.verifyNoMoreInteractions(timeTableServiceMock);
    }
    
    @Test
    public void checkMethodUpdateShouldUpdateNecessaryAuditory() throws Exception {
        timeTableServiceMock.updateAuditory(createFakeAuditory());
        mockMvc.perform(post("/auditories/edit/1?capacity=40")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)               
                .param("capacity", "40"))
        .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/auditories"));
        ArgumentCaptor<Auditory> argumentCaptor = ArgumentCaptor.forClass(Auditory.class);
        Mockito.verify(timeTableServiceMock, times(1)).updateAuditory(argumentCaptor.capture());
        Mockito.verifyNoMoreInteractions(timeTableServiceMock);
    }
    
    @Test
    public void checkMethodDeleteShouldDeleteNecessaryAuditory() throws Exception {
        mockMvc.perform(get("/auditories/delete/11")).andDo(print())
        .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auditories"));
    }
    
    @Test
    public void checkMethodGetShouldThrowExceptionNecessaryAuditory() throws Exception {
        mockMvc.perform(get("/auditories/show/111")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("auditories/error"));
    }
    
    @Test
    public void checkMethodCreateShouldThrowException() throws Exception {
        mockMvc.perform(post("/auditories?100")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("auditories/new"));
    }
    
    @Test
    public void checkMethodUpdateFormShouldThrowExceptionNecessaryAuditory() throws Exception {
        mockMvc.perform(get("/auditories/edit/111")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("auditories/error"));
    }
    
    @Test
    public void checkMethodUpdateShouldThrowExceptionNecessaryAuditory() throws Exception {
        mockMvc.perform(post("/auditories/edit/11?capacity=100")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("auditories/edit"));
    }

    @Test
    public void checkMethodDeleteShouldThrowExceptionNecessaryAuditory() throws Exception {
        mockMvc.perform(get("/auditories/delete/111")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("auditories/error"));
    }
    
    private Auditory createFakeAuditory() {
        return Auditory.builder().id(1).capacity(40).build();
    }
    
    private List<Auditory> createFakeAuditoriesList() {
        return Arrays.asList(Auditory.builder().id(1).capacity(40).build(),
                Auditory.builder().id(2).capacity(45).build(),
                Auditory.builder().id(3).capacity(20).build(),
                Auditory.builder().id(4).capacity(25).build(),
                Auditory.builder().id(5).capacity(30).build());
    }
}
