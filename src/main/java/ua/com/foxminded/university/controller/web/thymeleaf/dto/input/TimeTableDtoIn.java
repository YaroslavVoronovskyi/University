package ua.com.foxminded.university.controller.web.thymeleaf.dto.input;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.validation.constraints.FutureOrPresent;

import org.springframework.format.annotation.DateTimeFormat;

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
public class TimeTableDtoIn {
    private int id;
    
    @FutureOrPresent(message = "Start time must be in future or present time")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime startTime;
    
    @FutureOrPresent(message = "End time must be in future or present time")
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime endTime;
    
    @FutureOrPresent(message = "Date must be in future or present time")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    
    private AuditoryDtoIn auditory;
    private CourseDtoIn course;
}
