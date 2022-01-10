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

import ua.com.foxminded.university.model.Professor;
import ua.com.foxminded.university.service.GroupService;

public class ProfessorsControllerTest extends AbstractControllerTest {
    @Mock
    GroupService  groupServiceMock;
    @InjectMocks
    ProfessorsController professorsController;
    
    @Test
    public void checkMethodGetAllShouldShowAllProfessors() throws Exception {
        Mockito.when(groupServiceMock.getAllProfessors()).thenReturn(createFakeProfessorsList());
        groupServiceMock.getAllProfessors();
        mockMvc.perform(get("/professors")).andDo(print())
        .andExpect(status().isOk())
                .andExpect(view().name("professors/index"))
                .andExpect(model().attributeExists("professor"))
                .andExpect(model().attribute("professor", hasSize(19)))
                .andExpect(model().attribute("professor", hasItem(allOf(hasProperty("id", is(1)), 
                        hasProperty("firstName", is("Viktor")), hasProperty("lastName", is("Rebrov"))))))
                .andExpect(model().attribute("professor", hasItem(allOf(hasProperty("id", is(2)), 
                        hasProperty("firstName", is("Denys")), hasProperty("lastName", is("Rebrov"))))))
                .andExpect(model().attribute("professor", hasItem(allOf(hasProperty("id", is(3)), 
                        hasProperty("firstName", is("Olena")), hasProperty("lastName", is("Rebrov"))))))
                .andExpect(model().attribute("professor", hasItem(allOf(hasProperty("id", is(4)), 
                        hasProperty("firstName", is("Darya")), hasProperty("lastName", is("Rebrov"))))))
                .andExpect(model().attribute("professor", hasItem(allOf(hasProperty("id", is(5)), 
                        hasProperty("firstName", is("Mykola")), hasProperty("lastName", is("Rebrov"))))));
        Mockito.verify(groupServiceMock, times(1)).getAllProfessors();
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }
    
    @Test
    public void checkMethodGetShouldShowNecessaryProfessor() throws Exception {
        Mockito.when(groupServiceMock.getProfessor(1)).thenReturn(createFakeProfessor());
        groupServiceMock.getProfessor(1);
        mockMvc.perform(get("/professors/show/1")).andDo(print())
        .andExpect(status().isOk())
                .andExpect(view().name("professors/show"))
                .andExpect(model().attributeExists("professor"))
                .andExpect(model().attribute("professor", hasProperty("id", is(1))))
                .andExpect(model().attribute("professor", hasProperty("firstName", is("Viktor"))))
                .andExpect(model().attribute("professor", hasProperty("lastName", is("Rebrov"))));
        Mockito.verify(groupServiceMock, times(1)).getProfessor(1);
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }
    
    @Test
    public void checkMethodCreateFormShouldOpenFormForCreateProfessor() throws Exception {
        mockMvc.perform(get("/professors/new")).andDo(print())
        .andExpect(status().isOk())
                .andExpect(view().name("professors/new"));
    }
    
    @Test
    public void checkMethodCreateShouldCreateProfessor() throws Exception {
        groupServiceMock.saveProfessor(Arrays.asList(createFakeProfessor()));
        mockMvc.perform(post("/professors")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("firstName", "Yaroslav")
                .param("lastName", "Voronovskyi")
                .sessionAttr("professor", new Professor()))
                .andExpect(view().name("redirect:/professors"));              
        ArgumentCaptor<Professor> argumentCaptor = ArgumentCaptor.forClass(Professor.class);
        Mockito.verify(groupServiceMock, times(1)).saveProfessor(Arrays.asList(argumentCaptor.capture()));
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }
    
    @Test
    public void checkMethodUpdateFormShouldOpenNecessaryProfessor() throws Exception {
        Mockito.when(groupServiceMock.getProfessor(1)).thenReturn(createFakeProfessor());
        groupServiceMock.getProfessor(1);
        mockMvc.perform(get("/professors/edit/1")).andDo(print())
        .andExpect(status().isOk())
                .andExpect(view().name("professors/edit")).andExpect(model().attributeExists("professorDtoIn"))
                .andExpect(model().attribute("professorDtoIn", hasProperty("id", is(1))))
                .andExpect(model().attribute("professorDtoIn", hasProperty("firstName", is("Viktor"))))
                .andExpect(model().attribute("professorDtoIn", hasProperty("lastName", is("Rebrov"))));
        Mockito.verify(groupServiceMock, times(1)).getProfessor(1);
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }
    
    @Test
    public void checkMethodUpdateShouldUpdateNecessaryProfessor() throws Exception {
        groupServiceMock.updateProfessor(createFakeProfessor());
        mockMvc.perform(post("/professors/edit/1?Yaroslav&Voronovskyi")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)               
                .param("firstName", "firstName")
                .param("lastName", "lastName"))
        .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/professors"));
        ArgumentCaptor<Professor> argumentCaptor = ArgumentCaptor.forClass(Professor.class);
        Mockito.verify(groupServiceMock, times(1)).updateProfessor(argumentCaptor.capture());
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }
    
    @Test
    public void checkMethodDeleteShouldDeleteNecessaryProfessor() throws Exception {
        mockMvc.perform(get("/professors/delete/6")).andDo(print())
        .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/professors"));
    }
    
    @Test
    public void checkMethodGetShouldThrowExceptionNecessaryProfessor() throws Exception {
        mockMvc.perform(get("/professors/show/111")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("professors/error"));
    }
    
    @Test
    public void checkMethodCreateShouldThrowException() throws Exception {
        mockMvc.perform(post("/professors?firstName")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("professors/new"));
    }
    
    @Test
    public void checkMethodUpdateFormShouldThrowExceptionNecessaryProfessor() throws Exception {
        mockMvc.perform(get("/professors/edit/111")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("professors/error"));
    }
    
    @Test
    public void checkMethodUpdateShouldThrowExceptionNecessaryProfessor() throws Exception {
        mockMvc.perform(post("/professors/edit/11?firstName=Yaroslav")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("professors/edit"));
    }
    
    @Test
    public void checkMethodDeleteShouldThrowExceptionNecessaryProfessor() throws Exception {
        mockMvc.perform(get("/professors/delete/111")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("professors/error"));
    }
    
    private Professor createFakeProfessor() {
        return Professor.builder().id(1).firstName("Viktor").lastName("Rebrov").build();
    }
    
    private List<Professor> createFakeProfessorsList() {
        return Arrays.asList(Professor.builder().id(1).firstName("Viktor").lastName("Rebrov").build(), 
                Professor.builder().id(2).firstName("Denys").lastName("Rebrov").build(),
                Professor.builder().id(3).firstName("Olena").lastName("Rebrov").build(),
                Professor.builder().id(4).firstName("Darya").lastName("Rebrov").build(),
                Professor.builder().id(5).firstName("Mykola").lastName("Rebrov").build());
    }
}
