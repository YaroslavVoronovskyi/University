package ua.com.foxminded.university.controller.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import ua.com.foxminded.university.controller.web.rest.dto.input.StudentDtoIn;
import ua.com.foxminded.university.controller.web.rest.dto.output.StudentDtoOut;

public class StudentsRestControllerTest extends AbstractRestControllerTest {
 
    @Test
    public void checkMethodGetAllShouldShowAllStudents() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/rest/student/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<StudentDtoOut> studentsDtoOutList = mapper.readerForListOf(StudentDtoOut.class).readValue(response);
        assertEquals(studentsDtoOutList.size(), 20);
        assertEquals(studentsDtoOutList.get(2).getId(), 3);
        assertEquals(studentsDtoOutList.get(2).getFirstName(), "Olena");
        assertEquals(studentsDtoOutList.get(2).getLastName(), "Tsygankov");
        assertEquals(studentsDtoOutList.get(3).getId(), 4);
        assertEquals(studentsDtoOutList.get(3).getFirstName(), "Darya");
        assertEquals(studentsDtoOutList.get(3).getLastName(), "Tsygankov");
        assertEquals(studentsDtoOutList.get(4).getId(), 5);
        assertEquals(studentsDtoOutList.get(4).getFirstName(), "Mykola");
        assertEquals(studentsDtoOutList.get(4).getLastName(), "Tsygankov");
    }
    
    @Test
    public void checkMethodGetShouldShowNecessaryStudent() throws Exception {
        String response =  mockMvc.perform(MockMvcRequestBuilders.get("/rest/student/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        StudentDtoOut studentDtoOut = mapper.readValue(response, StudentDtoOut.class);
        assertEquals(studentDtoOut.getId(), 1);
        assertEquals(studentDtoOut.getFirstName(), "Viktor");
        assertEquals(studentDtoOut.getLastName(), "Tsygankov");
    }
    
    @Test
    public void checkMethodCreateShouldCreateStudent() throws Exception {
        StudentDtoIn studentDtoIn = StudentDtoIn.builder().firstName("Yaroslav").lastName("Voronovskyi").build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(studentDtoIn);  
        mockMvc.perform(MockMvcRequestBuilders.post("/rest/student").content(requestJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/rest/student/91")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        StudentDtoOut studentDtoOut = mapper.readValue(response, StudentDtoOut.class);
        assertEquals(studentDtoOut.getId(), 91);
        assertEquals(studentDtoOut.getFirstName(), "Yaroslav");
        assertEquals(studentDtoOut.getLastName(), "Voronovskyi");
    }
    
    @Test
    public void checkMethodUpdateShouldUpdateNecessaryStudent() throws Exception {
        StudentDtoIn studentDtoIn = StudentDtoIn.builder().firstName("Yaroslav").lastName("Voronovskyi").build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(studentDtoIn);
        String response = mockMvc.perform(MockMvcRequestBuilders.put("/rest/student/1").content(requestJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        StudentDtoOut studentDtoOut = mapper.readValue(response, StudentDtoOut.class);
        assertEquals(studentDtoOut.getId(), 1);
        assertEquals(studentDtoOut.getFirstName(), "Yaroslav");
        assertEquals(studentDtoOut.getLastName(), "Voronovskyi");
    }
    
    @Test
    public void checkMethodDeleteShouldDeleteNecessaryStudent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/rest/student/10")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/rest/student/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<StudentDtoOut> studentsDtoOutList = mapper.readerForListOf(StudentDtoOut.class).readValue(response);
        assertEquals(studentsDtoOutList.size(), 100);
        assertFalse(studentsDtoOutList.contains(StudentDtoOut.builder().id(10).build()));
    }
    
    @Test
    public void checkMethodGetShouldThrowExceptionNecessaryStudent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/rest/student/111")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void checkMethodCreateShouldThrowException() throws Exception {
        StudentDtoIn studentDtoIn = StudentDtoIn.builder().firstName("Yar").lastName("Vo").build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(studentDtoIn);  
        mockMvc.perform(MockMvcRequestBuilders.post("/rest/student").content(requestJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
    }
    
    @Test
    public void checkMethodUpdateFormShouldThrowExceptionNecessaryStudent() throws Exception {
        StudentDtoIn studentDtoIn = StudentDtoIn.builder().firstName("Yaroslav").lastName("Voronovskyi").build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(studentDtoIn);
        mockMvc.perform(MockMvcRequestBuilders.get("/rest/student/111").content(requestJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isNotFound());
    }
    
    @Test
    public void checkMethodUpdateShouldThrowExceptionNecessaryStudent() throws Exception {
        StudentDtoIn studentDtoIn = StudentDtoIn.builder().firstName("Ya").lastName("Vo").build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(studentDtoIn);
        mockMvc.perform(MockMvcRequestBuilders.put("/rest/student/1").content(requestJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
    }
    
    @Test
    public void checkMethodDeleteShouldThrowExceptionNecessaryStudent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/rest/student/111")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    } 
}
