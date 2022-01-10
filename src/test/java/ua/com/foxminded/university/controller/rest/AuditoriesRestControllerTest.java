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

import ua.com.foxminded.university.controller.web.rest.dto.input.AuditoryDtoIn;
import ua.com.foxminded.university.controller.web.rest.dto.output.AuditoryDtoOut;

public class AuditoriesRestControllerTest extends AbstractRestControllerTest {

    @Test
    public void checkMethodGetAllShouldShowAllAuditories() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/rest/auditory/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<AuditoryDtoOut> auditoriesDtoOutList = mapper.readerForListOf(AuditoryDtoOut.class).readValue(response);
        assertEquals(auditoriesDtoOutList.size(), 45);
        assertEquals(auditoriesDtoOutList.get(2).getId(), 3);
        assertEquals(auditoriesDtoOutList.get(2).getCapacity(), 20);
        assertEquals(auditoriesDtoOutList.get(3).getId(), 4);
        assertEquals(auditoriesDtoOutList.get(3).getCapacity(), 25);
        assertEquals(auditoriesDtoOutList.get(4).getId(), 5);
        assertEquals(auditoriesDtoOutList.get(4).getCapacity(), 30);
    }
    
    @Test
    public void checkMethodGetShouldShowNecessaryAuditory() throws Exception {
        String response =  mockMvc.perform(MockMvcRequestBuilders.get("/rest/auditory/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        AuditoryDtoOut auditoryDtoOut = mapper.readValue(response, AuditoryDtoOut.class);
        assertEquals(auditoryDtoOut.getId(), 1);
        assertEquals(auditoryDtoOut.getCapacity(), 40);
    }
    
    @Test
    public void checkMethodCreateShouldCreateAuditory() throws Exception {
        AuditoryDtoIn auditoryDtoIn = AuditoryDtoIn.builder().capacity(40).build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(auditoryDtoIn);        
        mockMvc.perform(MockMvcRequestBuilders.post("/rest/auditory")
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()); 
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/rest/auditory/6")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        AuditoryDtoOut auditoryDtoOut = mapper.readValue(response, AuditoryDtoOut.class);
        assertEquals(auditoryDtoOut.getId(), 6);
        assertEquals(auditoryDtoOut.getCapacity(), 40);
    }
    
    @Test
    public void checkMethodUpdateShouldUpdateNecessaryAuditory() throws Exception {
        AuditoryDtoIn auditoryDtoIn = AuditoryDtoIn.builder().capacity(49).build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(auditoryDtoIn);  
        String response = mockMvc.perform(MockMvcRequestBuilders.put("/rest/auditory/1").content(requestJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        AuditoryDtoOut auditoryDtoOut = mapper.readValue(response, AuditoryDtoOut.class);
        assertEquals(auditoryDtoOut.getId(), 1);
        assertEquals(auditoryDtoOut.getCapacity(), 49);
    }
    
    @Test
    public void checkMethodDeleteShouldDeleteNecessaryAuditory() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/rest/auditory/10")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/rest/auditory/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<AuditoryDtoOut> auditoriesDtoOutList = mapper.readerForListOf(AuditoryDtoOut.class).readValue(response);
        assertEquals(auditoriesDtoOutList.size(), 35);
        assertFalse(auditoriesDtoOutList.contains(AuditoryDtoOut.builder().id(10).build()));
    }
    
    @Test
    public void checkMethodGetShouldThrowExceptionNecessaryAuditory() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/rest/auditory/111")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isNotFound());
    }
    
    @Test
    public void checkMethodCreateShouldThrowException() throws Exception {
        AuditoryDtoIn auditoryDtoIn = AuditoryDtoIn.builder().capacity(400).build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(auditoryDtoIn);  
        mockMvc.perform(MockMvcRequestBuilders.post("/rest/auditory").content(requestJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status()
                .isBadRequest());
    }

    @Test
    public void checkMethodUpdateShouldThrowExceptionNecessaryAuditory() throws Exception {
        AuditoryDtoIn auditoryDtoIn = AuditoryDtoIn.builder().capacity(400).build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(auditoryDtoIn);  
        mockMvc.perform(MockMvcRequestBuilders.put("/rest/auditory/1").content(requestJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status()
                .isBadRequest());
    }
    
    @Test
    public void checkMethodUpdateShouldThrowNotFoundExceptionNecessaryAuditory() throws Exception {
        AuditoryDtoIn auditoryDtoIn = AuditoryDtoIn.builder().capacity(40).build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(auditoryDtoIn);
        mockMvc.perform(MockMvcRequestBuilders.put("/rest/auditory/111").content(requestJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status()
                .isNotFound());
    }
    
    @Test
    public void checkMethodDeleteShouldThrowExceptionNecessaryAuditory() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/rest/auditory/111")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isNotFound());
    }
}
