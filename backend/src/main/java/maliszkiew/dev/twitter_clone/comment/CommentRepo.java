package maliszkiew.dev.twitter_clone.comment;

import maliszkiew.dev.twitter_clone.post.Post;
import maliszkiew.dev.twitter_clone.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepo extends JpaRepository<Comment, Long> {
    Page<Comment> findByPostOrderByCreatedAtDesc(Post post, Pageable pageable);

    Page<Comment> findByAuthorOrderByCreatedAtDesc(User author, Pageable pageable);

    Page<Comment> findByContentContainingIgnoreCaseOrderByCreatedAtDesc(String keyword, Pageable pageable);

    Page<Comment> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
