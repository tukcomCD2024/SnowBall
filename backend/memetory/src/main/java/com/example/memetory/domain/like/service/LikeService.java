package com.example.memetory.domain.like.service;

import com.example.memetory.domain.like.dto.LikeServiceDto;
import com.example.memetory.domain.like.entity.Like;
import com.example.memetory.domain.like.exception.NotFoundLikeException;
import com.example.memetory.domain.like.repository.LikeRepository;
import com.example.memetory.domain.member.entity.Member;
import com.example.memetory.domain.member.service.MemberService;
import com.example.memetory.domain.memes.entity.Memes;
import com.example.memetory.domain.memes.service.MemesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final MemberService memberService;
    private final MemesService memesService;
    private final LikeRepository likeRepository;

    @Transactional
    public void register(LikeServiceDto likeServiceDto) {
        Member member = memberService.findByEmail(likeServiceDto.getEmail());
        Memes memes = memesService.getMemesBetweenService(likeServiceDto.getMemesId());
        memes.addLikeCount();

        Like newLike = likeServiceDto.toEntity(member, memes);
        likeRepository.save(newLike);
    }

    @Transactional
    public void cancel(LikeServiceDto likeServiceDto) {
        Member member = memberService.findByEmail(likeServiceDto.getEmail());
        Memes memes = memesService.getMemesBetweenService(likeServiceDto.getMemesId());
        memes.cancelLikeCount();

        Like like = likeRepository.findLikeByMemberAndMemes(member, memes).orElseThrow(NotFoundLikeException::new);

        likeRepository.delete(like);
    }
}
