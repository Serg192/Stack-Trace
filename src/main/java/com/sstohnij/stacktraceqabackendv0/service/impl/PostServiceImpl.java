package com.sstohnij.stacktraceqabackendv0.service.impl;

import com.sstohnij.stacktraceqabackendv0.dto.internal.PostSummaryDTO;
import com.sstohnij.stacktraceqabackendv0.dto.request.UpdatePostRequest;
import com.sstohnij.stacktraceqabackendv0.dto.request.LikeRequest;
import com.sstohnij.stacktraceqabackendv0.dto.request.PostsPageRequest;
import com.sstohnij.stacktraceqabackendv0.dto.request.UpdateCommentRequest;
import com.sstohnij.stacktraceqabackendv0.dto.response.CommentResponse;
import com.sstohnij.stacktraceqabackendv0.dto.response.LikeOpResponse;
import com.sstohnij.stacktraceqabackendv0.dto.response.PostResponse;
import com.sstohnij.stacktraceqabackendv0.dto.response.PostsPageResponse;
import com.sstohnij.stacktraceqabackendv0.entity.*;
import com.sstohnij.stacktraceqabackendv0.enums.LikeOpResult;
import com.sstohnij.stacktraceqabackendv0.enums.PostSortOption;
import com.sstohnij.stacktraceqabackendv0.enums.RoleName;
import com.sstohnij.stacktraceqabackendv0.exception.custom.NotValidRequestParameter;
import com.sstohnij.stacktraceqabackendv0.exception.custom.PermissionException;
import com.sstohnij.stacktraceqabackendv0.mapper.CommentMapper;
import com.sstohnij.stacktraceqabackendv0.mapper.PostMapper;
import com.sstohnij.stacktraceqabackendv0.repository.*;
import com.sstohnij.stacktraceqabackendv0.service.PostService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.archive.scan.spi.ClassDescriptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.authentication.AuthenticationProviderBeanDefinitionParser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.MethodNotAllowedException;

import java.text.SimpleDateFormat;
import java.util.*;
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

        PostSummaryDTO tmp = postRepository.getPostById(id).orElseThrow(() -> new EntityNotFoundException(
                String.format("Post with id='%s' not found", id)
        ));

        PostResponse response = PostMapper.postSummaryToPostResponse(tmp);

        // If user is authenticated we add additional info about user reaction
        // to post
        try {
            AppUser user = getAuthenticatedUser();
            Optional<Like> userPostLike = likeRepository.findByUserAndPost(user, tmp.getPost());
            userPostLike.ifPresent(like -> response.setUserReaction(like.isDislike() ? LikeOpResult.DISLIKED : LikeOpResult.LIKED));
        } catch (Exception ignored) {}

        return response;
    }

    @Override
    public PostResponse createPost(UpdatePostRequest postRequest) {

        AppUser appUser = getAuthenticatedUser();

        Set<Category> postCategories = categoryRepository.findByIdIn(postRequest.getCategories());

        Post post = PostMapper.fromPostRequest(postRequest);
        post.setUser(appUser);
        post.setPublishDate(Calendar.getInstance().getTime());
        post.setCategories(postCategories);
        post = postRepository.save(post);

        return PostMapper.toPostResponse(post);
    }

    @Override
    public PostResponse editPost(Long postId, UpdatePostRequest request) {

        PostSummaryDTO summaryDTO = getPostIfHasPermission(postId, "User can edit only his own posts");
        Set<Category> postCategories = categoryRepository.findByIdIn(request.getCategories());

        Post updatedPost = summaryDTO.getPost();
        updatedPost.setTitle(request.getTitle());
        updatedPost.setPostContent(request.getPostContent());
        updatedPost.setCategories(postCategories);
        updatedPost = postRepository.save(updatedPost);
        summaryDTO.setPost(updatedPost);

        return PostMapper.postSummaryToPostResponse(summaryDTO);
    }

    @Override
    public CommentResponse addCommentToPost(Long postId, UpdateCommentRequest request) {
        AppUser appUser = getAuthenticatedUser();

        PostSummaryDTO summaryDTO = postRepository.getPostById(postId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Post with id='%d' not found", postId)));

        Comment comment = Comment.builder()
                .user(appUser)
                .content(request.getContent())
                .post(summaryDTO.getPost())
                .publishDate(Calendar.getInstance().getTime())
                .build();

        comment = commentRepository.save(comment);
        return CommentMapper.toCommentResponse(comment);
    }

    @Override
    public LikeOpResponse likePost(Long postId, LikeRequest request) {
        AppUser user = getAuthenticatedUser();
        PostSummaryDTO summaryDTO = postRepository.getPostById(postId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Post with id='%d' not found", postId)));

        Optional<Like> postLike = likeRepository.findByUserAndPost(user, summaryDTO.getPost());
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
                    .post(summaryDTO.getPost())
                    .user(user)
                    .build();
            likeRepository.save(like);
        }

        if(likeOpResult == null)
            likeOpResult = request.getIsDislike() ? LikeOpResult.DISLIKED : LikeOpResult.LIKED;

        return new LikeOpResponse(likeOpResult);
    }

    @Override
    public PostsPageResponse getAllPosts(PostsPageRequest request) {

        int page = Math.max(request.getPageNumber(), 0);
        int pageSize = request.getPageSize() > 0 ? request.getPageSize() : 10;

        Date startDate = null, endDate = null;
        SimpleDateFormat dateFt = new SimpleDateFormat("yyyy-MM-dd");

        try {
            startDate = dateFt.parse(request.getStartDate());
            endDate = dateFt.parse(request.getEndDate());
        } catch (Exception ignored){}

        Set<Long> filterCategories = request.getCategories();
        long catSize = 0;
        if(Objects.nonNull(filterCategories))
            catSize = filterCategories.size();

        Page<PostSummaryDTO> postsSummary = postRepository.getSorterAndFilteredPostPage(
                getSortIOpt(request.getSort()),
                filterCategories,
                catSize,
                startDate,
                endDate,
                PageRequest.of(page, pageSize)
        );

        List<PostResponse> pRes = postsSummary.getContent().stream().map(PostMapper::postSummaryToPostResponse).toList();
        return PostsPageResponse.builder()
                .posts(pRes)
                .total(postsSummary.getTotalPages())
                .currentPage(postsSummary.getNumber())
                .pageSize(postsSummary.getSize())
                .build();
    }

    @Override
    public void deletePost(Long postId) {
        PostSummaryDTO summaryDTO = getPostIfHasPermission(postId, String.format("User with role: '%s' can only delete his own posts", RoleName.ROLE_USER.str()));
        postRepository.delete(summaryDTO.getPost());
    }

    private PostSummaryDTO getPostIfHasPermission(Long postId, String noPermissionExceptionText){
        AppUser user = getAuthenticatedUser();
        PostSummaryDTO summaryDTO = postRepository.getPostById(postId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Post with id='%d' not found in the database", postId))
        );

        if(!Objects.equals(user.getId(), summaryDTO.getPost().getUser().getId())) {
            throw new PermissionException(String.format(noPermissionExceptionText));
        }
        return summaryDTO;
    }

    private String getSortIOpt(String requestSort) {
        return Arrays.stream(PostSortOption.values())
                .map(Enum::name)
                .filter(requestSort::equals)
                .findFirst()
                .orElse(PostSortOption.MOST_LIKED.name());
    }

    private AppUser getAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return appUserRepository.findByUsername(authentication.getName()).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with username='%s' not found", authentication.getName()))
        );
    }
}
