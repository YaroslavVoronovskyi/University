package ua.com.foxminded.university.controller;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.MediaType;

import ua.com.foxminded.university.model.Course;
import ua.com.foxminded.university.service.CourseService;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

public class CoursesControllerTest extends AbstractControllerTest {
    @Mock
    private CourseService courseServiceMock;  
    @InjectMocks
    private CoursesController coursesController;
    
    @Test
    public void checkMethodGetAllShouldShowAllCourses() throws Exception {
        Mockito.when(courseServiceMock.getAllCourses()).thenReturn(createFakeCoursesList());
        courseServiceMock.getAllCourses();
        mockMvc.perform(get("/courses")).andDo(print())
        .andExpect(status().isOk())
                .andExpect(view().name("courses/index"))
                .andExpect(model().attributeExists("course"))
                .andExpect(model().attribute("course", hasSize(49)))
                .andExpect(model().attribute("course", hasItem(allOf(hasProperty("id", is(1)), hasProperty("name", is("FIRST"))))))
                .andExpect(model().attribute("course", hasItem(allOf(hasProperty("id", is(2)), hasProperty("name", is("SECOND"))))))
                .andExpect(model().attribute("course", hasItem(allOf(hasProperty("id", is(3)), hasProperty("name", is("THIRD"))))))
                .andExpect(model().attribute("course", hasItem(allOf(hasProperty("id", is(4)), hasProperty("name", is("FOURTH"))))))
                .andExpect(model().attribute("course", hasItem(allOf(hasProperty("id", is(5)), hasProperty("name", is("FIFTH"))))))
                .andExpect(model().attribute("course", hasItem(allOf(hasProperty("id", is(6)), hasProperty("name", is("SIXTH"))))));
        Mockito.verify(courseServiceMock, times(1)).getAllCourses();
        Mockito.verifyNoMoreInteractions(courseServiceMock);
    }
    
    @Test
    public void checkMethodGetShouldShowNecessaryCourse() throws Exception {
        Mockito.when(courseServiceMock.getCourse(1)).thenReturn(createFakeCourse());
        courseServiceMock.getCourse(1);
        mockMvc.perform(get("/courses/show/1")).andDo(print())
        .andExpect(status().isOk())
                .andExpect(view().name("courses/show"))
                .andExpect(model().attributeExists("course"))
                .andExpect(model().attribute("course", hasProperty("id", is(1))))
                .andExpect(model().attribute("course", hasProperty("name", is("FIRST"))));
        Mockito.verify(courseServiceMock, times(1)).getCourse(1);
        Mockito.verifyNoMoreInteractions(courseServiceMock);
    }
    
    @Test
    public void checkMethodGetShouldThrowResponseStatusExceptionNecessaryCourse() throws Exception {
        mockMvc.perform(get("/courses/show/111")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("courses/error"));
    }
    
    @Test
    public void checkMethodCreateFormShouldOpenFormForCreateCourse() throws Exception {
        mockMvc.perform(get("/courses/new")).andDo(print())
        .andExpect(status().isOk())
                .andExpect(view().name("courses/new"));
    }
    
    @Test
    public void checkMethodCreateShouldCreateCourse() throws Exception {
        courseServiceMock.saveCourse(Arrays.asList(createFakeCourse()));
        mockMvc.perform(post("/courses")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "name")
                .sessionAttr("course", new Course()))
                .andExpect(view().name("redirect:/courses"));              
        ArgumentCaptor<Course> argumentCaptor = ArgumentCaptor.forClass(Course.class);
        Mockito.verify(courseServiceMock, times(1)).saveCourse(Arrays.asList(argumentCaptor.capture()));
        Mockito.verifyNoMoreInteractions(courseServiceMock);
    }
    
    @Test
    public void checkMethodCreateShouldThrowResponseStatusException() throws Exception {
        mockMvc.perform(post("/courses?name")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("courses/new"));
    }
    
    @Test
    public void checkMethodUpdateFormShouldOpenNecessaryCourse() throws Exception {
        Mockito.when(courseServiceMock.getCourse(1)).thenReturn(createFakeCourse());
        courseServiceMock.getCourse(1);
        mockMvc.perform(get("/courses/edit/1")).andDo(print())
        .andExpect(status().isOk())
                .andExpect(view().name("courses/edit")).andExpect(model().attributeExists("courseDtoIn"))
                .andExpect(model().attribute("courseDtoIn", hasProperty("id", is(1))))
                .andExpect(model().attribute("courseDtoIn", hasProperty("name", is("FIRST"))));
        Mockito.verify(courseServiceMock, times(1)).getCourse(1);
        Mockito.verifyNoMoreInteractions(courseServiceMock);
    }
 
    @Test
    public void checkMethodUpdateFormShouldThrowResponseStatusExceptionNecessaryCourse() throws Exception {
        mockMvc.perform(get("/courses/edit/11")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("courses/edit"));
    }
    
    @Test
    public void checkMethodUpdateShouldUpdateNecessaryCourse() throws Exception {
        courseServiceMock.updateCourse(createFakeCourse());
        mockMvc.perform(post("/courses/edit/1?")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "FIRST"))
        .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/courses"));
        ArgumentCaptor<Course> argumentCaptor = ArgumentCaptor.forClass(Course.class);
        Mockito.verify(courseServiceMock, times(1)).updateCourse(argumentCaptor.capture());
        Mockito.verifyNoMoreInteractions(courseServiceMock);
    }
    
    @Test
    public void checkMethodUpdateShouldThrowResponseStatusExceptionNecessaryCourse() throws Exception {
        mockMvc.perform(post("/courses/edit/111?name=")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("courses/edit"));
    }
    
    @Test
    public void checkMethodDeleteShouldDeleteNecessaryCourse() throws Exception {
        mockMvc.perform(get("/courses/delete/1")).andDo(print())
        .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/courses"));
    }
    
    @Test
    public void checkMethodDeleteShouldThrowResponseStatusExceptionNecessaryCourse() throws Exception {
        mockMvc.perform(get("/courses/delete/111")).andDo(print()).andExpect(status().isOk())
                .andExpect(view().name("courses/error"));
    }
    
    private Course createFakeCourse() {
        return Course.builder().id(1).name("FIRST").build();
    }
    
    private List<Course> createFakeCoursesList() {
        return Arrays.asList(
                Course.builder().id(1).name("FIRST").build(),
                Course.builder().id(2).name("SECOND").build(),
                Course.builder().id(3).name("THIRD").build(),
                Course.builder().id(4).name("FOURTH").build(),
                Course.builder().id(5).name("FIFTH").build(),
                Course.builder().id(6).name("SIXTH").build());
    }
}
