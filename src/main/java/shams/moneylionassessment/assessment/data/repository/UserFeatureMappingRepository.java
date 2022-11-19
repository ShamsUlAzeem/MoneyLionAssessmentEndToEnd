package shams.moneylionassessment.assessment.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shams.moneylionassessment.assessment.data.models.UserFeatureId;
import shams.moneylionassessment.assessment.data.models.UserFeatureMapping;

public interface UserFeatureMappingRepository extends JpaRepository<UserFeatureMapping, UserFeatureId> {
}