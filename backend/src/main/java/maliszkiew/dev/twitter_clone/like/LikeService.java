package maliszkiew.dev.twitter_clone.like;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import maliszkiew.dev.twitter_clone.exception.PostNotFoundException;
import maliszkiew.dev.twitter_clone.exception.ResourceAlreadyExistsException;
import maliszkiew.dev.twitter_clone.exception.ResourceNotFoundException;
import maliszkiew.dev.twitter_clone.exception.UserNotFoundException;
import maliszkiew.dev.twitter_clone.post.Post;
import maliszkiew.dev.twitter_clone.post.PostRepo;
import maliszkiew.dev.twitter_clone.user.User;
import maliszkiew.dev.twitter_clone.user.UserRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepo likeRepo;
    private final UserRepo userRepo;
    private final PostRepo postRepo;

    @Transactional
    public void likePost(Long postId, String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        if (likeRepo.existsByUserAndPost(user, post)) {
            throw new ResourceAlreadyExistsException("User already liked this post");
        }

        Like like = Like.builder()
                .user(user)
                .post(post)
                .createdAt(LocalDateTime.now())
                .build();

        likeRepo.save(like);

        post.setLikesCount(post.getLikesCount() + 1);
        postRepo.save(post);
    }

    @Transactional
    public void unlikePost(Long postId, String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        Like like = likeRepo.findByUserAndPost(user, post)
                .orElseThrow(() -> new ResourceNotFoundException("User has not liked this post"));

        likeRepo.delete(like);

        post.setLikesCount(Math.max(0, post.getLikesCount() - 1));
        postRepo.save(post);
    }

    public boolean hasUserLikedPost(Long postId, String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        return likeRepo.existsByUserAndPost(user, post);
    }

    public int getPostLikesCount(Long postId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        return post.getLikesCount();
    }
}

