package ua.com.foxminded.university.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UniversityController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UniversityController.class);
    
    @GetMapping("/university")
    public String index() {
        LOGGER.debug("Try open start page wep application University");
        return "university";   
    }
}
