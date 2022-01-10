package ua.com.foxminded.university.controller.web.thymeleaf.dto.output;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class GroupDtoOut {
    private int id;
    private String name;
    
    @Builder.Default
    private List<StudentDtoOut> studentList= new ArrayList<>();
    
    @Builder.Default
    private List<CourseDtoOut> courseList= new ArrayList<>();   
}
