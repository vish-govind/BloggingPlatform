package com.BloggingPlatform.ByteBlog.Mapper;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.BloggingPlatform.ByteBlog.Dto.CommentDto;
import com.BloggingPlatform.ByteBlog.Entity.Comment;

@Component
public class CommentMapper {

    private static final Logger logger = LoggerFactory.getLogger(CommentMapper.class);

    public CommentDto toDTO(Comment comment) {
        if (comment == null) {
            logger.warn("Attempted to map a null Comment entity to DTO.");
            return null;
        }

        logger.info("Mapping Comment entity (id: {}) to DTO.", comment.getCommentId());
        return new CommentDto(comment);
    }

    public List<CommentDto> toDTOList(List<Comment> comments) {
        if (comments == null || comments.isEmpty()) {
            logger.warn("Attempted to map an empty or null Comment list to DTO list.");
            return new ArrayList<>();
        }

        logger.info("Mapping {} Comment entities to DTO list.", comments.size());
        List<CommentDto> commentDTOs = new ArrayList<>();
        for (Comment comment : comments) {
            commentDTOs.add(new CommentDto(comment));
        }
        return commentDTOs;
    }

    public static Comment toEntity(CommentDto commentDto) {
        if (commentDto == null) {
            logger.warn("Attempted to map a null CommentDto to entity.");
            return null;
        }

        logger.info("Mapping CommentDto (id: {}) to entity.", commentDto.getCommentId());
        Comment comment = new Comment();
        comment.setCommentId(commentDto.getCommentId());
        comment.setBlogId(commentDto.getBlogId());
        comment.setComment(commentDto.getComment());
        comment.setAuthorName(commentDto.getAuthorUsername());
        comment.setCreatedAt(commentDto.getCreatedAt());
        return comment;
    }
}
