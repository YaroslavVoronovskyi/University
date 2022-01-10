package ua.com.foxminded.university.controller.web.thymeleaf.dto.input;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
public class GroupDtoIn {
    private int id;
    
    @NotBlank(message = "Name must not be null and must contain at least onenon-whitespace character")
    @Size(min = 5, max = 5, message = "Group name should be 5 characters")
    private String name;
    
    @Builder.Default
    private List<StudentDtoIn> studentList = new ArrayList<>();
    
    @Builder.Default
    private List<CourseDtoIn> courseList = new ArrayList<>();   
}
