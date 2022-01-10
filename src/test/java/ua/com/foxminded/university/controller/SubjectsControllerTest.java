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

import ua.com.foxminded.university.model.Subject;
import ua.com.foxminded.university.service.GroupService;

public class SubjectsControllerTest extends AbstractControllerTest {
    @Mock
    GroupService groupServiceMock;
    @InjectMocks
    SubjectsController subjectsController;
    
    @Test
    public void checkMethodGetAllShouldShowAllSubjects() throws Exception {
        Mockito.when(groupServiceMock.getAllSubjects()).thenReturn(createFakeSubjectsList());
        groupServiceMock.getAllSubjects();
        mockMvc.perform(get("/subjects")).andDo(print())
        .andExpect(status().isOk())
                .andExpect(view().name("subjects/index"))
                .andExpect(model().attributeExists("subject"))
                .andExpect(model().attribute("subject", hasSize(40)))
                .andExpect(model().attribute("subject", hasItem(allOf(hasProperty("id", is(1)), hasProperty("name", is("Math")), 
                        hasProperty("description", is("Math includes the study of such topics as number theory, algebra, geometry and mathematical analysis"))))))
                .andExpect(model().attribute("subject", hasItem(allOf(hasProperty("id", is(2)), hasProperty("name", is("Physics")), 
                        hasProperty("description", is("Physics is the natural science that studies matter, its motion and behavior through space and time, and the related entities of energy and force."))))))
                .andExpect(model().attribute("subject", hasItem(allOf(hasProperty("id", is(3)), hasProperty("name", is("Chemistry")), 
                        hasProperty("description", is("Chemistry is the scientific discipline involved with elements and compounds composed of atoms, molecules and ions: their composition, structure, properties, behavior and the changes they undergo during a reaction with other substances."))))))
                .andExpect(model().attribute("subject", hasItem(allOf(hasProperty("id", is(4)), hasProperty("name", is("Astronomy")), 
                        hasProperty("description", is("Astronomy is a natural science that studies celestial objects and phenomena."))))))
                .andExpect(model().attribute("subject", hasItem(allOf(hasProperty("id", is(5)), hasProperty("name", is("Biology")), 
                        hasProperty("description", is("Biology is the natural science that studies life and living organisms, including their physical structure, chemical processes, molecular interactions, physiological mechanisms, development and evolution."))))));
        Mockito.verify(groupServiceMock, times(1)).getAllSubjects();
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }
    
    @Test
    public void checkMethodGetShouldShowNecessarySubject() throws Exception {
        Mockito.when(groupServiceMock.getSubject(1)).thenReturn(createFakeSubject());
        groupServiceMock.getSubject(1);
        mockMvc.perform(get("/subjects/show/1")).andDo(print())
        .andExpect(status().isOk())
                .andExpect(view().name("subjects/show"))
                .andExpect(model().attributeExists("subject"))
                .andExpect(model().attribute("subject", hasProperty("id", is(1))))
                .andExpect(model().attribute("subject", hasProperty("name", is("Math"))))
                .andExpect(model().attribute("subject", hasProperty("description", is("Math includes the study"
                        + " of such topics as number theory, algebra, geometry and mathematical analysis"))));
        Mockito.verify(groupServiceMock, times(1)).getSubject(1);
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }
    
    @Test
    public void checkMethodCreateFormShouldOpenFormForCreateSubject() throws Exception {
        mockMvc.perform(get("/subjects/new")).andDo(print())
        .andExpect(status().isOk())
                .andExpect(view().name("subjects/new"));
    }
    
    @Test
    public void checkMethodCreateShouldCreateSubject() throws Exception {
        groupServiceMock.saveSubject(Arrays.asList(createFakeSubject()));
        mockMvc.perform(post("/subjects")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Math")
                .param("description", "Math includes the study of such topics as number theory, algebra, geometry and mathematical analysis")
                .sessionAttr("subject", new Subject()))
                .andExpect(view().name("redirect:/subjects"));              
        ArgumentCaptor<Subject> argumentCaptor = ArgumentCaptor.forClass(Subject.class);
        Mockito.verify(groupServiceMock, times(1)).saveSubject(Arrays.asList(argumentCaptor.capture()));
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }
    
