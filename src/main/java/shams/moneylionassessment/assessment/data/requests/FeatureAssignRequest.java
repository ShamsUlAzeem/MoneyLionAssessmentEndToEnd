package shams.moneylionassessment.assessment.data.requests;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import shams.moneylionassessment.assessment.data.models.UserFeatureMapping;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * A request object for the {@link UserFeatureMapping} entity
 */
@Getter
@Setter
@Accessors(chain = true)
public class FeatureAssignRequest implements Serializable {
    @Size(max = 255)
    private String featureName;

    @Size(max = 50)
    private String email;

    private Boolean enable;
}
