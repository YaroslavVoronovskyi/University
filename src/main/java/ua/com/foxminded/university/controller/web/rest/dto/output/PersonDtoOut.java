package ua.com.foxminded.university.controller.web.rest.dto.output;

import java.io.Serializable;

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
public class PersonDtoOut implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String firstName;
    private String lastName;   
}
