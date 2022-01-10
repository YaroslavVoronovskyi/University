package ua.com.foxminded.university.controller.rest.it;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ua.com.foxminded.university.config.AppConfigTest;

@SpringBootTest(classes = { AppConfigTest.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Sql(scripts = {"/ScriptInsertUniversity.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class AbstractRestControllerTestIT {
    @LocalServerPort
    Integer port;
    
    @Autowired
    protected TestRestTemplate restTemplate;

}
