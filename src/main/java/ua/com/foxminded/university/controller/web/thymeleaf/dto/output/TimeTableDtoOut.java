package ua.com.foxminded.university.controller.web.thymeleaf.dto.output;

import java.time.LocalDate;
import java.time.LocalTime;

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
public class TimeTableDtoOut {
    private int id;
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime startTime;
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime endTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private AuditoryDtoOut auditory;
    private CourseDtoOut course;
}
