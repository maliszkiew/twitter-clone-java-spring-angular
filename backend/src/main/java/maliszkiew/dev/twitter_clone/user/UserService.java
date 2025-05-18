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

    public void saveUser(UserLoginRegisterDto userLoginRegisterDto) {
        if (userRepo.findByUsername(userLoginRegisterDto.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException(userLoginRegisterDto.getUsername());
        }
        String encryptedPassword = passwordEncoder.encode(userLoginRegisterDto.getPassword());
        User userToSave = User.builder()
                .username(userLoginRegisterDto.getUsername())
                .password(encryptedPassword)
                .userRole(UserRole.USER).build();
        userRepo.save(userToSave);
    }

    public void loginUser(UserLoginRegisterDto userLoginRegisterDto) {
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
    }
}
