package maliszkiew.dev.twitter_clone.comment.like;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import maliszkiew.dev.twitter_clone.comment.Comment;
import maliszkiew.dev.twitter_clone.comment.CommentRepo;
import maliszkiew.dev.twitter_clone.exception.CommentNotFoundException;
import maliszkiew.dev.twitter_clone.exception.ResourceAlreadyExistsException;
import maliszkiew.dev.twitter_clone.exception.ResourceNotFoundException;
import maliszkiew.dev.twitter_clone.exception.UserNotFoundException;
import maliszkiew.dev.twitter_clone.user.User;
import maliszkiew.dev.twitter_clone.user.UserRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentLikeService {
    private final CommentLikeRepo commentLikeRepo;
    private final UserRepo userRepo;
    private final CommentRepo commentRepo;

    @Transactional
    public void likeComment(Long commentId, String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with ID: " + commentId));

        if (commentLikeRepo.existsByUserAndComment(user, comment)) {
            throw new ResourceAlreadyExistsException("User already liked this comment");
        }

        CommentLike like = CommentLike.builder()
                .user(user)
                .comment(comment)
                .createdAt(LocalDateTime.now())
                .build();

        commentLikeRepo.save(like);

        comment.setLikesCount(comment.getLikesCount() + 1);
        commentRepo.save(comment);
    }

    @Transactional
    public void unlikeComment(Long commentId, String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with ID: " + commentId));

        CommentLike like = commentLikeRepo.findByUserAndComment(user, comment)
                .orElseThrow(() -> new ResourceNotFoundException("User has not liked this comment"));

        commentLikeRepo.delete(like);

        comment.setLikesCount(Math.max(0, comment.getLikesCount() - 1));
        commentRepo.save(comment);
    }

    public boolean hasUserLikedComment(Long commentId, String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with ID: " + commentId));

        return commentLikeRepo.existsByUserAndComment(user, comment);
    }

    public int getCommentLikesCount(Long commentId) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with ID: " + commentId));

        return comment.getLikesCount();
    }
}
