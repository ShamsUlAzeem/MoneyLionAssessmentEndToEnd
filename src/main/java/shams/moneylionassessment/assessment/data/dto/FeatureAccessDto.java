package shams.moneylionassessment.assessment.data.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import shams.moneylionassessment.assessment.data.models.Feature;

import java.io.Serializable;

/**
 * A DTO for the {@link Feature} entity
 */
@Getter
@Setter
@Accessors(chain = true) // Can't use fluent accessor due to JSON serialization requirement
public class FeatureAccessDto implements Serializable {
    private Boolean canAccess;
}
