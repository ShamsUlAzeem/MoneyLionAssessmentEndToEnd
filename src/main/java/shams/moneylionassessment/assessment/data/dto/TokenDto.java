package shams.moneylionassessment.assessment.data.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import shams.moneylionassessment.assessment.data.models.Feature;

import java.io.Serializable;

@Getter
@Setter
@Accessors(chain = true)
public class TokenDto implements Serializable {
    private String token;
}