package ua.com.foxminded.university.controller.web.thymeleaf.dto.input;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class SubjectDtoIn {
    private int id;
    
    @NotBlank(message = "Name must not be null and must contain at least onenon-whitespace character")
    @Size(min = 4, max = 15, message = "Course name should be between 4 and 15 characters")
    private String name;
    
    @NotBlank(message = "Description must not be null and must contain at least onenon-whitespace character")
    @Size(min = 15, max = 350, message = "Course name should be between 15 and 350 characters")
    private String description;
    private ProfessorDtoIn professor;
}
