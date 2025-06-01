package maliszkiew.dev.twitter_clone.follow;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import maliszkiew.dev.twitter_clone.exception.BadRequestException;
import maliszkiew.dev.twitter_clone.exception.UserNotFoundException;
import maliszkiew.dev.twitter_clone.user.User;
import maliszkiew.dev.twitter_clone.user.UserRepo;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepo followRepo;
    private final UserRepo userRepo;

    @Transactional
    public void followUser(String followerUsername, String followingUsername) {
        if (followerUsername.equals(followingUsername)) {
            throw new BadRequestException("Users cannot follow themselves");
        }

        User follower = userRepo.findByUsername(followerUsername)
                .orElseThrow(() -> new UserNotFoundException("Follower user not found"));

        User following = userRepo.findByUsername(followingUsername)
                .orElseThrow(() -> new UserNotFoundException("User to follow not found"));

        if (followRepo.existsByFollowerAndFollowing(follower, following)) {
            throw new BadRequestException("Already following this user");
        }

        Follow follow = Follow.builder()
                .follower(follower)
                .following(following)
                .createdAt(LocalDateTime.now())
                .build();

        followRepo.save(follow);

        follower.setFollowingCount(follower.getFollowingCount() + 1);
        following.setFollowersCount(following.getFollowersCount() + 1);

        userRepo.save(follower);
        userRepo.save(following);
    }

    @Transactional
    public void unfollowUser(String followerUsername, String followingUsername) {
        User follower = userRepo.findByUsername(followerUsername)
                .orElseThrow(() -> new UserNotFoundException("Follower user not found"));

        User following = userRepo.findByUsername(followingUsername)
                .orElseThrow(() -> new UserNotFoundException("User to unfollow not found"));

        Follow follow = followRepo.findByFollowerAndFollowing(follower, following)
                .orElseThrow(() -> new BadRequestException("Not following this user"));

        followRepo.delete(follow);

        follower.setFollowingCount(Math.max(0, follower.getFollowingCount() - 1));
        following.setFollowersCount(Math.max(0, following.getFollowersCount() - 1));

        userRepo.save(follower);
        userRepo.save(following);
    }

    public boolean isFollowing(String followerUsername, String followingUsername) {
        User follower = userRepo.findByUsername(followerUsername)
                .orElseThrow(() -> new UserNotFoundException("Follower user not found"));

        User following = userRepo.findByUsername(followingUsername)
                .orElseThrow(() -> new UserNotFoundException("User to check not found"));

        return followRepo.existsByFollowerAndFollowing(follower, following);
    }

    public List<String> getFollowers(String username, int page, int size) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return followRepo.findByFollowing(user, PageRequest.of(page, size))
                .getContent()
                .stream()
                .map(follow -> follow.getFollower().getUsername())
                .collect(Collectors.toList());
    }

    public List<String> getFollowing(String username, int page, int size) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return followRepo.findByFollower(user, PageRequest.of(page, size))
                .getContent()
                .stream()
                .map(follow -> follow.getFollowing().getUsername())
                .collect(Collectors.toList());
    }

    public int getFollowersCount(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return user.getFollowersCount();
    }

    public int getFollowingCount(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return user.getFollowingCount();
    }
}

