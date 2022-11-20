package shams.moneylionassessment.assessment.service;

import org.springframework.stereotype.Service;
import shams.moneylionassessment.assessment.data.models.Feature;
import shams.moneylionassessment.assessment.data.models.UserFeatureId;
import shams.moneylionassessment.assessment.data.models.UserFeatureMapping;
import shams.moneylionassessment.assessment.data.repository.FeatureRepository;
import shams.moneylionassessment.assessment.data.repository.UserFeatureMappingRepository;

import java.util.List;
import java.util.Objects;

@Service
public class FeatureService {

    final FeatureRepository featureRepository;
    final UserFeatureMappingRepository userFeatureMappingRepository;

    public FeatureService(FeatureRepository featureRepository, UserFeatureMappingRepository userFeatureMappingRepository) {
        this.featureRepository = featureRepository;
        this.userFeatureMappingRepository = userFeatureMappingRepository;
    }

    public Feature saveFeature(String name) {
        return featureRepository.save(new Feature().name(name));
    }

    public Feature saveFeature(Feature feature) {
        return featureRepository.save(feature);
    }

    public Feature getFeature(String name) {
        return featureRepository.findById(name).orElse(null);
    }

    public boolean featureExists(String name) {
        return featureRepository.existsById(name);
    }

    public List<Feature> list() {
        return featureRepository.findAll();
    }

    public UserFeatureMapping assignFeature(String username, String featureName) {
        UserFeatureId userFeatureId = new UserFeatureId().username(username).featureName(featureName);

        return Objects.requireNonNullElseGet(
                userFeatureMappingRepository.findById(userFeatureId).orElse(null),
                () -> userFeatureMappingRepository.save(new UserFeatureMapping().id(userFeatureId))
        );
    }

    public void revokeFeature(String username, String featureName) {
        UserFeatureId userFeatureId = new UserFeatureId().username(username).featureName(featureName);

        if(userFeatureMappingRepository.existsById(userFeatureId)) {
            userFeatureMappingRepository.deleteById(userFeatureId);
        }
    }

    public boolean hasFeatureAccess(String username, String featureName) {
        return userFeatureMappingRepository.existsById(new UserFeatureId().username(username).featureName(featureName));
    }
}