package maliszkiew.dev.twitter_clone.comment.like;

import maliszkiew.dev.twitter_clone.comment.Comment;
import maliszkiew.dev.twitter_clone.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentLikeRepo extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByUserAndComment(User user, Comment comment);
    boolean existsByUserAndComment(User user, Comment comment);
    int countByComment(Comment comment);
    void deleteByUserAndComment(User user, Comment comment);
}
