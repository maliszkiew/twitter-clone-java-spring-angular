package maliszkiew.dev.twitter_clone.comment;

import lombok.RequiredArgsConstructor;
import maliszkiew.dev.twitter_clone.exception.CommentNotFoundException;
import maliszkiew.dev.twitter_clone.exception.PostNotFoundException;
import maliszkiew.dev.twitter_clone.exception.UserNotFoundException;
import maliszkiew.dev.twitter_clone.post.Post;
import maliszkiew.dev.twitter_clone.post.PostRepo;
import maliszkiew.dev.twitter_clone.user.User;
import maliszkiew.dev.twitter_clone.user.UserRepo;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepo commentRepo;
    private final UserRepo userRepo;
    private final PostRepo postRepo;

    public List<Comment> getCommentsByPostId(Long postId, int page, int size) {
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Page number and size must be positive");
        }

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        return commentRepo.findByPostOrderByCreatedAtDesc(post, PageRequest.of(page, size)).getContent();
    }

    public Comment createComment(Long postId, String content, String username) {
        User author = userRepo.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        LocalDateTime now = LocalDateTime.now();

        Comment comment = Comment.builder()
                .author(author)
                .post(post)
                .content(content)
                .createdAt(now)
                .updatedAt(now)
                .build();

        return commentRepo.save(comment);
    }

    public void deleteComment(Long commentId, String username) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        if (!comment.getAuthor().getUsername().equals(username)) {
            throw new AccessDeniedException("You can only delete your own comments");
        }

        commentRepo.delete(comment);
    }

    public Comment updateComment(Long commentId, String content, String username) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        if (!comment.getAuthor().getUsername().equals(username)) {
            throw new AccessDeniedException("You can only update your own comments");
        }

        comment.setContent(content);
        comment.setUpdatedAt(LocalDateTime.now());

        return commentRepo.save(comment);
    }
}
