package maliszkiew.dev.twitter_clone.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserLoginRegisterDto {
    private String username;
    private String password;
}
