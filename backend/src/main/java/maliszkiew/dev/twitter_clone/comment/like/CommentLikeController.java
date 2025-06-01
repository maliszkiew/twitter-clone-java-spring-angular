package maliszkiew.dev.twitter_clone.comment.like;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/comments/likes")
@RequiredArgsConstructor
public class CommentLikeController {
    private final CommentLikeService commentLikeService;

    @PostMapping("/{commentId}")
    public ResponseEntity<Void> likeComment(
            @PathVariable Long commentId,
            Authentication authentication) {

        commentLikeService.likeComment(commentId, authentication.getName());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> unlikeComment(
            @PathVariable Long commentId,
            Authentication authentication) {

        commentLikeService.unlikeComment(commentId, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{commentId}/status")
    public ResponseEntity<Map<String, Boolean>> getLikeStatus(
            @PathVariable Long commentId,
            Authentication authentication) {

        boolean hasLiked = commentLikeService.hasUserLikedComment(commentId, authentication.getName());
        return ResponseEntity.ok(Map.of("liked", hasLiked));
    }

    @GetMapping("/{commentId}/count")
    public ResponseEntity<Map<String, Integer>> getLikeCount(
            @PathVariable Long commentId) {

        int count = commentLikeService.getCommentLikesCount(commentId);
        return ResponseEntity.ok(Map.of("count", count));
    }
}
