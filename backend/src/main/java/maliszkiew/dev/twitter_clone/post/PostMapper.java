package maliszkiew.dev.twitter_clone.post;

import org.springframework.stereotype.Component;

@Component
public class PostMapper {
    public PostFeedDto toPostFeedDto(Post post) {
        return PostFeedDto.builder()
                .id(post.getId())
                .authorId(post.getAuthor().getId())
                .authorUsername(post.getAuthor().getUsername())
                .content(post.getContent())
                .createdAt(String.valueOf(post.getCreatedAt()))
                .updatedAt(String.valueOf(post.getUpdatedAt()))
                .build();
    }

}
