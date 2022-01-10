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

import ua.com.foxminded.university.controller.web.rest.dto.input.GroupDtoIn;
import ua.com.foxminded.university.controller.web.rest.dto.output.GroupDtoOut;

public class GroupsRestControllerTest extends AbstractRestControllerTest {
    
    @Test
    public void checkMethodGetAllShouldShowAllGroups() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/rest/group/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<GroupDtoOut> groupsDtoOutList = mapper.readerForListOf(GroupDtoOut.class).readValue(response);
        assertEquals(groupsDtoOutList.size(), 20);
        assertEquals(groupsDtoOutList.get(2).getId(), 3);
        assertEquals(groupsDtoOutList.get(2).getName(), "TY-21");
        assertEquals(groupsDtoOutList.get(3).getId(), 4);
        assertEquals(groupsDtoOutList.get(3).getName(), "UI-22");
        assertEquals(groupsDtoOutList.get(4).getId(), 5);
        assertEquals(groupsDtoOutList.get(4).getName(), "OP-31");
    }
    
    @Test
    public void checkMethodGetShouldShowNecessaryGroup() throws Exception {
        String response =  mockMvc.perform(MockMvcRequestBuilders.get("/rest/group/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        GroupDtoOut groupDtoOut = mapper.readValue(response, GroupDtoOut.class);
        assertEquals(groupDtoOut.getId(), 1);
        assertEquals(groupDtoOut.getName(), "FF-22");
    }
    
    @Test
    public void checkMethodCreateShouldCreateGroup() throws Exception {
        GroupDtoIn groupDtoIn = GroupDtoIn.builder().name("FF-22").build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(groupDtoIn);
        mockMvc.perform(MockMvcRequestBuilders.post("/rest/group").content(requestJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/rest/group/101")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        GroupDtoOut groupDtoOut = mapper.readValue(response, GroupDtoOut.class);
        assertEquals(groupDtoOut.getId(), 101);
        assertEquals(groupDtoOut.getName(), "FF-22");
    }
    
    @Test
    public void checkMethodUpdateShouldUpdateNecessaryGroup() throws Exception {
        GroupDtoIn groupDtoIn = GroupDtoIn.builder().name("FF-22").build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(groupDtoIn);
        String response = mockMvc.perform(MockMvcRequestBuilders.put("/rest/group/1").content(requestJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        GroupDtoOut groupDtoOut = mapper.readValue(response, GroupDtoOut.class);
        assertEquals(groupDtoOut.getId(), 1);
        assertEquals(groupDtoOut.getName(), "FF-22");
    }
    
    @Test
    public void checkMethodDeleteShouldDeleteNecessaryGroup() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/rest/group/10")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/rest/group/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<GroupDtoOut> groupsDtoOutList = mapper.readerForListOf(GroupDtoOut.class).readValue(response);
        assertEquals(groupsDtoOutList.size(), 39);
        assertFalse(groupsDtoOutList.contains(GroupDtoOut.builder().id(10).build()));
    }
    
    @Test
    public void checkMethodGetShouldThrowExceptionNecessaryGroup() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/rest/group/111")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isNotFound());
    }
    
    @Test
    public void checkMethodCreateShouldThrowException() throws Exception {
        GroupDtoIn groupDtoIn = GroupDtoIn.builder().name("FF-222").build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(groupDtoIn);
        mockMvc.perform(MockMvcRequestBuilders.post("/rest/group").content(requestJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
    }
    
    @Test
    public void checkMethodUpdateFormShouldThrowExceptionNecessaryGroup() throws Exception {
        GroupDtoIn groupDtoIn = GroupDtoIn.builder().name("FF-22").build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(groupDtoIn);
        mockMvc.perform(MockMvcRequestBuilders.put("/rest/group/111").content(requestJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
    }
    
    @Test
    public void checkMethodUpdateShouldThrowExceptionNecessaryGroup() throws Exception {
        GroupDtoIn groupDtoIn = GroupDtoIn.builder().name("FF-222").build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = objectWriter.writeValueAsString(groupDtoIn);
        mockMvc.perform(MockMvcRequestBuilders.put("/rest/group/1").content(requestJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
    }
    
    @Test
    public void checkMethodDeleteShouldThrowExceptionNecessaryGroup() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/rest/group/111")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }  
}
