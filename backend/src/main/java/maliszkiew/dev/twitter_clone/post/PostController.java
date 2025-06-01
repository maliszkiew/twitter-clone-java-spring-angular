package maliszkiew.dev.twitter_clone.post;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import maliszkiew.dev.twitter_clone.exception.AuthenticationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/post")
@PreAuthorize("hasAnyAuthority('USER', 'MODERATOR')")
public class PostController {
    private final PostService postService;
    private final PostMapper postMapper;

    @PostMapping("")
    public ResponseEntity<?> createPost(@RequestBody PostCreatorDto postCreatorDto, HttpSession session) {
        String username = (String) session.getAttribute("us eer");
        PostFeedDto postFeedDto = null;
        if (username != null) {
            postFeedDto = postMapper.toPostFeedDto(postService.createPost(postCreatorDto, username));
            return ResponseEntity.ok(postFeedDto);
        } else {
            throw new AuthenticationException("User not authenticated");
        }
    }

    @GetMapping("/latest")
    public ResponseEntity<List<PostFeedDto>> getLatestPosts(@RequestParam(required = false, defaultValue = "0") int page,
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

