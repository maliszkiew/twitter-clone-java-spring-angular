package maliszkiew.dev.twitter_clone.user;

import lombok.RequiredArgsConstructor;
import maliszkiew.dev.twitter_clone.exception.AuthenticationException;
import maliszkiew.dev.twitter_clone.exception.UserAlreadyExistsException;
import maliszkiew.dev.twitter_clone.exception.UserNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepo userRepo;
    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public User saveUser(UserLoginRegisterDto userLoginRegisterDto) {
        if (userRepo.findByUsername(userLoginRegisterDto.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException(userLoginRegisterDto.getUsername());
        }
        String encryptedPassword = passwordEncoder.encode(userLoginRegisterDto.getPassword());
        User userToSave = User.builder()
                .username(userLoginRegisterDto.getUsername())
                .password(encryptedPassword)
                .userRole(UserRole.USER)
                .bio("")
                .postsCount(0)
                .build();
        return userRepo.save(userToSave);
    }
    public User loginUser(UserLoginRegisterDto userLoginRegisterDto) {
        Optional<User> user = userRepo.findByUsername(userLoginRegisterDto.getUsername());
        if (user.isEmpty()) {
            throw new UserNotFoundException(userLoginRegisterDto.getUsername());
        }
        boolean passwordMatches = passwordEncoder.matches(
                userLoginRegisterDto.getPassword(),
                user.get().getPassword()
        );
        if (!passwordMatches) {
            throw new AuthenticationException("Invalid password");
        }
        return user.get();
    }

    public User getUserByUsername(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        return user;
    }
}
