package ua.com.foxminded.university.controller.rest.it;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import ua.com.foxminded.university.controller.web.rest.dto.input.CourseDtoIn;
import ua.com.foxminded.university.controller.web.rest.dto.output.CourseDtoOut;

public class CoursesRestControllerTestIT extends AbstractRestControllerTestIT {
    
    @Test
    public void checkMethodGetAllShouldGetAllCourses() throws Exception {
        String response = restTemplate.getForObject("http://localhost:" + port + "/rest/course/", String.class);
        ObjectMapper mapper = new ObjectMapper();
        List<CourseDtoOut> coursesDtoOutList = mapper.readerForListOf(CourseDtoOut.class).readValue(response);
        assertEquals(coursesDtoOutList.size(), 54);
        assertEquals(coursesDtoOutList.get(2).getId(), 3);
        assertEquals(coursesDtoOutList.get(2).getName(), "THIRD");
        assertEquals(coursesDtoOutList.get(3).getId(), 4);
        assertEquals(coursesDtoOutList.get(3).getName(), "FOURTH");
        assertEquals(coursesDtoOutList.get(4).getId(), 5);
        assertEquals(coursesDtoOutList.get(4).getName(), "FIFTH");
    }
    
    @Test
    public void checkMethodGetShouldGetNecessaryCourse() throws Exception {
        CourseDtoOut courseDtoOut = restTemplate.getForObject("http://localhost:" + port + "/rest/course/1", CourseDtoOut.class);                
        assertEquals(courseDtoOut.getId(), 1);
        assertEquals(courseDtoOut.getName(), "TENTH");
    }
    
    @Test
    public void checkMethodCreateShouldCreateCourse() throws Exception {
        CourseDtoIn courseDtoIn = CourseDtoIn.builder().name("SEVENTH").build();         
        restTemplate.postForObject("http://localhost:" + port + "/rest/course/", courseDtoIn, String.class);
        String response = restTemplate.getForObject("http://localhost:" + port + "/rest/course/", String.class);
        ObjectMapper mapper = new ObjectMapper();
        List<CourseDtoOut> coursesDtoOutList = mapper.readerForListOf(CourseDtoOut.class).readValue(response);
        CourseDtoOut courseDtoOut = restTemplate.getForObject("http://localhost:" + port + "/rest/course/43", CourseDtoOut.class);
        assertEquals(coursesDtoOutList.size(), 43);
        assertEquals(courseDtoOut.getId(), 43);
        assertEquals(courseDtoOut.getName(), "SEVENTH");
    }
    
    @Test
    public void checkMethodUpdateShouldUpdateNecessaryCourse() throws Exception {
        CourseDtoIn courseDtoIn = CourseDtoIn.builder().name("TENTH").build(); 
        restTemplate.put("http://localhost:" + port + "/rest/course/1", courseDtoIn);
        CourseDtoOut courseDtoOut = restTemplate.getForObject("http://localhost:" + port + "/rest/course/1", CourseDtoOut.class);
        assertEquals(courseDtoOut.getId(), 1);
        assertEquals(courseDtoOut.getName(), "TENTH");
    }
    
    @Test
    public void checkMethodDeleteShouldDeleteNecessaryCourse() throws Exception {
        restTemplate.delete("http://localhost:" + port + "/rest/course/10");
        String response = restTemplate.getForObject("http://localhost:" + port + "/rest/course/", String.class);
        ObjectMapper mapper = new ObjectMapper();
        List<CourseDtoOut> coursesDtoOutList = mapper.readerForListOf(CourseDtoOut.class).readValue(response);
        assertEquals(coursesDtoOutList.size(), 48);
        assertFalse(coursesDtoOutList.contains(CourseDtoOut.builder().id(10).build()));
    }
    
}
