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

import ua.com.foxminded.university.model.Student;
import ua.com.foxminded.university.service.GroupService;

public class StudentsControllerTest extends AbstractControllerTest {
    @Mock
    GroupService groupServiceMock;
    
    @InjectMocks
    StudentsController studentsController;
    
    @Test
    public void checkMethodGetAllShouldShowAllStudents() throws Exception {
        Mockito.when(groupServiceMock.getAllStudents()).thenReturn(createFakeStudentsList());
        groupServiceMock.getAllStudents();
        mockMvc.perform(get("/students")).andDo(print())
        .andExpect(status().isOk())
                .andExpect(view().name("students/index"))
                .andExpect(model().attributeExists("student"))
                .andExpect(model().attribute("student", hasSize(20)))
                .andExpect(model().attribute("student", hasItem(allOf(hasProperty("id", is(1)), 
                        hasProperty("firstName", is("Viktor")), hasProperty("lastName", is("Tsygankov"))))))
                .andExpect(model().attribute("student", hasItem(allOf(hasProperty("id", is(2)), 
                        hasProperty("firstName", is("Denys")), hasProperty("lastName", is("Tsygankov"))))))
                .andExpect(model().attribute("student", hasItem(allOf(hasProperty("id", is(3)), 
                        hasProperty("firstName", is("Olena")), hasProperty("lastName", is("Tsygankov"))))))
                .andExpect(model().attribute("student", hasItem(allOf(hasProperty("id", is(4)), 
                        hasProperty("firstName", is("Darya")), hasProperty("lastName", is("Tsygankov"))))))
                .andExpect(model().attribute("student", hasItem(allOf(hasProperty("id", is(5)), 
                        hasProperty("firstName", is("Mykola")), hasProperty("lastName", is("Tsygankov"))))));
        Mockito.verify(groupServiceMock, times(1)).getAllStudents();
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }
    
    @Test
    public void checkMethodGetShouldShowNecessaryStudent() throws Exception {
        Mockito.when(groupServiceMock.getStudent(1)).thenReturn(createFakeStudent());
        groupServiceMock.getStudent(1);
        mockMvc.perform(get("/students/show/1")).andDo(print())
        .andExpect(status().isOk())
                .andExpect(view().name("students/show"))
                .andExpect(model().attributeExists("student"))
                .andExpect(model().attribute("student", hasProperty("id", is(1))))
                .andExpect(model().attribute("student", hasProperty("firstName", is("Viktor"))))
                .andExpect(model().attribute("student", hasProperty("lastName", is("Tsygankov"))));
        Mockito.verify(groupServiceMock, times(1)).getStudent(1);
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }
    
    @Test
    public void checkMethodCreateFormShouldOpenFormForCreateStudent() throws Exception {
        mockMvc.perform(get("/students/new")).andDo(print())
        .andExpect(status().isOk())
                .andExpect(view().name("students/new"));
    }
    
    @Test
    public void checkMethodCreateShouldCreateStudent() throws Exception {
        groupServiceMock.saveStudent(Arrays.asList(createFakeStudent()));
        mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("firstName", "Viktor")
                .param("lastName", "Tsygankov")
                .sessionAttr("student", new Student()))
                .andExpect(view().name("redirect:/students"));              
        ArgumentCaptor<Student> argumentCaptor = ArgumentCaptor.forClass(Student.class);
        Mockito.verify(groupServiceMock, times(1)).saveStudent(Arrays.asList(argumentCaptor.capture()));
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }
    
    @Test
    public void checkMethodUpdateFormShouldOpenNecessaryStudent() throws Exception {
        Mockito.when(groupServiceMock.getStudent(1)).thenReturn(createFakeStudent());
        groupServiceMock.getStudent(1);
        mockMvc.perform(get("/students/edit/1")).andDo(print())
        .andExpect(status().isOk())
                .andExpect(view().name("students/edit")).andExpect(model().attributeExists("studentDtoIn"))
                .andExpect(model().attribute("studentDtoIn", hasProperty("id", is(1))))
                .andExpect(model().attribute("studentDtoIn", hasProperty("firstName", is("Viktor"))))
                .andExpect(model().attribute("studentDtoIn", hasProperty("lastName", is("Tsygankov"))));
        Mockito.verify(groupServiceMock, times(1)).getStudent(1);
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }
    
    @Test
    public void checkMethodUpdateShouldUpdateNecessaryStudent() throws Exception {
        groupServiceMock.updateStudent(createFakeStudent());
        mockMvc.perform(post("/students/edit/1?")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)               
                .param("firstName", "Viktor") 
                .param("lastName", "Tsygankov"))
        .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/students"));
        ArgumentCaptor<Student> argumentCaptor = ArgumentCaptor.forClass(Student.class);
        Mockito.verify(groupServiceMock, times(1)).updateStudent(argumentCaptor.capture());
        Mockito.verifyNoMoreInteractions(groupServiceMock);
    }
    
    @Test
    public void checkMethodDeleteShouldDeleteNecessaryStudent() throws Exception {
        mockMvc.perform(get("/students/delete/11")).andDo(print())
        .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students"));
    }
    
    @Test
    public void checkMethodGetShouldThrowExceptionNecessaryStudent() throws Exception {
        mockMvc.perform(get("/students/show/111")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("students/error"));
    }
    
    @Test
    public void checkMethodCreateShouldThrowException() throws Exception {
        mockMvc.perform(post("/students?firstName")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("students/new"));
    }
    
    @Test
    public void checkMethodUpdateFormShouldThrowExceptionNecessaryStudent() throws Exception {
        mockMvc.perform(get("/students/edit/11")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("students/edit"));
    }
    
    @Test
    public void checkMethodUpdateShouldThrowExceptionNecessaryStudent() throws Exception {
        mockMvc.perform(post("/students/edit/11?firstName=Yaroslav")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("students/edit"));
    }
    
    @Test
    public void checkMethodDeleteShouldThrowExceptionNecessaryStudent() throws Exception {
        mockMvc.perform(get("/students/delete/111")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("students/error"));
    }
    
    private Student createFakeStudent() {
        return Student.builder().id(1).firstName("Viktor").lastName("Tsygankov").build();
    }
    
    private List<Student> createFakeStudentsList() {
        return Arrays.asList(Student.builder().id(1).firstName("Viktor").lastName("Tsygankov").build(),
                Student.builder().id(2).firstName("Denys").lastName("Tsygankov").build(),
                Student.builder().id(3).firstName("Olena").lastName("Tsygankov").build(),
                Student.builder().id(4).firstName("Darya").lastName("Tsygankov").build(),
                Student.builder().id(5).firstName("Mykola").lastName("Tsygankov").build());
    }
}
