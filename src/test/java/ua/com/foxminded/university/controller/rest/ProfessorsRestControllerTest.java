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

import ua.com.foxminded.university.controller.web.rest.dto.input.ProfessorDtoIn;
import ua.com.foxminded.university.controller.web.rest.dto.output.ProfessorDtoOut;

public class ProfessorsRestControllerTest extends AbstractRestControllerTest {
    
    @Test
    public void checkMethodGetAllShouldShowAllProfessors() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/rest/professor/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<ProfessorDtoOut> professorsDtoOutList = mapper.readerForListOf(ProfessorDtoOut.class).readValue(response);
        assertEquals(professorsDtoOutList.size(), 14);
        assertEquals(professorsDtoOutList.get(2).getId(), 3);
        assertEquals(professorsDtoOutList.get(2).getFirstName(), "Olena");
        assertEquals(professorsDtoOutList.get(2).getLastName(), "Rebrov");
        assertEquals(professorsDtoOutList.get(3).getId(), 4);
        assertEquals(professorsDtoOutList.get(3).getFirstName(), "Darya");
        assertEquals(professorsDtoOutList.get(3).getLastName(), "Rebrov");
        assertEquals(professorsDtoOutList.get(4).getId(), 5);
        assertEquals(professorsDtoOutList.get(4).getFirstName(), "Mykola");
        assertEquals(professorsDtoOutList.get(4).getLastName(), "Rebrov");
    }
    
    @Test
    public void checkMethodGetShouldShowNecessaryProfessor() throws Exception {
        String response =  mockMvc.perform(MockMvcRequestBuilders.get("/rest/professor/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        ProfessorDtoOut professorDtoOut = mapper.readValue(response, ProfessorDtoOut.class);
        assertEquals(professorDtoOut.getId(), 1);
        assertEquals(professorDtoOut.getFirstName(), "Viktor");
        assertEquals(professorDtoOut.getLastName(), "Rebrov");
    }
    
    @Test
    public void checkMethodCreateShouldCreateProfessor() throws Exception {
        ProfessorDtoIn professorDtoIn = ProfessorDtoIn.builder().firstName("Yaroslav").lastName("Voronovskyi").build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(professorDtoIn);  
        mockMvc.perform(MockMvcRequestBuilders.post("/rest/professor").content(requestJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/rest/professor/21")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        ProfessorDtoOut professorDtoOut = mapper.readValue(response, ProfessorDtoOut.class);
        assertEquals(professorDtoOut.getId(), 21);
        assertEquals(professorDtoOut.getFirstName(), "Yaroslav");
        assertEquals(professorDtoOut.getLastName(), "Voronovskyi");
    }
    
    @Test
    public void checkMethodUpdateShouldUpdateNecessaryProfessor() throws Exception {
        ProfessorDtoIn professorDtoIn = ProfessorDtoIn.builder().firstName("Yaroslav").lastName("Voronovskyi").build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(professorDtoIn);  
        String response = mockMvc.perform(MockMvcRequestBuilders.put("/rest/professor/1").content(requestJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        ProfessorDtoOut professorDtoOut = mapper.readValue(response, ProfessorDtoOut.class);
        assertEquals(professorDtoOut.getId(), 1);
        assertEquals(professorDtoOut.getFirstName(), "Yaroslav");
        assertEquals(professorDtoOut.getLastName(), "Voronovskyi");
    }
    
    @Test
    public void checkMethodDeleteShouldDeleteNecessaryProfessor() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/rest/professor/10")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/rest/professor/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<ProfessorDtoOut> professorsDtoOutList = mapper.readerForListOf(ProfessorDtoOut.class).readValue(response);
        assertEquals(professorsDtoOutList.size(), 9);
        assertFalse(professorsDtoOutList.contains(ProfessorDtoOut.builder().id(10).build()));
    }
    
    @Test
    public void checkMethodGetShouldThrowExceptionNecessaryProfessor() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/rest/professor/111")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void checkMethodCreateShouldThrowException() throws Exception {
        ProfessorDtoIn professorDtoIn = ProfessorDtoIn.builder().firstName("Yar").lastName("Vo").build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(professorDtoIn);  
        mockMvc.perform(MockMvcRequestBuilders.post("/rest/professor").content(requestJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
    }
    
    @Test
    public void checkMethodUpdateFormShouldThrowExceptionNecessaryProfessor() throws Exception {
        ProfessorDtoIn professorDtoIn = ProfessorDtoIn.builder().firstName("Yaroslav").lastName("Voronovskyi").build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(professorDtoIn);
        mockMvc.perform(MockMvcRequestBuilders.get("/rest/professor/111").content(requestJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isNotFound());
    }
    
    @Test
    public void checkMethodUpdateShouldThrowExceptionNecessaryProfessor() throws Exception {
        ProfessorDtoIn professorDtoIn = ProfessorDtoIn.builder().firstName("Yar").lastName("Vo").build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(professorDtoIn);  
        mockMvc.perform(MockMvcRequestBuilders.put("/rest/professor/1").content(requestJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
    }
    
    @Test
    public void checkMethodDeleteShouldThrowExceptionNecessaryProfessor() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/rest/professor/111")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    } 
}
