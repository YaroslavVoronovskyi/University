package ua.com.foxminded.university.controller.rest.it;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import ua.com.foxminded.university.controller.web.rest.dto.input.ProfessorDtoIn;
import ua.com.foxminded.university.controller.web.rest.dto.output.ProfessorDtoOut;

public class ProfessorsRestControllerTestIT extends AbstractRestControllerTestIT {
    
    @Test
    public void checkMethodGetAllShouldGetAllProfessors() throws Exception {
        String response = restTemplate.getForObject("http://localhost:" + port + "/rest/professor/", String.class);
        ObjectMapper mapper = new ObjectMapper();
        List<ProfessorDtoOut> professorsDtoOutList = mapper.readerForListOf(ProfessorDtoOut.class).readValue(response);
        assertEquals(professorsDtoOutList.size(), 21);
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
    public void checkMethodGetShouldGetNecessaryProfessor() throws Exception {
        ProfessorDtoOut professorDtoOut = restTemplate.getForObject("http://localhost:" + port + "/rest/professor/1", ProfessorDtoOut.class);
        assertEquals(professorDtoOut.getId(), 1);
        assertEquals(professorDtoOut.getFirstName(), "Viktor");
        assertEquals(professorDtoOut.getLastName(), "Rebrov");
    }
    
    @Test
    public void checkMethodCreateShouldCreateProfessor() throws Exception {
        ProfessorDtoIn professorDtoIn = ProfessorDtoIn.builder().firstName("Yaroslav").lastName("Voronovskyi").build();
        restTemplate.postForObject("http://localhost:" + port + "/rest/professor/", professorDtoIn, String.class);       
        String response = restTemplate.getForObject("http://localhost:" + port + "/rest/professor/", String.class);
        ObjectMapper mapper = new ObjectMapper();
        List<ProfessorDtoOut> professorsDtoOutList = mapper.readerForListOf(ProfessorDtoOut.class).readValue(response);
        ProfessorDtoOut professorDtoOut = restTemplate.getForObject("http://localhost:" + port + "/rest/professor/16", ProfessorDtoOut.class);
        assertEquals(professorsDtoOutList.size(), 16);
        assertEquals(professorDtoOut.getId(), 16);
        assertEquals(professorDtoOut.getFirstName(), "Yaroslav");
        assertEquals(professorDtoOut.getLastName(), "Voronovskyi");
    }
    
    @Test
    public void checkMethodUpdateShouldUpdateNecessaryProfessor() throws Exception {
        ProfessorDtoIn professorDtoIn = ProfessorDtoIn.builder().firstName("Yaroslav").lastName("Voronovskyi").build();
        restTemplate.put("http://localhost:" + port + "/rest/professor/1", professorDtoIn);
        ProfessorDtoOut professorDtoOut = restTemplate.getForObject("http://localhost:" + port + "/rest/professor/1", ProfessorDtoOut.class);
        assertEquals(professorDtoOut.getId(), 1);
        assertEquals(professorDtoOut.getFirstName(), "Yaroslav");
        assertEquals(professorDtoOut.getLastName(), "Voronovskyi");
    }
    
    @Test
    public void checkMethodDeleteShouldDeleteNecessaryProfessor() throws Exception {
        restTemplate.delete("http://localhost:" + port + "/rest/professor/10");
        String response = restTemplate.getForObject("http://localhost:" + port + "/rest/professor/", String.class);
        ObjectMapper mapper = new ObjectMapper();
        List<ProfessorDtoOut> professorsDtoOutList = mapper.readerForListOf(ProfessorDtoOut.class).readValue(response);
        assertEquals(professorsDtoOutList.size(), 5);
        assertFalse(professorsDtoOutList.contains(ProfessorDtoOut.builder().id(10).build()));
    }
}
