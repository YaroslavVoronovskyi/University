package ua.com.foxminded.university.controller.web.thymeleaf.dto.output;

import java.util.List;
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
public class ProfessorDtoOut extends PersonDtoOut {
    @Builder.Default
    private List<SubjectDtoOut> subjectList = new ArrayList<>();
}
