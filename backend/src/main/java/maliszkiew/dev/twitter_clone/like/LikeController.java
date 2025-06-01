package maliszkiew.dev.twitter_clone.like;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/post/{postId}")
    public ResponseEntity<Void> likePost(
            @PathVariable Long postId,
            Authentication authentication) {

        likeService.likePost(postId, authentication.getName());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<Void> unlikePost(
            @PathVariable Long postId,
            Authentication authentication) {

        likeService.unlikePost(postId, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/post/{postId}/status")
    public ResponseEntity<Map<String, Boolean>> getLikeStatus(
            @PathVariable Long postId,
            Authentication authentication) {

        boolean hasLiked = likeService.hasUserLikedPost(postId, authentication.getName());
        return ResponseEntity.ok(Map.of("liked", hasLiked));
    }

    @GetMapping("/post/{postId}/count")
    public ResponseEntity<Map<String, Integer>> getLikeCount(
            @PathVariable Long postId) {

        int count = likeService.getPostLikesCount(postId);
        return ResponseEntity.ok(Map.of("count", count));
    }
}
