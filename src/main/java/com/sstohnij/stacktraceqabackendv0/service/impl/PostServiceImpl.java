package com.sstohnij.stacktraceqabackendv0.service.impl;

import com.sstohnij.stacktraceqabackendv0.dto.request.CreatePostRequest;
import com.sstohnij.stacktraceqabackendv0.dto.request.LikeRequest;
import com.sstohnij.stacktraceqabackendv0.dto.request.UpdateCommentRequest;
import com.sstohnij.stacktraceqabackendv0.dto.response.CommentResponse;
import com.sstohnij.stacktraceqabackendv0.dto.response.LikeOpResponse;
import com.sstohnij.stacktraceqabackendv0.dto.response.PostResponse;
import com.sstohnij.stacktraceqabackendv0.entity.*;
import com.sstohnij.stacktraceqabackendv0.enums.LikeOpResult;
import com.sstohnij.stacktraceqabackendv0.exception.custom.NotValidRequestParameter;
import com.sstohnij.stacktraceqabackendv0.mapper.CommentMapper;
import com.sstohnij.stacktraceqabackendv0.mapper.PostMapper;
import com.sstohnij.stacktraceqabackendv0.repository.*;
import com.sstohnij.stacktraceqabackendv0.service.PostService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final AppUserRepository appUserRepository;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    @Override
    public PostResponse getPostById(Long id) {
        log.info("Trying to get post with id='{}'", id);

        Post post = postRepository.getPostById(id).orElseThrow(() -> new EntityNotFoundException(
                String.format("Post with id='%s' not found", id)
        ));

        final long likes = likeRepository.countByPostIdAndIsDislikeIsFalse(id);
        final long dislikes = likeRepository.countByPostIdAndIsDislikeIsTrue(id);
        final long comments = commentRepository.countByPostId(id);

        PostResponse response = PostMapper.toPostResponse(post);
        response.setLikes(likes);
        response.setDislikes(dislikes);
        response.setComments(comments);
        // If user is authenticated we add additional info about user reaction
        // to post
        try {
            AppUser user = getAuthenticatedUser();
            Optional<Like> userPostLike = likeRepository.findByUserAndPost(user, post);
            userPostLike.ifPresent(like -> response.setUserReaction(like.isDislike() ? LikeOpResult.DISLIKED : LikeOpResult.LIKED));
        } catch (Exception ignored) {}

        return response;
    }

    @Override
    public PostResponse createPost(CreatePostRequest postRequest) {

        AppUser appUser = getAuthenticatedUser();

        Set<Category> postCategories = categoryRepository.findByIdIn(postRequest.getCategories())
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if(postCategories.size() != postRequest.getCategories().size()) {
            throw new NotValidRequestParameter(String.format("Request parameter: 'categories' which is %s contains not valid ids", postRequest.getCategories()));
        }

        Post post = PostMapper.fromPostRequest(postRequest);
        post.setUser(appUser);
        post.setPublishDate(Calendar.getInstance().getTime());
        post.setCategories(postCategories);
        post = postRepository.save(post);

        return PostMapper.toPostResponse(post);
    }

    @Override
    public CommentResponse addCommentToPost(Long postId, UpdateCommentRequest request) {
        AppUser appUser = getAuthenticatedUser();
        Post post = postRepository.getPostById(postId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Post with id='%d' not found", postId)));

        Comment comment = Comment.builder()
                .user(appUser)
                .content(request.getContent())
                .post(post)
                .publishDate(Calendar.getInstance().getTime())
                .build();

        comment = commentRepository.save(comment);
        return CommentMapper.toCommentResponse(comment);
    }

    @Override
    public LikeOpResponse likePost(Long postId, LikeRequest request) {
        AppUser user = getAuthenticatedUser();
        Post post = postRepository.getPostById(postId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Post with id='%d' not found", postId)));

        Optional<Like> postLike = likeRepository.findByUserAndPost(user, post);
        LikeOpResult likeOpResult = null;

        if(postLike.isPresent()) {
            Like likeInDB = postLike.get();
            if(likeInDB.isDislike() == request.getIsDislike()) {
                likeRepository.delete(likeInDB);
                likeOpResult = LikeOpResult.DELETED;
            } else {
                likeInDB.setDislike(request.getIsDislike());
                likeRepository.save(likeInDB);
            }
        } else {
            Like like = Like.builder()
                    .isDislike(request.getIsDislike())
                    .post(post)
                    .user(user)
                    .build();
            likeRepository.save(like);
        }

        if(likeOpResult == null)
            likeOpResult = request.getIsDislike() ? LikeOpResult.DISLIKED : LikeOpResult.LIKED;

        return new LikeOpResponse(likeOpResult);
    }

    private AppUser getAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return appUserRepository.findByUsername(authentication.getName()).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with username='%s' not found", authentication.getName()))
        );
    }

}
