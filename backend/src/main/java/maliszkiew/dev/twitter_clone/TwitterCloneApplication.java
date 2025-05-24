package maliszkiew.dev.twitter_clone;

import maliszkiew.dev.twitter_clone.post.Post;
import maliszkiew.dev.twitter_clone.post.PostRepo;
import maliszkiew.dev.twitter_clone.user.User;
import maliszkiew.dev.twitter_clone.user.UserRepo;
import maliszkiew.dev.twitter_clone.user.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class TwitterCloneApplication {
    public static void main(String[] args) {
        SpringApplication.run(TwitterCloneApplication.class, args);
    }

    @Autowired
    public void onInit(PasswordEncoder passwordEncoder, UserRepo userRepo, PostRepo postRepo) {
        User admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .userRole(UserRole.MODERATOR)
                .bio("Admin of the Twitter clone")
                .postsCount(0)
                .build();
        User user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("user"))
                .userRole(UserRole.USER)
                .bio("Regular user of the Twitter clone")
                .postsCount(0)
                .build();

        userRepo.save(user);
        userRepo.save(admin);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yesterday = now.minusDays(1);
        LocalDateTime lastWeek = now.minusDays(7);


        Post post1 = Post.builder()
                .author(admin)
                .content("Welcome to our Twitter clone! This is the first post from admin.")
                .createdAt(lastWeek)
                .updatedAt(lastWeek)
                .build();

        Post post2 = Post.builder()
                .author(user)
                .content("Hello everyone! I'm a regular user trying out this platform.")
                .createdAt(yesterday)
                .updatedAt(yesterday)
                .build();

        Post post3 = Post.builder()
                .author(admin)
                .content("Remember to follow the community guidelines when posting.")
                .createdAt(now)
                .updatedAt(now)
                .build();
        postRepo.saveAll(List.of(post1, post2, post3));
    }

}
