package ua.com.foxminded.university.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
@Entity
@Table(name = "professors")
public class Professor extends Person implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Builder.Default
    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Subject> subjectList = new ArrayList<>();
}
