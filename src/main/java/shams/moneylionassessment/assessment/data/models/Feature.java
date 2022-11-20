package shams.moneylionassessment.assessment.data.models;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.Objects;
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

    // Not using @EqualsAndHashCode annotation due to performance considerations with JPA
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Feature feature = (Feature) o;
        return name != null && Objects.equals(name, feature.name);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}