package ua.com.foxminded.university.controller.rest.it;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import ua.com.foxminded.university.controller.web.rest.dto.input.AuditoryDtoIn;
import ua.com.foxminded.university.controller.web.rest.dto.output.AuditoryDtoOut;

public class AuditoriesRestControllerTestIT extends AbstractRestControllerTestIT {
    
    @Test
    public void checkMethodGetAllShouldGetAllAuditories() throws Exception {
        String response = restTemplate.getForObject("http://localhost:" + port + "/rest/auditory/", String.class);
        ObjectMapper mapper = new ObjectMapper();
        List<AuditoryDtoOut> auditoriesDtoOutList = mapper.readerForListOf(AuditoryDtoOut.class).readValue(response);
        assertEquals(auditoriesDtoOutList.size(), 25);
        assertEquals(auditoriesDtoOutList.get(2).getId(), 3);
        assertEquals(auditoriesDtoOutList.get(2).getCapacity(), 20);
        assertEquals(auditoriesDtoOutList.get(3).getId(), 4);
        assertEquals(auditoriesDtoOutList.get(3).getCapacity(), 25);
        assertEquals(auditoriesDtoOutList.get(4).getId(), 5);
        assertEquals(auditoriesDtoOutList.get(4).getCapacity(), 30);
    }
    
    @Test
    void checkMethodGetShouldGetNecessaryAuditory() {
        AuditoryDtoOut auditoryDtoOut = restTemplate
                .getForObject("http://localhost:" + port + "/rest/auditory/1", AuditoryDtoOut.class);
        assertEquals(auditoryDtoOut.getId(), 1);
        assertEquals(auditoryDtoOut.getCapacity(), 40);
    }
    
    @Test
    public void checkMethodCreateShouldCreateAuditory() throws Exception {
        AuditoryDtoIn auditoryDtoIn = AuditoryDtoIn.builder().capacity(22).build();
        restTemplate.postForObject("http://localhost:" + port + "/rest/auditory/", auditoryDtoIn, String.class);
        String response = restTemplate.getForObject("http://localhost:" + port + "/rest/auditory/", String.class);
        ObjectMapper mapper = new ObjectMapper();
        List<AuditoryDtoOut> auditoriesDtoOutList = mapper.readerForListOf(AuditoryDtoOut.class).readValue(response);
        AuditoryDtoOut auditoryDtoOut = restTemplate.getForObject("http://localhost:" + port + "/rest/auditory/6", AuditoryDtoOut.class);
        assertEquals(auditoriesDtoOutList.size(), 6);
        assertEquals(auditoryDtoOut.getId(), 6);
        assertEquals(auditoryDtoOut.getCapacity(), 22);
    }
    
    @Test
    public void checkMethodUpdateShouldUpdateNecessaryAuditory() throws Exception {
        AuditoryDtoIn auditoryDtoIn = AuditoryDtoIn.builder().capacity(49).build();
        restTemplate.put("http://localhost:" + port + "/rest/auditory/1", auditoryDtoIn);
        AuditoryDtoOut auditoryDtoOut = restTemplate
                .getForObject("http://localhost:" + port + "/rest/auditory/1", AuditoryDtoOut.class);
        assertEquals(auditoryDtoOut.getId(), 1);
        assertEquals(auditoryDtoOut.getCapacity(), 49);
    }
    
    @Test
    public void checkMethodDeleteShouldDeleteNecessaryAuditory() throws Exception {
        restTemplate.delete("http://localhost:" + port + "/rest/auditory/10");
        String response = restTemplate.getForObject("http://localhost:" + port + "/rest/auditory/", String.class);
        ObjectMapper mapper = new ObjectMapper();
        List<AuditoryDtoOut> auditoriesDtoOutList = mapper.readerForListOf(AuditoryDtoOut.class).readValue(response);
        assertEquals(auditoriesDtoOutList.size(), 15);
        assertFalse(auditoriesDtoOutList.contains(AuditoryDtoOut.builder().id(10).build()));
    }
}
