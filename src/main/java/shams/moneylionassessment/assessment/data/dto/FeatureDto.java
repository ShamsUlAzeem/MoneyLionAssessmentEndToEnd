package shams.moneylionassessment.assessment.data.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import shams.moneylionassessment.assessment.data.models.Feature;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * A DTO for the {@link Feature} entity
 */
@Getter
@Setter
@Accessors(chain = true)
public class FeatureDto implements Serializable {
    @Size(max = 255)
    private String name;

    public static FeatureDto of(Feature feature) {
        return new FeatureDto().setName(feature.name());
    }
}