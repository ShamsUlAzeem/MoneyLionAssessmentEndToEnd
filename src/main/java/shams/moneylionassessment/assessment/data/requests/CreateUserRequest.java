package shams.moneylionassessment.assessment.data.requests;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@Accessors(chain = true)
public class CreateUserRequest implements Serializable {
    @Size(max = 50)
    private String username;

    @Size(max = 500)
    private String password;
}
