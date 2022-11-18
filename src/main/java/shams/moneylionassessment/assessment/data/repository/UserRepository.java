package shams.moneylionassessment.assessment.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shams.moneylionassessment.assessment.data.models.User;

public interface UserRepository extends JpaRepository<User, String> {
}