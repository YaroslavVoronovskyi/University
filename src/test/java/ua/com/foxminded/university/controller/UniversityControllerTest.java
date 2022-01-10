package ua.com.foxminded.university.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;

public class UniversityControllerTest extends AbstractControllerTest {
    @Test
    public void test() throws Exception {
        mockMvc.perform(get("/"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(view().name("university"));
    }
}
