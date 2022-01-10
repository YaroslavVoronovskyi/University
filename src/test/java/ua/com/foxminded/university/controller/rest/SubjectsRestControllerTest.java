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

import ua.com.foxminded.university.controller.web.rest.dto.input.SubjectDtoIn;
import ua.com.foxminded.university.controller.web.rest.dto.output.SubjectDtoOut;

public class SubjectsRestControllerTest extends AbstractRestControllerTest {
    
    @Test
    public void checkMethodGetAllShouldShowAllSubjects() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/rest/subject/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<SubjectDtoOut> subjectsDtoOutList = mapper.readerForListOf(SubjectDtoOut.class).readValue(response);
        assertEquals(subjectsDtoOutList.size(), 30);
        assertEquals(subjectsDtoOutList.get(2).getId(), 3);
        assertEquals(subjectsDtoOutList.get(2).getName(), "Chemistry");
        assertEquals(subjectsDtoOutList.get(2).getDescription(), "Chemistry is the scientific discipline involved with elements and compounds composed of atoms, molecules and ions: their composition, structure, properties, behavior and the changes they undergo during a reaction with other substances.");
        assertEquals(subjectsDtoOutList.get(3).getId(), 4);
        assertEquals(subjectsDtoOutList.get(3).getName(), "Astronomy");
        assertEquals(subjectsDtoOutList.get(3).getDescription(), "Astronomy is a natural science that studies celestial objects and phenomena.");
        assertEquals(subjectsDtoOutList.get(4).getId(), 5);
        assertEquals(subjectsDtoOutList.get(4).getName(), "Biology");
        assertEquals(subjectsDtoOutList.get(4).getDescription(), "Biology is the natural science that studies life and living organisms, including their physical structure, chemical processes, molecular interactions, physiological mechanisms, development and evolution.");
    }
    
    @Test
    public void checkMethodGetShouldShowNecessarySubject() throws Exception {
        String response =  mockMvc.perform(MockMvcRequestBuilders.get("/rest/subject/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        SubjectDtoOut subjectDtoOut = mapper.readValue(response, SubjectDtoOut.class);
        assertEquals(subjectDtoOut.getId(), 1);
        assertEquals(subjectDtoOut.getName(), "Math");
        assertEquals(subjectDtoOut.getDescription(), "Math includes the study of such topics as number theory, algebra, geometry and mathematical analysis");
    }
    
    @Test
    public void checkMethodCreateShouldCreateSubject() throws Exception {
        SubjectDtoIn subjectDtoIn = SubjectDtoIn.builder().name("Java").description("A programming language").build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(subjectDtoIn);  
        mockMvc.perform(MockMvcRequestBuilders.post("/rest/subject").content(requestJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/rest/subject/91")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        SubjectDtoOut subjectDtoOut = mapper.readValue(response, SubjectDtoOut.class);
        assertEquals(subjectDtoOut.getId(), 91);
        assertEquals(subjectDtoOut.getName(), "Java");
        assertEquals(subjectDtoOut.getDescription(), "A programming language");
    }
    
    @Test
    public void checkMethodUpdateShouldUpdateNecessarySubject() throws Exception {
        SubjectDtoIn subjectDtoIn = SubjectDtoIn.builder().name("Java").description("A programming language").build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(subjectDtoIn);  
        String response = mockMvc.perform(MockMvcRequestBuilders.put("/rest/subject/1").content(requestJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        SubjectDtoOut subjectDtoOut = mapper.readValue(response, SubjectDtoOut.class);
        assertEquals(subjectDtoOut.getId(), 1);
        assertEquals(subjectDtoOut.getName(), "Java");
        assertEquals(subjectDtoOut.getDescription(), "A programming language");
    }
    
    @Test
    public void checkMethodDeleteShouldDeleteNecessarySubject() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/rest/subject/10")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/rest/subject/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<SubjectDtoOut> subjectsDtoOutList = mapper.readerForListOf(SubjectDtoOut.class).readValue(response);
        assertEquals(subjectsDtoOutList.size(), 100);
        assertFalse(subjectsDtoOutList.contains(SubjectDtoOut.builder().id(10).build()));
    }
    
    @Test
    public void checkMethodGetShouldThrowExceptionNecessarySubject() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/rest/subject/111")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void checkMethodCreateShouldThrowException() throws Exception {
        SubjectDtoIn subjectDtoIn = SubjectDtoIn.builder().name("Jav").description("A").build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(subjectDtoIn);  
        mockMvc.perform(MockMvcRequestBuilders.post("/rest/subject?name=AAA").content(requestJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
    }
    
    @Test
    public void checkMethodUpdateFormShouldThrowExceptionNecessarySubject() throws Exception {
        SubjectDtoIn subjectDtoIn = SubjectDtoIn.builder().name("Java").description("A programming language").build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(subjectDtoIn);  
        mockMvc.perform(MockMvcRequestBuilders.get("/rest/auditory/111").content(requestJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void checkMethodUpdateShouldThrowExceptionNecessarySubject() throws Exception {
        SubjectDtoIn subjectDtoIn = SubjectDtoIn.builder().name("Jav").description("A").build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(subjectDtoIn);  
        mockMvc.perform(MockMvcRequestBuilders.put("/rest/subject/1").content(requestJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
    }
    
    @Test
    public void checkMethodDeleteShouldThrowExceptionNecessarySubject() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/rest/subject/111")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }  
}
