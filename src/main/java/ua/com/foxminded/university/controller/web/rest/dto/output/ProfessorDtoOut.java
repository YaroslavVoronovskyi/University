package ua.com.foxminded.university.controller.web.rest.dto.output;

import java.util.List;
import java.io.Serializable;
import java.util.ArrayList;

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
public class ProfessorDtoOut extends PersonDtoOut implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Builder.Default
    private List<SubjectDtoOut> subjectList = new ArrayList<>();
}
