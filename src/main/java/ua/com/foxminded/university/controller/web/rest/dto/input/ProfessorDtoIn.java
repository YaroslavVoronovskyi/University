package ua.com.foxminded.university.controller.web.rest.dto.input;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ProfessorDtoIn extends PersonDtoIn implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Builder.Default
    private List<SubjectDtoIn> subjectList = new ArrayList<>();
}
