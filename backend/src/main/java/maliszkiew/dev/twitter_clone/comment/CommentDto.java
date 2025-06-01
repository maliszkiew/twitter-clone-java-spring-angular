package maliszkiew.dev.twitter_clone.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private String content;
    private Long authorId;
    private String authorUsername;
    private Long postId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer likesCount;
}
