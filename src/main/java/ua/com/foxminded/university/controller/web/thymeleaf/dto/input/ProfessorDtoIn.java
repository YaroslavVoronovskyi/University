package ua.com.foxminded.university.controller.web.thymeleaf.dto.input;

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
public class ProfessorDtoIn extends PersonDtoIn {
    @Builder.Default
    private List<SubjectDtoIn> subjectList = new ArrayList<>();
}
