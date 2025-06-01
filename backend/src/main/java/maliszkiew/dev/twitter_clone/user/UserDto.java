package maliszkiew.dev.twitter_clone.user;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserDto {
    private Long id;
    private String username;
    private UserRole userRole;
    private String bio;
    private Integer postsCount;
}
