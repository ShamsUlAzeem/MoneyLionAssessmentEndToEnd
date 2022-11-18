package shams.moneylionassessment.assessment.data.models;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Getter
@Setter
@Accessors(fluent = true)
@Entity
@Table(name = "USER_FEATURE_MAPPING")
public class UserFeatureMapping {

    @EmbeddedId
    private UserFeatureId id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FEATURE_NAME", insertable = false, updatable = false)
    private Feature feature;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "USERNAME", insertable = false, updatable = false)
    private User user;
}