package maliszkiew.dev.twitter_clone.post;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostCreatorDto {
    private String content;
}
