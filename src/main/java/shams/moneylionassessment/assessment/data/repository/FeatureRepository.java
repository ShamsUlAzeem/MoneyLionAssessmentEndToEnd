package shams.moneylionassessment.assessment.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shams.moneylionassessment.assessment.data.models.Feature;

public interface FeatureRepository extends JpaRepository<Feature, String> {
}