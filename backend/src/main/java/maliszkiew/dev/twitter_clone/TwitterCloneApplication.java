package maliszkiew.dev.twitter_clone;

import maliszkiew.dev.twitter_clone.user.User;
import maliszkiew.dev.twitter_clone.user.UserRepo;
import maliszkiew.dev.twitter_clone.user.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class TwitterCloneApplication {
    public static void main(String[] args) {
        SpringApplication.run(TwitterCloneApplication.class, args);
    }

    @Autowired
    public void onInit(PasswordEncoder passwordEncoder, UserRepo userRepo) {
        User admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .userRole(UserRole.MODERATOR)
                .build();
        User user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("user"))
                .userRole(UserRole.USER)
                .build();
        userRepo.save(user);
        userRepo.save(admin);
    }

}
