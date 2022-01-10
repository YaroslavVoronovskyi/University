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
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class StudentDtoOut extends PersonDtoOut implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private GroupDtoOut group;
}
