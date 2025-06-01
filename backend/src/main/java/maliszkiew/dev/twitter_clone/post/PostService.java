package maliszkiew.dev.twitter_clone.post;

import lombok.RequiredArgsConstructor;
import maliszkiew.dev.twitter_clone.exception.PostNotFoundException;
import maliszkiew.dev.twitter_clone.exception.UserNotFoundException;
import maliszkiew.dev.twitter_clone.user.User;
import maliszkiew.dev.twitter_clone.user.UserRepo;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepo postRepo;
    private final UserRepo userRepo;
    private final PostMapper postMapper;

    public Post createPost(PostCreatorDto postCreatorDto, String username) {
        User author = userRepo.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        LocalDateTime now = LocalDateTime.now();
        Post post = Post.builder()
                .author(author)
                .content(postCreatorDto.getContent())
                .createdAt(now)
                .updatedAt(now)
                .build();
        return postRepo.save(post);
    }

    public List<Post> getPostsContaining(String keyword, int page, int size) {
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Page number and size must be positive");
        }
        if (keyword == null || keyword.isEmpty()) {
            return postRepo.findAll(PageRequest.of(page, size)).getContent();
        }
        return postRepo.findByContentContainingIgnoreCaseOrderByCreatedAtDesc(keyword, PageRequest.of(page, size)).getContent();
    }

    public List<Post> getLatestPosts(int page, int size) {
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Page number and size must be positive");
        }
        return postRepo.findAllByOrderByCreatedAtDesc(PageRequest.of(page, size)).getContent();
    }

    public List<Post> getUserPosts(String username, int page, int size) {
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Page number and size must be positive");
        }
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        return postRepo.findAllByAuthorOrderByCreatedAtDesc(user, PageRequest.of(page, size)).getContent();
    }

    public Post updatePost(Long postId, String content, String username) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        if (!post.getAuthor().getUsername().equals(username)) {
            throw new AccessDeniedException("You can only update your own posts");
        }

        post.setContent(content);
        post.setUpdatedAt(LocalDateTime.now());

        return postRepo.save(post);
    }

    public void deletePost(Long postId, String username) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        if (!post.getAuthor().getUsername().equals(username)) {
            throw new AccessDeniedException("You can only delete your own posts");
        }

        postRepo.delete(post);
    }
}

