package com.example.memetory.domain.comment.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.memetory.domain.comment.dto.CommentInfo;
import com.example.memetory.domain.comment.dto.CommentInfoSlice;
import com.example.memetory.domain.comment.dto.request.CommentRequest;
import com.example.memetory.domain.comment.entity.Comment;
import com.example.memetory.domain.comment.exception.NotFoundCommentException;
import com.example.memetory.domain.comment.repository.CommentRepository;
import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.service.MemberService;
import com.example.memetory.domain.memes.entity.Memes;
import com.example.memetory.domain.memes.service.MemesService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
	private final CommentRepository commentRepository;
	private final MemberService memberService;
	private final MemesService memesService;

	@Transactional
	public CommentInfo saveComment(CommentRequest commentRequest) {
		Member member = memberService.findMemberFromEmail(commentRequest.getEmail());
		Memes memes = memesService.findMemesFromMemesId(commentRequest.getMemesId());

		Comment comment = commentRepository.save(
			Comment.builder()
				.member(member)
				.memes(memes)
				.content(commentRequest.getContent())
				.build()
		);

		memes.addCommentCount();

		return CommentInfo.of(comment);
	}

	@Transactional
	public void deleteComment(String email, Long commentId) {
		Comment comment = commentRepository.findById(commentId).orElseThrow(NotFoundCommentException::new);
		Member loginMember = memberService.findMemberFromEmail(email);

		memberService.certifyMember(loginMember, comment.getMember());

		comment.getMemes().cancelCommentCount();
		commentRepository.delete(comment);
	}

	@Transactional(readOnly = true)
	public CommentInfoSlice findCommentFromMemesId(Long memesId, Pageable pageable) {
		Slice<CommentInfo> commentInfos = commentRepository.findCommentsSliceByMemesId(memesId, pageable);

		return CommentInfoSlice.builder()
			.commentInfoList(commentInfos.getContent())
			.currentPage(pageable.getPageNumber())
			.hasNext(commentInfos.hasNext())
			.build();
	}
}
