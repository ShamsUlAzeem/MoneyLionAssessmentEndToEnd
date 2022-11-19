package shams.moneylionassessment.assessment.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import shams.moneylionassessment.assessment.data.models.User;
import shams.moneylionassessment.assessment.data.repository.UserRepository;

@Service
public class UserService {

    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean userExists(String username) {
        return userRepository.existsById(username);
    }

    public User createUser(String username, String password) {
        return userRepository.save(new User().username(username).password(passwordEncoder.encode(password)).enabled(true));
    }

    public void removeUser(String username) {
        userRepository.deleteById(username);
    }
}
