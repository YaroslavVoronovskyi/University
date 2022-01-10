package ua.com.foxminded.university.controller.web.rest.dto.output;

import java.io.Serializable;

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
public class SubjectDtoOut implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String name;
    private String description;
    private ProfessorDtoOut professor;
}