    @Test
    public void checkMethodUpdateFormShouldOpenNecessarySubject() throws Exception {
        Mockito.when(groupServiceMock.getSubject(1)).thenReturn(createFakeSubject());
        groupServiceMock.getSubject(1);
        mockMvc.perform(get("/subjects/edit/1")).andDo(print())
        .andExpect(status().isOk())
                .andExpect(view().name("subjects/edit")).andExpect(model().attributeExists("subjectDtoIn"))
                .andExpect(model().attribute("subjectDtoIn", hasProperty("id", is(1))))
                .andExpect(model().attribute("subjectDtoIn", hasProperty("name", is("Math"))))
                .andExpect(model().attribute("subjectDtoIn", hasProperty("description", is("Math includes the study "
                        + "of such topics as number theory, algebra, geometry and mathematical analysis"))));
        Mockito.verify(groupServiceMock, times(1)).getSubject(1);
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }
     
    @Test
    public void checkMethodUpdateShouldUpdateNecessarySubject() throws Exception {
        groupServiceMock.updateSubject(createFakeSubject());
        mockMvc.perform(post("/subjects/edit/1?")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)               
                .param("name", "Math")
                .param("description", "Math includes the study of such topics as number theory, algebra, geometry and mathematical analysis"))
        .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/subjects"));
        ArgumentCaptor<Subject> argumentCaptor = ArgumentCaptor.forClass(Subject.class);
        Mockito.verify(groupServiceMock, times(1)).updateSubject(argumentCaptor.capture());
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }
    
    @Test
    public void checkMethodDeleteShouldDeleteNecessarySubject() throws Exception {
        mockMvc.perform(get("/subjects/delete/1")).andDo(print())
        .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/subjects"));
    }
    
    @Test
    public void checkMethodGetShouldThrowExceptionNecessarySubject() throws Exception {
        mockMvc.perform(get("/subjects/show/111")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("subjects/error"));
    }
    
    @Test
    public void checkMethodCreateShouldThrowException() throws Exception {
        mockMvc.perform(post("/subjects?name")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("subjects/new"));
    }
    
    @Test
    public void checkMethodUpdateFormShouldThrowExceptionNecessarySubject() throws Exception {
        mockMvc.perform(get("/subjects/edit/111")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("subjects/error"));
    }
    
    @Test
    public void checkMethodUpdateShouldThrowExceptionNecessarySubject() throws Exception {
        mockMvc.perform(post("/subjects/edit/11?name=Java")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("subjects/edit"));
    }
    
    @Test
    public void checkMethodDeleteShouldThrowExceptionNecessarySubject() throws Exception {
        mockMvc.perform(get("/subjects/delete/111")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("subjects/error"));
    }
    
    private Subject createFakeSubject() {
        return Subject.builder().id(1).name("Math").description(
                "Math includes the study of such topics as number theory, algebra, geometry and mathematical analysis")
                .build();
    }
    
    private List<Subject> createFakeSubjectsList() {
        return Arrays.asList(
                Subject.builder().id(1).name("Math").description("Math includes the study of such topics as "
                        + "number theory, algebra, geometry and mathematical analysis").build(),
                Subject.builder().id(2).name("Physics").description("Physics is the natural science that studies "
                        + "matter, its motion and behavior through space and time, and the related entities "
                        + "of energy and force").build(),
                Subject.builder().id(3).name("Chemistry").description("Chemistry is the scientific discipline "
                        + "involved with elements and compounds composed of atoms, molecules and ions: their "
                        + "composition, structure, properties, behavior and the changes they undergo during a "
                        + "reaction with other substances").build());
    }
}
