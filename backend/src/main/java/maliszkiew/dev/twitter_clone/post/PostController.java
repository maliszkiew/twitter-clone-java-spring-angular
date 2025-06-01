package maliszkiew.dev.twitter_clone.post;

import lombok.RequiredArgsConstructor;
import maliszkiew.dev.twitter_clone.exception.AuthenticationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/post")
@PreAuthorize("hasAnyAuthority('USER', 'MODERATOR')")
public class PostController {
    private final PostService postService;
    private final PostMapper postMapper;

    @PostMapping("")
    public ResponseEntity<?> createPost(@RequestBody PostCreatorDto postCreatorDto, Authentication authentication) {
        String username = authentication.getName();
        PostFeedDto postFeedDto = postMapper.toPostFeedDto(postService.createPost(postCreatorDto, username));
        return ResponseEntity.ok(postFeedDto);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostFeedDto> updatePost(
            @PathVariable Long postId,
            @RequestBody Map<String, String> payload,
            Authentication authentication) {

        String content = payload.get("content");
        if (content == null || content.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Post post = postService.updatePost(postId, content, authentication.getName());
        return ResponseEntity.ok(postMapper.toPostFeedDto(post));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId,
            Authentication authentication) {

        postService.deletePost(postId, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/latest")
    public ResponseEntity<List<PostFeedDto>> getLatestPosts(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        List<PostFeedDto> posts = postService.getLatestPosts(page, size).stream().map(postMapper::toPostFeedDto).toList();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<PostFeedDto>> getUserPosts(
            @PathVariable String username,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        List<PostFeedDto> posts = postService.getUserPosts(username, page, size)
                .stream()
                .map(postMapper::toPostFeedDto)
                .toList();
        return ResponseEntity.ok(posts);
    }
}

