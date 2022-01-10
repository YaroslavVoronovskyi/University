package ua.com.foxminded.university.controller.web.thymeleaf.dto.output;

import java.util.ArrayList;
import java.util.List;

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
public class CourseDtoOut {
    private int id;
    private String name;
    private SubjectDtoOut subject;
    
    @Builder.Default
    private List<GroupDtoOut> groupList = new ArrayList<>();
    
    @Builder.Default
    private List<TimeTableDtoOut> timeTableList = new ArrayList<>();
}
