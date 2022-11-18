package shams.moneylionassessment.assessment.data.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@Accessors(fluent = true)
@EqualsAndHashCode
@Embeddable
public class UserFeatureId implements Serializable {

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "FEATURE_NAME")
    private String featureName;

}