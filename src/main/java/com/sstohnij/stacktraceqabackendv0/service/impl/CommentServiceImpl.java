package com.sstohnij.stacktraceqabackendv0.service.impl;

import com.sstohnij.stacktraceqabackendv0.dto.request.UpdateCommentRequest;
import com.sstohnij.stacktraceqabackendv0.dto.response.CommentResponse;
import com.sstohnij.stacktraceqabackendv0.entity.AppUser;
import com.sstohnij.stacktraceqabackendv0.entity.Comment;
import com.sstohnij.stacktraceqabackendv0.exception.custom.PermissionException;
import com.sstohnij.stacktraceqabackendv0.mapper.CommentMapper;
import com.sstohnij.stacktraceqabackendv0.repository.AppUserRepository;
import com.sstohnij.stacktraceqabackendv0.repository.CommentRepository;
import com.sstohnij.stacktraceqabackendv0.service.CommentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AppUserRepository appUserRepository;

    @Override
    public CommentResponse editComment(Long commentId, UpdateCommentRequest request) {
        Comment comment = getCommentIfHasPermission(commentId, "User can edit his own comment");
        comment.setContent(request.getContent());
        comment = commentRepository.save(comment);
        return CommentMapper.toCommentResponse(comment);
    }

    @Override
    public CommentResponse getCommentById(Long commentId) {
        Comment comment = commentRepository.findCommentById(commentId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Comment with id='%d' not found in the database", commentId))
        );
        return CommentMapper.toCommentResponse(comment);
    }

    @Override
    public void deleteComment(Long commentId) {
        Comment comment = getCommentIfHasPermission(commentId, "User can only delete his own comment");
        commentRepository.delete(comment);
    }

    private Comment getCommentIfHasPermission(Long id, String noPermissionExceptionText) {
        AppUser user = getAuthenticatedUser();
        Comment comment = commentRepository.findCommentById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Comment with id='%d' not found in the database", id))
        );

        if(!Objects.equals(comment.getUser().getId(), user.getId())){
            throw new PermissionException(noPermissionExceptionText);
        }

        return comment;
    }

    private AppUser getAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return appUserRepository.findByUsername(authentication.getName()).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with username='%s' not found", authentication.getName()))
        );
    }
}
