package ua.com.foxminded.university.controller.web.thymeleaf.dto.input;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.util.ArrayList;

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
public class CourseDtoIn {
    private int id;
    
    @NotBlank(message = "Name must not be null and must contain at least onenon-whitespace character")
    @Size(min = 3, max = 10, message = "Course name should be between 3 and 10 characters")
    private String name;
    private SubjectDtoIn subject;
    
    @Builder.Default
    private List<GroupDtoIn> groupList = new ArrayList<>();
    
    @Builder.Default
    private List<TimeTableDtoIn> timeTableList = new ArrayList<>();
}
