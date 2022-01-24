package ua.com.foxminded.university.controller.rest.it;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import ua.com.foxminded.university.controller.web.rest.dto.input.GroupDtoIn;
import ua.com.foxminded.university.controller.web.rest.dto.output.GroupDtoOut;

public class GroupsRestControllerTestIT extends AbstractRestControllerTestIT {
    
    @Test
    public void checkMethodGetAllShouldGetAllGroups() throws Exception {
        String response = restTemplate.getForObject("http://localhost:" + port + "/rest/group/", String.class);        
        ObjectMapper mapper = new ObjectMapper();
        List<GroupDtoOut> groupsDtoOutList = mapper.readerForListOf(GroupDtoOut.class).readValue(response);
        assertEquals(groupsDtoOutList.size(), 150);
        assertEquals(groupsDtoOutList.get(2).getId(), 3);
        assertEquals(groupsDtoOutList.get(2).getName(), "TY-21");
        assertEquals(groupsDtoOutList.get(3).getId(), 4);
        assertEquals(groupsDtoOutList.get(3).getName(), "UI-22");
        assertEquals(groupsDtoOutList.get(4).getId(), 5);
        assertEquals(groupsDtoOutList.get(4).getName(), "OP-31");
    }
    
    @Test
    public void checkMethodGetShouldGetNecessaryGroup() throws Exception {
        GroupDtoOut groupDtoOut =  restTemplate.getForObject("http://localhost:" + port + "/rest/group/1", GroupDtoOut.class);
        assertEquals(groupDtoOut.getId(), 1);
        assertEquals(groupDtoOut.getName(), "FF-22");
    }
    
    @Test
    public void checkMethodCreateShouldCreateGroup() throws Exception {
        GroupDtoIn groupDtoIn = GroupDtoIn.builder().name("FF-22").build();
        restTemplate.postForObject("http://localhost:" + port + "/rest/group/", groupDtoIn, String.class);
        String response = restTemplate.getForObject("http://localhost:" + port + "/rest/group/", String.class);        
        ObjectMapper mapper = new ObjectMapper();
        List<GroupDtoOut> groupsDtoOutList = mapper.readerForListOf(GroupDtoOut.class).readValue(response);
        GroupDtoOut groupDtoOut =  restTemplate.getForObject("http://localhost:" + port + "/rest/group/141", GroupDtoOut.class);
        assertEquals(groupsDtoOutList.size(), 140);
        assertEquals(groupDtoOut.getId(), 141);
        assertEquals(groupDtoOut.getName(), "FF-22");
    }
    
    @Test
    public void checkMethodUpdateShouldUpdateNecessaryGroup() throws Exception {
        GroupDtoIn groupDtoIn = GroupDtoIn.builder().name("FF-22").build();
        restTemplate.put("http://localhost:" + port + "/rest/group/1", groupDtoIn);
        GroupDtoOut groupDtoOut =  restTemplate.getForObject("http://localhost:" + port + "/rest/group/1", GroupDtoOut.class);
        assertEquals(groupDtoOut.getId(), 1);
        assertEquals(groupDtoOut.getName(), "FF-22");
    }
    
    @Test
    public void checkMethodDeleteShouldDeleteNecessaryGroup() throws Exception {
        restTemplate.delete("http://localhost:" + port + "/rest/group/10");
        String response = restTemplate.getForObject("http://localhost:" + port + "/rest/group/", String.class);        
        ObjectMapper mapper = new ObjectMapper();
        List<GroupDtoOut> groupsDtoOutList = mapper.readerForListOf(GroupDtoOut.class).readValue(response);
        assertEquals(groupsDtoOutList.size(), 109);
        assertFalse(groupsDtoOutList.contains(GroupDtoOut.builder().id(10).build()));
    }
}
