package ua.com.foxminded.university.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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
@Entity
@Table(name = "courses")
public class Course implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private int id;
    
    @Column(name = "course_name", nullable = false)
    private String name;
    
    
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_ref")
    private Subject subject;
    
    @Builder.Default
    @ManyToMany(mappedBy = "courseList", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Group> groupList= new ArrayList<>();
    
    @Builder.Default
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    private List<TimeTable> timeTableList = new ArrayList<>();
}
