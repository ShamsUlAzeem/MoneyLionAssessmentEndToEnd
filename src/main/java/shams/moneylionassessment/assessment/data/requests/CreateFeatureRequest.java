package shams.moneylionassessment.assessment.data.requests;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@Accessors(chain = true)
public class CreateFeatureRequest implements Serializable {
    @Size(max = 255)
    private String name;
}
