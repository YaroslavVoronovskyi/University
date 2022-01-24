package ua.com.foxminded.university.controller.rest.it;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import ua.com.foxminded.university.controller.web.rest.dto.input.StudentDtoIn;
import ua.com.foxminded.university.controller.web.rest.dto.output.StudentDtoOut;

public class StudentsRestControllerTestIT extends AbstractRestControllerTestIT {
    
    @Test
    public void checkMethodGetAllShouldGetAllStudents() throws Exception {
        String response = restTemplate.getForObject("http://localhost:" + port + "/rest/student/", String.class);
        ObjectMapper mapper = new ObjectMapper();
        List<StudentDtoOut> studentsDtoOutList = mapper.readerForListOf(StudentDtoOut.class).readValue(response);
        assertEquals(studentsDtoOutList.size(), 210);
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
    public void checkMethodGetShouldGetNecessaryStudent() throws Exception {        
        StudentDtoOut studentDtoOut = restTemplate
                .getForObject("http://localhost:" + port + "/rest/student/1", StudentDtoOut.class);
        assertEquals(studentDtoOut.getId(), 1);
        assertEquals(studentDtoOut.getFirstName(), "Yaroslav");
        assertEquals(studentDtoOut.getLastName(), "Voronovskyi");
    }
    
    @Test
    public void checkMethodCreateShouldCreateStudent() throws Exception {
        StudentDtoIn studentDtoIn = StudentDtoIn.builder().firstName("Yaroslav").lastName("Voronovskyi").build();
        restTemplate.postForObject("http://localhost:" + port + "/rest/student/", studentDtoIn, String.class);
        String response = restTemplate.getForObject("http://localhost:" + port + "/rest/student/", String.class);
        ObjectMapper mapper = new ObjectMapper();
        List<StudentDtoOut> studentsDtoOutList = mapper.readerForListOf(StudentDtoOut.class).readValue(response);
        StudentDtoOut studentDtoOut = restTemplate.getForObject("http://localhost:" + port + "/rest/student/231", StudentDtoOut.class);
        assertEquals(studentsDtoOutList.size(), 231);       
        assertEquals(studentDtoOut.getId(), 231);
        assertEquals(studentDtoOut.getFirstName(), "Yaroslav");
        assertEquals(studentDtoOut.getLastName(), "Voronovskyi");
    }
    
    @Test
    public void checkMethodUpdateShouldUpdateNecessaryStudent() throws Exception {
        StudentDtoIn studentDtoIn = StudentDtoIn.builder().firstName("Yaroslav").lastName("Voronovskyi").build();
        restTemplate.put("http://localhost:" + port + "/rest/student/1", studentDtoIn);
        StudentDtoOut studentDtoOut = restTemplate.getForObject("http://localhost:" + port + "/rest/student/1", StudentDtoOut.class);
        assertEquals(studentDtoOut.getId(), 1);
        assertEquals(studentDtoOut.getFirstName(), "Yaroslav");
        assertEquals(studentDtoOut.getLastName(), "Voronovskyi");
    }
    
    @Test
    public void checkMethodDeleteShouldDeleteNecessaryStudent() throws Exception {
        restTemplate.delete("http://localhost:" + port + "/rest/student/10");
        String response = restTemplate.getForObject("http://localhost:" + port + "/rest/student/", String.class);
        ObjectMapper mapper = new ObjectMapper();
        List<StudentDtoOut> studentsDtoOutList = mapper.readerForListOf(StudentDtoOut.class).readValue(response);
        assertEquals(studentsDtoOutList.size(), 250);
        assertFalse(studentsDtoOutList.contains(StudentDtoOut.builder().id(10).build()));
    }
}
