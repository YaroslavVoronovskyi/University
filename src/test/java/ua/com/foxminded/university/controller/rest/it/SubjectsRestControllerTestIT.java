package ua.com.foxminded.university.controller.rest.it;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import ua.com.foxminded.university.controller.web.rest.dto.input.SubjectDtoIn;
import ua.com.foxminded.university.controller.web.rest.dto.output.SubjectDtoOut;

public class SubjectsRestControllerTestIT extends AbstractRestControllerTestIT {
    
    @Test
    public void checkMethodGetAllShouldShowAllSubjects() throws Exception {
        String response = restTemplate.getForObject("http://localhost:" + port + "/rest/subject/", String.class);
        ObjectMapper mapper = new ObjectMapper();
        List<SubjectDtoOut> subjectsDtoOutList = mapper.readerForListOf(SubjectDtoOut.class).readValue(response);
        assertEquals(subjectsDtoOutList.size(), 10);
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
        SubjectDtoOut subjectDtoOut = restTemplate.getForObject("http://localhost:" + port + "/rest/subject/1", SubjectDtoOut.class);
        assertEquals(subjectDtoOut.getId(), 1);
        assertEquals(subjectDtoOut.getName(), "Math");
        assertEquals(subjectDtoOut.getDescription(), "Math includes the study of such topics as number theory, algebra, geometry and mathematical analysis");
    }
    
    @Test
    public void checkMethodCreateShouldCreateSubject() throws Exception {
        SubjectDtoIn subjectDtoIn = SubjectDtoIn.builder().name("Java").description("A programming language").build();
        restTemplate.postForObject("http://localhost:" + port + "/rest/subject/", subjectDtoIn, String.class);
        String response = restTemplate.getForObject("http://localhost:" + port + "/rest/subject/", String.class);
        ObjectMapper mapper = new ObjectMapper();
        List<SubjectDtoOut> subjectsDtoOutList = mapper.readerForListOf(SubjectDtoOut.class).readValue(response);
        SubjectDtoOut subjectDtoOut = restTemplate.getForObject("http://localhost:" + port + "/rest/subject/41", SubjectDtoOut.class);
        assertEquals(subjectsDtoOutList.size(), 41);
        assertEquals(subjectDtoOut.getId(), 41);
        assertEquals(subjectDtoOut.getName(), "Java");
        assertEquals(subjectDtoOut.getDescription(), "A programming language");
    }
    
    @Test
    public void checkMethodUpdateShouldUpdateNecessarySubject() throws Exception {
        SubjectDtoIn subjectDtoIn = SubjectDtoIn.builder().name("Java").description("A programming language").build();
        restTemplate.put("http://localhost:" + port + "/rest/subject/1", subjectDtoIn);
        SubjectDtoOut subjectDtoOut = restTemplate.getForObject("http://localhost:" + port + "/rest/subject/1", SubjectDtoOut.class);
        assertEquals(subjectDtoOut.getId(), 1);
        assertEquals(subjectDtoOut.getName(), "Java");
        assertEquals(subjectDtoOut.getDescription(), "A programming language");
    }
    
    @Test
    public void checkMethodDeleteShouldDeleteNecessarySubject() throws Exception {
        restTemplate.delete("http://localhost:" + port + "/rest/subject/10");
        String response = restTemplate.getForObject("http://localhost:" + port + "/rest/subject/", String.class);
        ObjectMapper mapper = new ObjectMapper();
        List<SubjectDtoOut> subjectsDtoOutList = mapper.readerForListOf(SubjectDtoOut.class).readValue(response);
        assertEquals(subjectsDtoOutList.size(), 50);
        assertFalse(subjectsDtoOutList.contains(SubjectDtoOut.builder().id(10).build()));
    }
}
