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

import ua.com.foxminded.university.controller.web.rest.dto.input.CourseDtoIn;
import ua.com.foxminded.university.controller.web.rest.dto.output.CourseDtoOut;

public class CoursesRestControllerTest extends AbstractRestControllerTest {
    
    @Test
    public void checkMethodGetAllShouldShowAllCourses() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/rest/course/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<CourseDtoOut> coursesDtoOutList = mapper.readerForListOf(CourseDtoOut.class).readValue(response);
        assertEquals(coursesDtoOutList.size(), 43);
        assertEquals(coursesDtoOutList.get(2).getId(), 3);
        assertEquals(coursesDtoOutList.get(2).getName(), "THIRD");
        assertEquals(coursesDtoOutList.get(3).getId(), 4);
        assertEquals(coursesDtoOutList.get(3).getName(), "FOURTH");
        assertEquals(coursesDtoOutList.get(4).getId(), 5);
        assertEquals(coursesDtoOutList.get(4).getName(), "FIFTH");
    }
    
    @Test
    public void checkMethodGetShouldShowNecessaryCourse() throws Exception {
        String response =  mockMvc.perform(MockMvcRequestBuilders.get("/rest/course/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        CourseDtoOut courseDtoOut = mapper.readValue(response, CourseDtoOut.class);
        assertEquals(courseDtoOut.getId(), 1);
        assertEquals(courseDtoOut.getName(), "TENTH");
    }
    
    @Test
    public void checkMethodCreateShouldCreateCourse() throws Exception {
        CourseDtoIn courseDtoIn = CourseDtoIn.builder().name("SEVENTH").build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(courseDtoIn);  
        mockMvc.perform(MockMvcRequestBuilders.post("/rest/course").content(requestJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/rest/course/37")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        CourseDtoOut courseDtoOut = mapper.readValue(response, CourseDtoOut.class);
        assertEquals(courseDtoOut.getId(), 37);
        assertEquals(courseDtoOut.getName(), "SEVENTH");
    }
    
    @Test
    public void checkMethodUpdateShouldUpdateNecessaryCourse() throws Exception {
        CourseDtoIn courseDtoIn = CourseDtoIn.builder().name("TENTH").build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(courseDtoIn);
        String response = mockMvc.perform(MockMvcRequestBuilders.put("/rest/course/1").content(requestJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        CourseDtoOut courseDtoOut = mapper.readValue(response, CourseDtoOut.class);
        assertEquals(courseDtoOut.getId(), 1);
        assertEquals(courseDtoOut.getName(), "TENTH");
    }
    
    @Test
    public void checkMethodDeleteShouldDeleteNecessaryCourse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/rest/course/10")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/rest/course/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<CourseDtoOut> coursesDtoOutList = mapper.readerForListOf(CourseDtoOut.class).readValue(response);
        assertEquals(coursesDtoOutList.size(), 54);
        assertFalse(coursesDtoOutList.contains(CourseDtoOut.builder().id(10).build()));
    }
    
    @Test
    public void checkMethodGetShouldThrowExceptionNecessaryCourse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/rest/course/111")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isNotFound());
    }
    
    @Test
    public void checkMethodCreateShouldThrowException() throws Exception {
        CourseDtoIn courseDtoIn = CourseDtoIn.builder().name("SE").build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(courseDtoIn);
        mockMvc.perform(MockMvcRequestBuilders.post("/rest/course").content(requestJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status()
                .isBadRequest());
    }
    
    @Test
    public void checkMethodUpdateFormShouldThrowExceptionNecessaryCourse() throws Exception {
        CourseDtoIn courseDtoIn = CourseDtoIn.builder().name("SE").build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(courseDtoIn);
        mockMvc.perform(MockMvcRequestBuilders.get("/rest/course/111").content(requestJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isNotFound());
    }
    
    @Test
    public void checkMethodUpdateShouldThrowExceptionNecessaryCourse() throws Exception {
        CourseDtoIn courseDtoIn = CourseDtoIn.builder().name("SE").build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(courseDtoIn);
        mockMvc.perform(MockMvcRequestBuilders.put("/rest/course/1").content(requestJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status()
                .isBadRequest());
    }
    
    @Test
    public void checkMethodDeleteShouldThrowExceptionNecessaryCourse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/rest/course/111")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isNotFound());
    }  
}
