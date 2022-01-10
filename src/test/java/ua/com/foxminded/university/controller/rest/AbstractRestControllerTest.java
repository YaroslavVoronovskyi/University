package ua.com.foxminded.university.controller.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ua.com.foxminded.university.config.AppConfigTest;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { AppConfigTest.class })
@Sql(scripts = {"/ScriptInsertUniversity.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class AbstractRestControllerTest {
    @Autowired
    private WebApplicationContext appContext;
    protected MockMvc mockMvc;
    
    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.appContext).build();
    }
}
