package com.sstohnij.stacktraceqabackendv0.service.impl;

import com.sstohnij.stacktraceqabackendv0.dto.request.CreatePostRequest;
import com.sstohnij.stacktraceqabackendv0.dto.response.PostResponse;
import com.sstohnij.stacktraceqabackendv0.entity.AppUser;
import com.sstohnij.stacktraceqabackendv0.entity.Category;
import com.sstohnij.stacktraceqabackendv0.entity.Post;
import com.sstohnij.stacktraceqabackendv0.exception.custom.NotValidRequestParameter;
import com.sstohnij.stacktraceqabackendv0.mapper.PostMapper;
import com.sstohnij.stacktraceqabackendv0.repository.AppUserRepository;
import com.sstohnij.stacktraceqabackendv0.repository.CategoryRepository;
import com.sstohnij.stacktraceqabackendv0.repository.PostRepository;
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
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final AppUserRepository appUserRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public PostResponse getPostById(Long id) {
        log.info("Trying to get post with id='{}'", id);

        Post post = postRepository.getPostById(id).orElseThrow(() -> new EntityNotFoundException(
                String.format("Post with id='%s' not found", id)
        ));

        return PostMapper.toPostResponse(post);
    }

    @Override
    public PostResponse createPost(CreatePostRequest postRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser appUser = appUserRepository.findByUsername(authentication.getName()).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with username='%s' not found", authentication.getName()))
        );

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

}
