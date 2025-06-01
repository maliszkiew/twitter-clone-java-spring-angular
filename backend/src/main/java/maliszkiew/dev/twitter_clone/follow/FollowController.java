package maliszkiew.dev.twitter_clone.follow;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;

    @PostMapping("/{username}")
    public ResponseEntity<Void> followUser(
            @PathVariable String username,
            Authentication authentication) {

        followService.followUser(authentication.getName(), username);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> unfollowUser(
            @PathVariable String username,
            Authentication authentication) {

        followService.unfollowUser(authentication.getName(), username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{username}")
    public ResponseEntity<Map<String, Boolean>> getFollowStatus(
            @PathVariable String username,
            Authentication authentication) {

        boolean isFollowing = followService.isFollowing(authentication.getName(), username);
        return ResponseEntity.ok(Map.of("following", isFollowing));
    }

    @GetMapping("/{username}/followers")
    public ResponseEntity<List<String>> getFollowers(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return ResponseEntity.ok(followService.getFollowers(username, page, size));
    }

    @GetMapping("/{username}/following")
    public ResponseEntity<List<String>> getFollowing(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return ResponseEntity.ok(followService.getFollowing(username, page, size));
    }

    @GetMapping("/{username}/counts")
    public ResponseEntity<Map<String, Integer>> getFollowCounts(
            @PathVariable String username) {

        return ResponseEntity.ok(Map.of(
                "followers", followService.getFollowersCount(username),
                "following", followService.getFollowingCount(username)
        ));
    }
}
