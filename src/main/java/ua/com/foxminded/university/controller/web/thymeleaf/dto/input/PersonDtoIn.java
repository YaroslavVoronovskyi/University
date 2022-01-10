package ua.com.foxminded.university.controller.web.thymeleaf.dto.input;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Setter
@Getter
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class PersonDtoIn {
    private int id;
    
    @NotBlank(message = "First name must not be null and must contain at least onenon-whitespace character")
    @Size(min = 3, max = 15, message = "First name should be between 3 and 15 characters")
    private String firstName;
    
    @NotBlank(message = "Last name must not be null and must contain at least onenon-whitespace character")
    @Size(min = 3, max = 15, message = "Last name should be between 3 and 15 characters")
    private String lastName;   
}
