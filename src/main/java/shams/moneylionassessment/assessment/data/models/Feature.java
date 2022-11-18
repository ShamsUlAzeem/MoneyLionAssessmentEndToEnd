package shams.moneylionassessment.assessment.data.models;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Accessors(fluent = true)
@Entity
@Table(name = "FEATURE")
public class Feature {

    @Id
    @Size(max = 255)
    @Column(name = "NAME", unique = true, nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(name = "USER_FEATURE_MAPPING",
            joinColumns = @JoinColumn(name = "FEATURE_NAME"),
            inverseJoinColumns = @JoinColumn(name = "USERNAME"))
    private Set<User> users = new LinkedHashSet<>();

}