package shams.moneylionassessment.assessment.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import shams.moneylionassessment.assessment.data.models.User;
import shams.moneylionassessment.assessment.data.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User authenticate(String username, String password) {
        User user = userRepository.findById(username).orElse(null);

        if(user != null && passwordEncoder.matches(password, user.password())) {
            return user;
        } else {
            return null;
        }
    }

    public boolean userExists(String username) {
        return userRepository.existsById(username);
    }

    public List<User> list() {
        return userRepository.findAll();
    }
    public User createUser(String username, String password) {
        return userRepository.save(new User().username(username).password(passwordEncoder.encode(password)).enabled(true));
    }

    public void removeUser(String username) {
        userRepository.deleteById(username);
    }
}
