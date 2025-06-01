package maliszkiew.dev.twitter_clone.post;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PostDto {
    private Long id;
    private Long authorId;
    private String authorName;
    private String content;
    private String createdAt;
    private String updatedAt;
}
