package com.example.memetory.domain.comment.service;

import com.example.memetory.domain.comment.dto.CommentServiceDto;
import com.example.memetory.domain.comment.entity.Comment;
import com.example.memetory.domain.comment.exception.NotFoundCommentException;
import com.example.memetory.domain.comment.repository.CommentRepository;
import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.service.MemberService;
import com.example.memetory.domain.memes.entity.Memes;
import com.example.memetory.domain.memes.service.MemesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberService memberService;
    private final MemesService memesService;

    @Transactional
    public void register(CommentServiceDto commentServiceDto) {
        Member foundMember = memberService.findMemberFromEmail(commentServiceDto.getEmail());
        Memes foundMemes = memesService.findMemesFromMemesId(commentServiceDto.getMemesId());
        foundMemes.addCommentCount();

        Comment newComment = commentServiceDto.toEntity(foundMember, foundMemes);

        commentRepository.save(newComment);
    }

    @Transactional
    public void delete(CommentServiceDto commentServiceDto) {
        Comment foundComment = commentRepository.findById(commentServiceDto.getCommentId()).orElseThrow(NotFoundCommentException::new);
        Memes foundMemes = memesService.findMemesFromMemesId(foundComment.getMemes().getId());
        foundMemes.cancelCommentCount();

        commentRepository.delete(foundComment);
    }
}
