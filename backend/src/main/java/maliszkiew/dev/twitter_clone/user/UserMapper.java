package maliszkiew.dev.twitter_clone.user;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .userRole(user.getUserRole())
                .bio(user.getBio())
                .postsCount(user.getPostsCount())
                .build();
    }
}
