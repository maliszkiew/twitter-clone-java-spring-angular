package maliszkiew.dev.twitter_clone.like;

import maliszkiew.dev.twitter_clone.post.Post;
import maliszkiew.dev.twitter_clone.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepo extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndPost(User user, Post post);
    boolean existsByUserAndPost(User user, Post post);
    int countByPost(Post post);
    void deleteByUserAndPost(User user, Post post);
}
