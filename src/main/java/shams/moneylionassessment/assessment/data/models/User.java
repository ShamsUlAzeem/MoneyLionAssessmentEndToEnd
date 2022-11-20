package shams.moneylionassessment.assessment.data.models;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Accessors(fluent = true)
@Entity
@Table(name = "USERS")
public class User {

    @Id
    @Column(name = "USERNAME", columnDefinition = "VARCHAR_IGNORECASE(50) not null")
    private String username;

    @Column(name = "PASSWORD", columnDefinition = "VARCHAR_IGNORECASE(500) not null")
    private String password;

    @NotNull
    @Column(name = "ENABLED", nullable = false)
    private Boolean enabled = false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USER_FEATURE_MAPPING",
            joinColumns = @JoinColumn(name = "USERNAME"),
            inverseJoinColumns = @JoinColumn(name = "FEATURE_NAME"))
    private Set<Feature> features = new LinkedHashSet<>();

}