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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
@Table(name = "groups")
public class Group implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private int id;
    
    @Column(name = "group_name", nullable = false)
    private String name;
    
    @Builder.Default
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Student> studentList = new ArrayList<>();
    
    @Builder.Default
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "groups_courses", joinColumns = {
            @JoinColumn(name = "group_ref", referencedColumnName = "group_id"),  
            @JoinColumn(name = "group_name", referencedColumnName = "group_name")}, inverseJoinColumns = {
                    @JoinColumn(name = "course_ref", referencedColumnName = "course_id"),
                    @JoinColumn(name = "course_name", referencedColumnName = "course_name")})
    private List<Course> courseList = new ArrayList<>();
}
