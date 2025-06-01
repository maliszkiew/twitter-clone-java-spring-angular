package maliszkiew.dev.twitter_clone.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostFeedDto {
    private Long id;
    private Long authorId;
    private String authorUsername;
    private String content;
    private String  createdAt;
    private String updatedAt;
}
